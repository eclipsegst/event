package com.example.android.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.android.znote.R;


public class MainActivity extends AppCompatActivity {

    Button btnStart, btnPause, btnEnd;
    TextView textCounter;
    private Chronometer mChronometer;

    String startDatetime;
    String endDatetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button)findViewById(R.id.start);
        btnPause = (Button)findViewById(R.id.pause);
        btnEnd = (Button)findViewById(R.id.end);
        textCounter = (TextView)findViewById(R.id.counter);
        mChronometer = (Chronometer)findViewById(R.id.chronometer);

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

            }
        });

        btnPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
