package com.pda.uhf_g.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pda.serialport.Tools;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.handheld.uhfr.Reader;
import com.pda.uhf_g.MainActivity;
import com.pda.uhf_g.R;
import com.pda.uhf_g.adapter.TempTagListViewAdapter;
import com.pda.uhf_g.ui.base.BaseFragment;
import com.pda.uhf_g.util.LogUtil;
import com.pda.uhf_g.util.UtilSound;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Temperature TAG fragment.
 */
public class TemperatureTagFragment extends BaseFragment {


    @BindView(R.id.spinner_manufactorer)
    Spinner spinnerTagManufactorer ;
    @BindView(R.id.listVew_epc)
    ListView listView ;
    @BindView(R.id.button_read)
    Button btnRead ;
    @BindView(R.id.button_clean)
    Button btnClean ;

    private MainActivity mainActivity ;

    private Handler mHandler = new Handler();

    private Map<String, Reader.TEMPTAGINFO> tagMap = new LinkedHashMap<>();
    private List<Reader.TEMPTAGINFO> listTag = new ArrayList<>();
    private boolean isRead = false ;
    private TempTagListViewAdapter adapter ;
    private int tagType = 0;
    private int index = 1 ;

    private KeyReceiver keyReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        isRead = false ;
        btnRead.setText(R.string.read);
        mHandler.removeCallbacks(readThread);
        mainActivity.unregisterReceiver(keyReceiver);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainActivity.mUhfrManager != null) {
            mainActivity.mUhfrManager.setCancleInventoryFilter();
        }
        registerKeyCodeReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperature_tag, container, false) ;
        ButterKnife.bind(this, view);
        initView() ;
        return view;
    }

    private void initView() {
        adapter = new TempTagListViewAdapter(mainActivity, listTag) ;
        listView.setAdapter(adapter);
        spinnerTagManufactorer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (tagType != position) {
                    onClean();
                }
                tagType = position ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Runnable readThread = new Runnable() {
        @Override
        public void run() {
            List<Reader.TEMPTAGINFO> list = null ;
            if (tagType == 0) {
                //Yuehe
                list = mainActivity.mUhfrManager.getYueheTagTemperature(null) ;
            }else if(tagType == 1){
                //Yilian
                list = mainActivity.mUhfrManager.getYilianTagTemperature() ;
            }
            if (list != null && !list.isEmpty()) {
                UtilSound.voiceTips(MainActivity.type, list.size(),false);
                for (Reader.TEMPTAGINFO info : list) {
                    Map<String, Reader.TEMPTAGINFO> map = pooledTagData(info);
                    listTag.clear();
                    listTag.addAll(map.values());
                    Log.e("pang", "epc:" + Tools.Bytes2HexString(info.EpcId, info.Epclen) + ", temp = " + info.Temperature);
                }
                adapter.notifyDataSetChanged();
            }

            mHandler.post(readThread);
        }
    } ;


    public Map<String, Reader.TEMPTAGINFO> pooledTagData(Reader.TEMPTAGINFO info) {
        String epc = Tools.Bytes2HexString(info.EpcId, info.Epclen) ;
        if (tagMap.containsKey(epc)) {
            Reader.TEMPTAGINFO tag = tagMap.get(epc);
            int count = tag.count ;
            count++ ;
            tag.count = count ;
            tag.Temperature = info.Temperature ;
            tagMap.put(epc, tag);

        }else{
            info.index = index ;
            info.count = 1 ;
            index++ ;
            tagMap.put(epc, info);
        }

        return tagMap ;
    }



    @OnClick(R.id.button_read)
    public void onReadTag() {
        if (!isRead) {
            isRead = true ;
            btnRead.setText(R.string.stop_read);
            mHandler.postDelayed(readThread, 0);
        }else{
            isRead = false ;
            btnRead.setText(R.string.read);
            mHandler.removeCallbacks(readThread);
        }
    }

    @OnClick(R.id.button_clean)
    public void onClean() {
        listTag.clear();
        tagMap.clear();
        index = 1 ;
        adapter.notifyDataSetChanged();
    }


    private void registerKeyCodeReceiver() {
        keyReceiver = new KeyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.rfid.FUN_KEY");
        filter.addAction("android.intent.action.FUN_KEY");
        mainActivity.registerReceiver(keyReceiver, filter);
    }

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
                        break;
                    case KeyEvent.KEYCODE_F2:
                        break;
                    case KeyEvent.KEYCODE_F5:
                        break;
                    case KeyEvent.KEYCODE_F3://C510x
                    case KeyEvent.KEYCODE_F4://6100
                    case KeyEvent.KEYCODE_F7://H3100
//                        invenroty();
                        onReadTag() ;
                        break;
                }
            }
        }

    }

}