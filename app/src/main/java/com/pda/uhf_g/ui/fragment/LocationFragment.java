package com.pda.uhf_g.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.pda.uhf_g.MainActivity;
import com.pda.uhf_g.R;
import com.pda.uhf_g.ui.base.BaseFragment;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;
import com.pda.uhf_g.util.LogUtil;
import com.pda.uhf_g.util.SharedUtil;
import com.pda.uhf_g.util.UtilSound;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment extends BaseFragment {

    private InventoryViewModel viewModel;

    private MainActivity mainActivity ;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    SharedUtil sharedUtil ;




    public LocationFragment() {

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

        viewModel.getCurrentTag().observe(getViewLifecycleOwner(), tagData -> {

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("pang", "onResume()");
        if (mainActivity.mUhfrManager != null) {
            mainActivity.mUhfrManager.setCancleInventoryFilter();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("pang", "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);
        LogUtil.e("onCreateView()");
        sharedUtil = new SharedUtil(mainActivity);
        return view;
    }

}