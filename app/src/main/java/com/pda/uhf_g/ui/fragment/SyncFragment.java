package com.pda.uhf_g.ui.fragment;

import static com.pda.uhf_g.ui.base.NavHostFragment.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pda.uhf_g.MainActivity;
import com.pda.uhf_g.R;
import com.pda.uhf_g.ui.adapter.ListAdapter;
import com.pda.uhf_g.ui.base.BaseFragment;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;
import com.pda.uhf_g.util.LogUtil;
import com.pda.uhf_g.util.SharedUtil;


import butterknife.BindView;
import butterknife.ButterKnife;

public class SyncFragment extends BaseFragment {

    private InventoryViewModel viewModel;
    @BindView(R.id.push_button)
    Button btnPush;

    @BindView(R.id.pull_button)
    Button btnPull;
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_sync, container, false);
        ButterKnife.bind(this, view);

        viewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);

        LogUtil.e("onCreateView()");
        sharedUtil = new SharedUtil(mainActivity);

        return view;
    }

}