package com.gautreault.eventmanager.groups;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gautreault.eventmanager.dao.group.GroupContentProvider;
import com.gautreault.eventmanager.dao.group.GroupDAO;
import com.tutos.perso.myapplication.R;

import static com.gautreault.eventmanager.dao.group.GroupContentProvider.CONTENT_URI;


/**
 * Created by gautreault on 02/11/2015.
 */
public class GroupListFragment extends ListFragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final int ADD_GROUP_REQUEST = 1;  // The request code

    public static final String GROUP_NAME = "GROUP NAME";

    private boolean actionModeNotRunning;
    private String[] items;
    private SimpleCursorAdapter adapter;

    private ActionMode mMode;
    private final ActionMode.Callback mActionMode = new ActionMode.Callback() {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    //Delete selected groups and close the mode
                    removeGroups();
                    // mMode need to be a field of the activity or fragment, you can't declare it
                    // and use it in the same callback
                    if (mMode != null) mMode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_itemactionmode_list, menu);
            actionModeNotRunning = false;
            adapter.setViewResource(android.R.layout.simple_list_item_multiple_choice);
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            setListAdapter(adapter);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionModeNotRunning = true;
            adapter.setViewResource(android.R.layout.simple_list_item_activated_1);
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            setListAdapter(adapter);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMode != null) mMode.finish();
        showDetails(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showDetails(boolean show) {
        if (getActivity().findViewById(R.id.content_frame_details) != null) {
            getActivity().findViewById(R.id.content_frame_details).setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Populate data
        getGroups();

        //Settings
        actionModeNotRunning = true;
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setHasOptionsMenu(true);

        //Listeners
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int arg2, long arg3) {
                if (actionModeNotRunning) {
                    mMode = getActivity().startActionMode(mActionMode);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setSelected(true);
                    return true;
                }
                return false;
            }
        });
        getListView().setOnItemClickListener(this);
        getListView().getEmptyView().setVisibility(ListView.GONE);

    }

    private void getGroups() {
        //items = getResources().getStringArray(R.array.list_examples);
        //ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, items);

        //A ne pas faire car requetes dans le thread graphique, ici pour test rapide
        //Cursor cursor = dao.getDb().rawQuery("SELECT ROWID _id,* FROM " + GroupDAO.TABLE_NAME, null);

        //GroupAdapter adapter = new GroupAdapter(cursor.getColumnNames(), getActivity().getBaseContext());

        String[] from = new String[]{GroupDAO.NAME};
        int[] to = new int[]{android.R.id.text1};

        //Open loader or get old one
        getLoaderManager().initLoader(0, null, this);

        adapter = new SimpleCursorAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_activated_1, null, from, to, 0);
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (actionModeNotRunning || getActivity().findViewById(R.id.content_frame_details) != null) {
            TextView textView = (TextView) view.findViewById(android.R.id.text1);

            Bundle args = new Bundle();
            args.putString(GroupListFragment.GROUP_NAME, textView.getText().toString());

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                if(getActivity().findViewById(R.id.content_frame_details).getVisibility() == View.GONE) showDetails(true);

                final GroupFragment groupFragment = new GroupFragment();
                groupFragment.setArguments(args);

                final FragmentTransaction ft = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.content_frame_details, groupFragment);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                Intent intent = new Intent(getActivity(), GroupActivity.class);
                intent.putExtras(args);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_actionbar_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                addGroup();
                return true;
            case R.id.menu_search:
                Toast.makeText(getActivity(), R.string.toast_search, Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

    public void addGroup() {

        Intent createGroup = new Intent(getActivity(), CreateGroupActivity.class);
        startActivityForResult(createGroup, ADD_GROUP_REQUEST);
    }

    public void removeGroups() {

        Uri uri = GroupContentProvider.CONTENT_URI;
        SparseBooleanArray selectedPositions = getListView().getCheckedItemPositions();
        boolean delete = false;

        for (int i = 0; i < selectedPositions.size(); i++) {
            if(selectedPositions.get(i)){
                String where = GroupDAO.KEY + " = " + adapter.getItemId(selectedPositions.keyAt(i));
                getActivity().getContentResolver().delete(uri, where, null);
                delete = true;
            }
        }

        showDetails(!delete);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == ADD_GROUP_REQUEST) {
            // Make sure the request was successful
            if (resultCode == getActivity().RESULT_OK) {
                String name = data.getStringExtra(CreateGroupActivity.NEW_GROUP_NAME);
                Uri uri = GroupContentProvider.CONTENT_URI;

                ContentValues values = new ContentValues();
                values.put(GroupDAO.NAME, name);

                getActivity().getContentResolver().insert(uri, values);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //For adapter to work, need _id in projection
        String[] projection = {GroupDAO.KEY, GroupDAO.NAME};
        CursorLoader cursorLoader = new CursorLoader(this.getContext(), CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Remove reference
        adapter.swapCursor(null);
    }
}
