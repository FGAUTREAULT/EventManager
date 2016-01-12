package com.gautreault.eventmanager.people;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutos.perso.myapplication.R;


/**
 * Created by gautreault on 02/11/2015.
 */
public class PeopleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.temp_layout, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView view = (TextView) getView().findViewById(R.id.tempLabel);
        view.setText("People");
    }

}
