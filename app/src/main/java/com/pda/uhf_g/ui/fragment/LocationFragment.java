package com.pda.uhf_g.ui.fragment;

import static com.pda.uhf_g.ui.base.NavHostFragment.findNavController;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pda.uhf_g.MainActivity;
import com.pda.uhf_g.R;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.entities.PondEntity;
import com.pda.uhf_g.ui.adapter.SpinnerAdapter;
import com.pda.uhf_g.ui.base.BaseFragment;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;
import com.pda.uhf_g.util.LogUtil;
import com.pda.uhf_g.util.SharedUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.spinner_mega_zona)
    Spinner spinnerMegazone;

    @BindView(R.id.spinner_zona)
    Spinner spinnerZone;
    @BindView(R.id.spinner_sector)
    Spinner spinnerSector;

    @BindView(R.id.spinner_piscina)
    Spinner spinnerPiscina;

    @BindView(R.id.button_save)
    FloatingActionButton btnSave;

    private InventoryViewModel viewModel;

    private MainActivity mainActivity ;
    SharedUtil sharedUtil ;

    List<PondEntity> ponds = new ArrayList<PondEntity>();

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

        ponds = getPonds();

        List<PondsDao.MegaZoneList> pondLists = getStubMegazones();
        List<PondsDao.PondList> megazones = new ArrayList<>();
        for (PondsDao.MegaZoneList pondList : pondLists) {
                megazones.add((PondsDao.PondList) pondList);
        }

        SpinnerAdapter adapter = new SpinnerAdapter(
                getContext(),
                megazones
        );

        //spinnerPiscina.setAdapter(adapter);

        spinnerMegazone.setAdapter(adapter);
        spinnerZone.setAdapter(adapter);
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

        spinnerMegazone.setOnItemSelectedListener(this);
        spinnerZone.setOnItemSelectedListener(this);
        spinnerSector.setOnItemSelectedListener(this);
        spinnerPiscina.setOnItemSelectedListener(this);

        btnSave.setOnClickListener( v -> {
            viewModel.saveToDatabase();
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        Object selectedItem = adapterView.getItemAtPosition(pos);
        if (selectedItem instanceof PondsDao.MegaZoneList){
            PondsDao.MegaZoneList item = (PondsDao.MegaZoneList) selectedItem;
            viewModel.setSelectedLocation(item.megazone, "", "", "");
            Log.d("SpinnerAdapter", item.megazone);
        } else if (selectedItem instanceof PondsDao.ZoneList){
            PondsDao.ZoneList item = (PondsDao.ZoneList) selectedItem;
            viewModel.setSelectedLocation("", "", "", "");
            Log.d("SpinnerAdapter", item.zone);

        } else if (selectedItem instanceof PondsDao.SectorList){
            PondsDao.SectorList item = (PondsDao.SectorList) selectedItem;
            viewModel.setSelectedLocation("", "", "", "");
            Log.d("SpinnerAdapter", item.sector);


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getMegazones(){

    }
    public void getZones(){

    }
    public void getSectors(){

    }
    public List<PondEntity> getPonds(){
        List<PondEntity> ponds;
        ponds = getStubPonds();
//        ponds = getPondsFromDB();
        return ponds;
    }

    public List<PondEntity> getStubPonds(){
        List<PondEntity> itemsList = new ArrayList<>();
        PondEntity pondDB = new PondEntity(
                "U001",
                "ZONA",
                "M001",
                "MEGAZONA",
                "Z001",
                "SECTOR",
                "S001",
                "PISCINA");
        itemsList.add(pondDB);
        return itemsList;
    }

    public List<PondsDao.MegaZoneList> getStubMegazones(){
        List<PondsDao.MegaZoneList> itemsList = new ArrayList<>();
        PondsDao.MegaZoneList m1 = new PondsDao.MegaZoneList("MEGAZONA1", "M001");
        PondsDao.MegaZoneList m2 = new PondsDao.MegaZoneList("MEGAZONA2", "M002");
        itemsList.add(m1);
        itemsList.add(m2);
        return itemsList;
    }


}