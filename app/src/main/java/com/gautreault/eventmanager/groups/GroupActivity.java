package com.gautreault.eventmanager.groups;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tutos.perso.myapplication.R;

/**
 * Created by gautreault on 04/11/2015.
 */
public class GroupActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            GroupFragment fragment = new GroupFragment();
            fragment.setArguments(getIntent().getExtras());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.detail_fragment, fragment).commit();
        }

        //Display back button
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Navigate to parent activity. Add :
                //<meta-data android:name="android.support.PARENT_ACTIVITY"
                //android:value="com.gautreault.eventmanager.main.MainActivity"></meta-data>
                //NavUtils.navigateUpFromSameTask(this);

                //Back to previous screen
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
