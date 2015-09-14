package com.example.android.event;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NotaActivity extends AppCompatActivity {

    private int mToolbarColor;
    private int mStatusBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        mToolbarColor = ((Constants) getApplication()).getToolbarColor();
        mStatusBarColor = ((Constants) getApplication()).getStatusBarColor();

        if (Build.VERSION.SDK_INT >= 21) {
            // top status bar
            getWindow().setStatusBarColor(this.getResources().getColor(mStatusBarColor));

            // bottom navigation bar
            getWindow().setNavigationBarColor(this.getResources().getColor(mStatusBarColor));
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(mToolbarColor));
        mToolbar.setTitle("");
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("nota detail");

        setSupportActionBar(mToolbar);

    }
}
