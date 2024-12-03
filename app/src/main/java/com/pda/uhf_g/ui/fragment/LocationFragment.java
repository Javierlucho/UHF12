package com.pda.uhf_g.ui.fragment;

import static com.pda.uhf_g.ui.base.NavHostFragment.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pda.uhf_g.MainActivity;
import com.pda.uhf_g.R;
import com.pda.uhf_g.ui.base.BaseFragment;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;
import com.pda.uhf_g.util.LogUtil;
import com.pda.uhf_g.util.SharedUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.spinner_piscina)
    Spinner spinnerPiscina;

    @BindView(R.id.button_save)
    FloatingActionButton btnSave;

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

        btnSave.setOnClickListener( v -> {
            findNavController(this).navigate(R.id.nav_inventory_ipsp);
        });

        List<String> itemsList = new ArrayList<>();
        itemsList.add("SANTAMONICA1");
        itemsList.add("SANTAMONICA2");
        itemsList.add("SANTAMONICA3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                itemsList
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPiscina.setAdapter(adapter);
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

        spinnerPiscina.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        Object selectedItem = adapterView.getItemAtPosition(pos);
        String selectedString = (String) selectedItem;
        Log.d("pang", selectedString);
        viewModel.setSelectedLocation("", "", "", selectedString);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}