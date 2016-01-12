package com.gautreault.eventmanager.groups;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutos.perso.myapplication.R;

/**
 * Created by gautreault on 04/11/2015.
 */
public class CreateGroupActivity extends Activity {

    public static final String NEW_GROUP_NAME = "New Group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_namefragment);

        final EditText editText = (EditText) findViewById(R.id.editName);
        editText.requestFocus();
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    validateInputName();
                    return true;
                }
                return false;
            }
        });

        Button saveButton = (Button) findViewById(R.id.editButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputName();
            }
        });

        //Display back button
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_validate, menu);
        return true;
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
            case R.id.menu_validate:
                validateInputName();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void validateInputName() {
        String name = ((EditText) findViewById(R.id.editName)).getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.parameterMandatory), Toast.LENGTH_LONG).show();
        } else {
            Intent result = new Intent();
            result.putExtra(NEW_GROUP_NAME, name);
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }
}
