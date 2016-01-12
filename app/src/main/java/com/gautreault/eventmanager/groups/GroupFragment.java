package com.gautreault.eventmanager.groups;

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
public class GroupFragment extends Fragment {

    private String groupName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.temp_layout, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.tempLabel);
        //TODO Parcelable and get the group
        groupName = getArguments().getString(GroupListFragment.GROUP_NAME);
        textView.setText(groupName);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Change action bar name
        //getActivity().getActionBar().setTitle(groupName);
    }
}
