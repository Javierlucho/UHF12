package com.pda.uhf_g.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pda.uhf_g.data.local.dao.PondsDao;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<PondsDao.PondList> {

    private Context context;
    private List<PondsDao.PondList> items;

    public SpinnerAdapter(Context context, List<PondsDao.PondList> items) {
        super(context, android.R.layout.simple_spinner_item);
        this.context = context;
        this.items = items;

        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        assert view != null;
        TextView label = view.findViewById(android.R.id.text1);


        Log.d("SpinnerAdapter", "getView: " + items.get(position));
        if (items.get(position) instanceof PondsDao.MegaZoneList){
            PondsDao.MegaZoneList item = (PondsDao.MegaZoneList) items.get(position);
            label.setText(item.megazone);
        } else if (items.get(position) instanceof PondsDao.ZoneList){
            PondsDao.ZoneList item = (PondsDao.ZoneList) items.get(position);
            label.setText(item.zone);
        } else if (items.get(position) instanceof PondsDao.SectorList){
            PondsDao.SectorList item = (PondsDao.SectorList) items.get(position);
            label.setText(item.sector);
        } else {
            Log.d("SpinnerAdapter", "Not found");
        }
        return view;
    }


}