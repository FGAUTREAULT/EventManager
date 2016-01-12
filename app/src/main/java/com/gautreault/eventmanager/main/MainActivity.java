package com.gautreault.eventmanager.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gautreault.eventmanager.drawer.DrawerItem;
import com.gautreault.eventmanager.drawer.DrawerItemAdapter;
import com.gautreault.eventmanager.events.EventFragment;
import com.gautreault.eventmanager.events.EventListFragment;
import com.gautreault.eventmanager.groups.GroupListFragment;
import com.gautreault.eventmanager.infos.InfosFragment;
import com.gautreault.eventmanager.people.PeopleListFragment;
import com.tutos.perso.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_mainactivity);

        mTitle = mDrawerTitle = getTitle();

        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.drawer_titles);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        buildDrawerList();

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.drawer_upcoming,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        //Display upcomming fragment
        if (savedInstanceState == null) {
            Fragment fragment = new EventFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void buildDrawerList() {

        List<DrawerItem> items = new ArrayList<DrawerItem>();
        items.add(new DrawerItem(R.drawable.drawer_upcoming, mNavigationDrawerItemTitles[0]));
        items.add(new DrawerItem(R.drawable.drawer_groups, mNavigationDrawerItemTitles[1]));
        items.add(new DrawerItem(R.drawable.drawer_events, mNavigationDrawerItemTitles[2]));
        items.add(new DrawerItem(R.drawable.drawer_people, mNavigationDrawerItemTitles[3]));
        items.add(new DrawerItem(R.drawable.drawer_infos, mNavigationDrawerItemTitles[4]));

        DrawerItemAdapter adapter = new DrawerItemAdapter(this, R.layout.drawer_list_item, items);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void selectItem(int position) {

        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new EventFragment();
                break;
            case 1:
                fragment = new GroupListFragment();
                break;
            case 2:
                fragment = new EventListFragment();
                break;
            case 3:
                fragment = new PeopleListFragment();
                break;
            case 4:
                fragment = new InfosFragment();
                break;

            default:
                fragment = new EventFragment();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }

    }
}
