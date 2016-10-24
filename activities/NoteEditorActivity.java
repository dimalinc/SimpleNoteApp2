package com.okason.simplenotepad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.okason.simplenotepad.R;
import com.okason.simplenotepad.fragments.NotePlainEditorFragment;

public class NoteEditorActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);


        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //remove this line in the MainActivity.java

        if (savedInstanceState == null){
            Bundle args = getIntent().getExtras();
            if (args != null) {
                Log.d("myLogs", "in NoteEditorActivity.onCreate fragment bundle args != null ");

                if (args.containsKey("id")) {
                    Log.d("myLogs", "in NoteEditorActivity.onCreate fragment bundle  args.containsKey(\"id\") ");

                    long id = args.getLong("id", 0);

                    Log.d("myLogs", "in NoteEditorActivity.onCreate fragment bundle args id = " + id);

                    if (id > 0) {
                        openFragment(NotePlainEditorFragment.newInstance(id), "Editor");
                    }
                }
            } else {
                Log.d("myLogs","no fragment bundle args in NoteEditorActivity.onCreate");
                openFragment(NotePlainEditorFragment.newInstance(0), "Editor");
            }






         }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openFragment(final Fragment fragment, String title){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(title);
    }
}
