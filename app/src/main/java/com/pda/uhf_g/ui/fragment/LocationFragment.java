package com.pda.uhf_g.ui.fragment;

import static com.pda.uhf_g.ui.base.NavHostFragment.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pda.uhf_g.MainActivity;
import com.pda.uhf_g.R;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.entities.Location;
import com.pda.uhf_g.data.local.entities.PondEntity;
import com.pda.uhf_g.ui.adapter.LocationAdapter;
import com.pda.uhf_g.ui.base.BaseFragment;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;
import com.pda.uhf_g.util.LogUtil;
import com.pda.uhf_g.util.SharedUtil;

import java.util.ArrayList;
import java.util.List;

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

    private MainActivity mainActivity;
    SharedUtil sharedUtil;

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

        btnSave.setOnClickListener(v -> {
            viewModel.saveToDatabase();
            findNavController(this).navigate(R.id.nav_inventory_ipsp);
        });

        // Load Data
        //ponds = getPonds();
        viewModel.getMegazonesFromDB();
        viewModel.getMegazones().observe(getViewLifecycleOwner(), megazones -> {
            LocationAdapter adapter = new LocationAdapter(
                    requireContext(),
                    transformToPondLists(megazones)
            );
            spinnerMegazone.setAdapter(adapter);
        });
        viewModel.getZones().observe(getViewLifecycleOwner(), zones -> {
            LocationAdapter adapter = new LocationAdapter(
                    requireContext(),
                    transformToPondLists(zones)
            );
            spinnerZone.setAdapter(adapter);
        });
        viewModel.getSectors().observe(getViewLifecycleOwner(), sectors -> {
            LocationAdapter adapter = new LocationAdapter(
                    requireContext(),
                    transformToPondLists(sectors)
            );
            spinnerSector.setAdapter(adapter);
        });
        viewModel.getPonds().observe(getViewLifecycleOwner(), ponds -> {
            LocationAdapter adapter = new LocationAdapter(
                    requireContext(),
                    transformToPondLists(ponds)
            );
            spinnerPiscina.setAdapter(adapter);
        });
    }

    private List<PondsDao.PondList> transformToPondLists(List listOfSubdivisions) {
        List<PondsDao.PondList> pondLists = new ArrayList<>();
        for(Object pond : listOfSubdivisions) {
            pondLists.add((PondsDao.PondList) pond);
        }
        return pondLists;
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


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        Object selectedItem = adapterView.getItemAtPosition(pos);
        Location selectedLocation = viewModel.getSelectedLocation().getValue();
        if (selectedItem instanceof PondsDao.MegaZoneList){
            PondsDao.MegaZoneList item = (PondsDao.MegaZoneList) selectedItem;
            viewModel.setSelectedLocation(
                    item.getID(), "",
                    "", "", "");
            Log.d("SpinnerAdapter", "Megazone: " + item.getVisualName());
            viewModel.getZonesFromDB(item.getID());

        } else if (selectedItem instanceof PondsDao.ZoneList){
            PondsDao.ZoneList item = (PondsDao.ZoneList) selectedItem;
            viewModel.setSelectedLocation(
                    selectedLocation.getMegaZonaID(), item.getID(),
                    "", "", "");
            Log.d("SpinnerAdapter", "Zone: " + item.getVisualName());
            viewModel.getSectorsFromDB(item.getID());

        } else if (selectedItem instanceof PondsDao.SectorList){
            PondsDao.SectorList item = (PondsDao.SectorList) selectedItem;
            viewModel.setSelectedLocation(
                    selectedLocation.getMegaZonaID(), selectedLocation.getZonaID(),
                    item.getID(), "", "");
            Log.d("SpinnerAdapter", "Sector: " + item.getVisualName());
            viewModel.getPondsFromDB(item.getID());

        } else if (selectedItem instanceof PondsDao.PondList){
            PondsDao.PondList item = (PondsDao.PondList) selectedItem;
            viewModel.setSelectedLocation(
                    selectedLocation.getMegaZonaID(), selectedLocation.getZonaID(),
                    selectedLocation.getSectorID(), item.getID(), item.getVisualName());
            Log.d("SpinnerAdapter", "Pond: " + item.getVisualName());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    public ArrayList<PondsDao.MegaZoneList> getStubMegazones(){
        ArrayList<PondsDao.MegaZoneList> itemsList = new ArrayList<>();
        PondsDao.MegaZoneList m1 = new PondsDao.MegaZoneList("MEGAZONA1", "M001");
        PondsDao.MegaZoneList m2 = new PondsDao.MegaZoneList("MEGAZONA2", "M002");
        itemsList.add(m1);
        itemsList.add(m2);
        return itemsList;
    }


}