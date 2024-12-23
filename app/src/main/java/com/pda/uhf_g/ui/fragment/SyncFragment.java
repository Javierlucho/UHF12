package com.pda.uhf_g.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pda.uhf_g.MainActivity;
import com.pda.uhf_g.R;
import com.pda.uhf_g.ui.base.BaseFragment;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;
import com.pda.uhf_g.util.SharedUtil;


import butterknife.BindView;
import butterknife.ButterKnife;

public class SyncFragment extends BaseFragment {

    private InventoryViewModel viewModel;
    @BindView(R.id.push_button)
    FloatingActionButton btnPush;

    @BindView(R.id.pull_button)
    FloatingActionButton btnPull;

    @BindView(R.id.categoria_status)
    TextView categoriaLabel;

    @BindView(R.id.items_status)
    TextView itemsLabel;

    @BindView(R.id.posicionamiento_status)
    TextView posicionamientoLabel;

    @BindView(R.id.ponds_status)
    TextView pondsLabel;


    private MainActivity mainActivity ;
    SharedUtil sharedUtil ;

    public SyncFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnPush.setOnClickListener( v -> {
            viewModel.pushToServer();
        });

        btnPull.setOnClickListener( v -> {
            viewModel.pullData();
        });

        viewModel.getDownloadedCatalog().observe(getViewLifecycleOwner(), downloaded -> {
            updateIcon(downloaded, categoriaLabel);
        });

        viewModel.getDownloadedItemsIPSP().observe(getViewLifecycleOwner(), downloaded -> {
            updateIcon(downloaded, itemsLabel);
        });

        viewModel.getDownloadedPosicionamiento().observe(getViewLifecycleOwner(), downloaded -> {
            updateIcon(downloaded, posicionamientoLabel);
        });

        viewModel.getDownloadedPonds().observe(getViewLifecycleOwner(), downloaded -> {
            updateIcon(downloaded, pondsLabel);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainActivity.mUhfrManager != null) {
            mainActivity.mUhfrManager.setCancleInventoryFilter();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_sync, container, false);
        ButterKnife.bind(this, view);

        viewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);

        sharedUtil = new SharedUtil(mainActivity);

        return view;
    }

    public void updateIcon(Boolean downloaded, TextView textView){
        if (downloaded){
            setOkDrawable(textView);
        } else {
            setNotOkDrawable(textView);
        }
    }
    public void setOkDrawable(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
    }
    public void setNotOkDrawable(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sync, 0, 0, 0);
    }
}