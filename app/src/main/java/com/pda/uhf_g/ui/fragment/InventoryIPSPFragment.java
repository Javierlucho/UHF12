package com.pda.uhf_g.ui.fragment;

import static com.pda.uhf_g.ui.base.NavHostFragment.findNavController;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pda.uhf_g.MainActivity;
import com.pda.uhf_g.R;

import com.pda.uhf_g.data.local.entities.TagInfo;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.ui.base.BaseFragment;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;
import com.pda.uhf_g.util.LogUtil;
import com.pda.uhf_g.util.SharedUtil;
import com.pda.uhf_g.util.UtilSound;
import com.uhf.api.cls.Reader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pda.serialport.Tools;

public class InventoryIPSPFragment extends BaseFragment {

    private InventoryViewModel viewModel;
    @BindView(R.id.button_inventory)
    FloatingActionButton btnInventory ;

    @BindView(R.id.button_location)
    Button btnLocation;

    @BindView(R.id.button_catalog)
    Button btnCatalog;

    @BindView(R.id.tv_lat)
    TextView tvLat;

    @BindView(R.id.tv_lon)
    TextView tvLon;

    @BindView(R.id.tv_tid)
    TextView tvTid;


    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_afid)
    TextView tvAfid;

    @BindView(R.id.tv_piscina)
    TextView tvPiscina;

    @BindView(R.id.my_image_view)
    ImageView imageView;


    private final Map<String, TagInfo> tagInfoMap = new LinkedHashMap<>();//
    private final List<TagInfo> tagInfoList = new ArrayList<>();//
    private MainActivity mainActivity ;

    private Long index = 1L;//
    private int time = 0;
    private boolean isReader = false;
    private Timer timer ;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private KeyReceiver keyReceiver;
    SharedUtil sharedUtil ;
    private boolean isMulti = false; // multi mode flag
    private final int MSG_inventory = 1 ;
    private final int MSG_inventory_TIME = 1001 ;

    private long lastCount = 0 ;//
    private long speed = 0 ;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_inventory:
                    long currentCount =  getReadCount(tagInfoList);
                    speed = currentCount - lastCount ;
                    if(speed >= 0){
                        lastCount = currentCount ;
                    }
                    break ;

                case MSG_inventory_TIME:
                    time++ ;
                     break ;
            }
        }
    };

    public InventoryIPSPFragment() {

    }

    private long getReadCount(List<TagInfo> tagInfoList) {
        long readCount = 0;
        for (int i = 0; i < tagInfoList.size(); i++) {
            readCount += tagInfoList.get(i).getCount();
        }
        return readCount;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
        viewModel.getCurrentLocation().observe(getViewLifecycleOwner(), location -> {
            tvLat.setText(String.valueOf(location.getLatitude()));
            tvLon.setText(String.valueOf(location.getLongitude()));
        });
        viewModel.getCurrentTag().observe(getViewLifecycleOwner(), tagData -> {
            tvAfid.setText(tagData.getAfid());
            tvTid.setText(tagData.getTid());
            stopInventory();
            if (Objects.equals(tagData.getAfid(), "0")){
                Drawable unknownIcon = ContextCompat.getDrawable(getContext(), R.drawable.indeterminate);
                imageView.setImageDrawable(unknownIcon);
                btnCatalog.setEnabled(false);
                btnLocation.setEnabled(false);
            } else {
                Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_check);
                imageView.setImageDrawable(icon);
                btnCatalog.setEnabled(true);
                btnLocation.setEnabled(true);
            }
        });
        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            tvName.setText(item.getFarmCode());
        });
        viewModel.getSelectedLocation().observe(getViewLifecycleOwner(), location -> {
            tvPiscina.setText(location.getPiscina());
        });
        btnLocation.setOnClickListener( v -> {
            findNavController(this).navigate(R.id.action_inventoryIPSPFragment_to_locationFragment);
        });
        btnCatalog.setOnClickListener( v -> {
            findNavController(this).navigate(R.id.action_inventoryIPSPFragment_to_catalogFragment);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("pang", "onResume()");
        if (mainActivity.mUhfrManager != null) {
            mainActivity.mUhfrManager.setCancleInventoryFilter();
        }
        registerKeyCodeReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("pang", "onPause()");
        stopInventory();
        mainActivity.unregisterReceiver(keyReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Runnable inventoryThread = new Runnable() {
        @Override
        public void run() {
            LogUtil.e("inventoryThread is running");
            // Reset variables before scanning
            List<Reader.TAGINFO> listTag = null;
            btnCatalog.setEnabled(false);
            btnLocation.setEnabled(false);

            Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_scan);
            imageView.setImageDrawable(icon);
            //6C
            //listTag = mainActivity.mUhfrManager.tagInventoryRealTime();
            listTag = mainActivity.mUhfrManager.tagEpcTidInventoryByTimer((short) 50) ;
            //listTag = mainActivity.mUhfrManager.tagInventoryByTimer((short) 50); ;
            //mainActivity.mUhfrManager.asyncStopReading();
            //mainActivity.mUhfrManager.asyncStartReading();

            if (listTag != null && !listTag.isEmpty()) {
                LogUtil.e("inventory listTag size = " + listTag.size());
                UtilSound.play(1,0);
                tagInfoMap.clear();
                for (Reader.TAGINFO taginfo : listTag) {
                    Map<String, TagInfo> infoMap = pooled6cData(taginfo);
                    tagInfoList.clear();
                    tagInfoList.addAll(infoMap.values());
                    //mainActivity.listEPC.clear();
                    //mainActivity.listEPC.addAll(infoMap.keySet());
                    viewModel.updateScanning(tagInfoList);
                }
                handler.sendEmptyMessage(MSG_inventory);
            }else{
                speed = 0 ;
            }
            handler.postDelayed(inventoryThread, 0) ;
        }
    } ;

    public Map<String, TagInfo> pooled6cData(Reader.TAGINFO info) {

        String epcAndTid = Tools.Bytes2HexString(info.EpcId, info.EpcId.length);
        Log.i("Inv", "[pooled6cData] tag epc: " + epcAndTid);

        if(info.EmbededData!=null) {
            epcAndTid = Tools.Bytes2HexString(info.EmbededData, info.EmbededData.length);
            Log.i("Inv", "[pooled6cData] tag tid: " + epcAndTid);
            if (TextUtils.isEmpty(epcAndTid)) {
                return tagInfoMap;
            }
        }else{
            Log.i("Inv", "[pooled6cData] drop null tid tag");
            return tagInfoMap;
        }
        if (tagInfoMap.containsKey(epcAndTid)) {
            TagInfo tagInfo = tagInfoMap.get(epcAndTid);
            assert tagInfo != null;
            Long count = tagInfo.getCount();
            count++;
            tagInfo.setRssi(info.RSSI + "");
            tagInfo.setCount(count);
            tagInfo.setIsShowTid(true);
            if (info.EmbededData != null && info.EmbededDatalen > 0) {
                tagInfo.setTid(Tools.Bytes2HexString(info.EmbededData, info.EmbededDatalen));
            }
            tagInfoMap.put(epcAndTid, tagInfo);
        } else {
            TagInfo tag = new TagInfo();
            tag.setIndex(index);
            tag.setType("6C");
            tag.setEpc(Tools.Bytes2HexString(info.EpcId, info.EpcId.length));
            tag.setCount(1L);
            tag.setIsShowTid(true);
            if (info.EmbededData != null && info.EmbededDatalen > 0) {
                tag.setTid(Tools.Bytes2HexString(info.EmbededData, info.EmbededDatalen));
            }
            tag.setRssi(info.RSSI + "");
            tagInfoMap.put(epcAndTid, tag);
            index++;
        }
        return tagInfoMap;
    }



    @OnClick(R.id.button_inventory)
    public void inventory() {
        //操作之前判定模块是否正常初始化
        if(mainActivity.mUhfrManager == null){
            showToast(R.string.communication_timeout);
            return ;
        }
        if (!isReader) {
            inventoryEPC();
        }else{
            stopInventory() ;
        }
    }

    private void inventoryEPC() {
        isReader = true ;
        //btnInventory.setText(R.string.stop_inventory);
        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow);
        btnInventory.setImageDrawable(icon);
        showToast(R.string.start_inventory);
        if(mainActivity.mUhfrManager.getGen2session()!=3){
            mainActivity.mUhfrManager.setGen2session(isMulti);
        }
        if (isMulti) {
            mainActivity.mUhfrManager.asyncStartReading() ;
        }
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(MSG_inventory_TIME);
                }
            }, 1000, 1000);
        }

        //启动盘存线程
        handler.postDelayed(inventoryThread, 0);
    }


    private void stopInventory()  {
        if (mainActivity.isConnectUHF) {
            if(isReader){
                handler.removeCallbacks(inventoryThread);
                isReader = false ;
                if (timer != null) {
                    timer.cancel();
                    timer = null ;
                }
//                btnInventory.setText(R.string.start_inventory);
                Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_barcode);
                btnInventory.setImageDrawable(icon);
            }
        } else {
            showToast(R.string.communication_timeout);
        }
        isReader = false ;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_ipsp, container, false) ;
        ButterKnife.bind(this, view);
        LogUtil.e("onCreateView()");
        sharedUtil = new SharedUtil(mainActivity);
        UtilSound.initSoundPool(mainActivity);

        return view;
    }


    private void registerKeyCodeReceiver() {
        keyReceiver = new KeyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.rfid.FUN_KEY");
        filter.addAction("android.intent.action.FUN_KEY");
        mainActivity.registerReceiver(keyReceiver, filter);
    }

    //
    private class KeyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int keyCode = intent.getIntExtra("keyCode", 0);
            LogUtil.e("keyCode = " + keyCode);
            if (keyCode == 0) {
                keyCode = intent.getIntExtra("keycode", 0);
            }
            boolean keyDown = intent.getBooleanExtra("keydown", false);
            if (keyDown) {
//                ToastUtils.showText("KeyReceiver:keyCode = down" + keyCode);
            } else {
//                ToastUtils.showText("KeyReceiver:keyCode = up" + keyCode);
                switch (keyCode) {
                    case KeyEvent.KEYCODE_F1:
                    case KeyEvent.KEYCODE_F2:
                    case KeyEvent.KEYCODE_F5:
                        break;
                    case KeyEvent.KEYCODE_F3://C510x
                    case KeyEvent.KEYCODE_F4://6100
                    case KeyEvent.KEYCODE_F7://H3100
                        inventory();
                        break;
                }
            }

        }

    }

}