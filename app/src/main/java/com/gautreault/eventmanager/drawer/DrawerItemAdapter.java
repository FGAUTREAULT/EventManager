package com.gautreault.eventmanager.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.tutos.perso.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DrawerItemAdapter extends ArrayAdapter<DrawerItem> {

    private Context mContext;
    private int layoutResourceId;
    private List<DrawerItem> data = new ArrayList<>();

    public DrawerItemAdapter(Context mContext, int layoutResourceId, List<DrawerItem> data) {
 
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        View listItem = convertView;
 
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
 
        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
        
        DrawerItem folder = data.get(position);
 
        imageViewIcon.setImageResource(folder.getIcon());
        textViewName.setText(folder.getName());
        
        return listItem;
    }
 
}