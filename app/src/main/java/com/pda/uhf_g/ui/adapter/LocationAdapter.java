package com.pda.uhf_g.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pda.uhf_g.data.local.dao.PondsDao;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<PondsDao.PondList> {

    private Context mContext;
    private List<PondsDao.PondList> locationList;

    public LocationAdapter(@NonNull Context context, List<PondsDao.PondList> list) {
        super(context, 0 , list);
        mContext = context;
        locationList = list;

        // Specify the layout to use when the list of choices appears.
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(
                    android.R.layout.simple_spinner_item, parent ,false);

        PondsDao.PondList currentLocation = locationList.get(position);

        TextView name = listItem.findViewById(android.R.id.text1);
        name.setText(currentLocation.getVisualName());

        return listItem;
    }


}