package com.example.android.event;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private int mToolbarColor;
    private int mStatusBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setTheme(R.style.MyRandomTheme);

        mToolbarColor = ((Constants) getApplication()).getToolbarColor();
        mStatusBarColor = ((Constants) getApplication()).getStatusBarColor();

        if (mToolbarColor == -1) {
            int randomNumber = utilities.randInt(0, ((Constants) getApplication()).TOOLBAR_COLOR.size());

            mToolbarColor = ((Constants) getApplication()).TOOLBAR_COLOR.get(randomNumber);
            mStatusBarColor = ((Constants) getApplication()).STATUS_BAR_COLOR.get(randomNumber);
            ((Constants) getApplication()).setToolbarColor(mToolbarColor);
            ((Constants) getApplication()).setStatusBarColor(mStatusBarColor);
        }

        // Set status bar and navigation bar color
        if (Build.VERSION.SDK_INT >= 21) {
            // Top status bar
            getWindow().setStatusBarColor(getResources().getColor(mStatusBarColor));

            // Bottom navigation bar
            getWindow().setNavigationBarColor(getResources().getColor(mStatusBarColor));
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(mToolbarColor));
        mToolbar.setTitle("");
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("now");
        setSupportActionBar(mToolbar);

        FloatingActionButton nFabNew = (FloatingActionButton) findViewById(R.id.fab_new);
        nFabNew.setBackgroundColor(getResources().getColor(mToolbarColor));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //i
        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
