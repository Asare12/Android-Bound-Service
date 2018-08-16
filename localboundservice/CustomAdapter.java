package com.example.asare.localboundservice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Asare on 01/03/2018.
 */

public class CustomAdapter extends ArrayAdapter<weatherList> {

    private static class ViewHolder{
        TextView description;
    }

    public CustomAdapter(@NonNull Context context, List<weatherList> weatherLists) {
        super(context,R.layout.list_item, weatherLists);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        weatherList weatherLists = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            viewHolder.description = (TextView)convertView.findViewById(R.id.weatherDescription);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.description.setText(weatherLists.getWeatherDesc());


        return convertView;
    }
}

