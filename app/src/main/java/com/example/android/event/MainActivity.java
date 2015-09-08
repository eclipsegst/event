package com.example.android.event;

import android.content.Intent;
import android.os.Build;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    Button btnStart, btnPause, btnEnd;
    TextView textCounter;
    private Chronometer mChronometer;

    String startDatetime;
    String endDatetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setTheme(R.style.MyRandomTheme);

        // Return 0 to 17 random number

        int randomNumber = randInt(0, 17);

        // Set status bar and navigation bar color
        if (Build.VERSION.SDK_INT >= 21) {
            // Top status bar
            getWindow().setStatusBarColor(getResources().getColor(STATUS_BAR_COLOR.get(randomNumber)));

            // Bottom navigation bar
            getWindow().setNavigationBarColor(getResources().getColor(STATUS_BAR_COLOR.get(randomNumber)));
        }

        setContentView(R.layout.activity_main);

        btnStart = (Button)findViewById(R.id.start);
        btnPause = (Button) findViewById(R.id.pause);
        btnEnd = (Button) findViewById(R.id.end);
        textCounter = (TextView)findViewById(R.id.counter);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

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

        btnEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(TOOLBAR_COLOR.get(randomNumber)));
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        mTitle.setText("event");
//        toolbar.setTitle("");
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

    private static final Map<Integer, Integer> TOOLBAR_COLOR;

    static {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        map.put(0, R.color.red);
        map.put(1, R.color.pink);
        map.put(2, R.color.purple);
        map.put(3, R.color.indigo);
        map.put(4, R.color.blue);
        map.put(5, R.color.light_blue);
        map.put(6, R.color.cyan);
        map.put(7, R.color.teal);
        map.put(8, R.color.green);
        map.put(9, R.color.light_green);
        map.put(10, R.color.lime);
        map.put(11, R.color.yellow);
        map.put(12, R.color.amber);
        map.put(13, R.color.orange);
        map.put(14, R.color.deep_orange);
        map.put(15, R.color.brown);
        map.put(16, R.color.grey);
        map.put(17, R.color.blue_grey);
        TOOLBAR_COLOR = Collections.unmodifiableMap(map);
    }

    private static final Map<Integer, Integer> STATUS_BAR_COLOR;

    static {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        map.put(0, R.color.dark_red);
        map.put(1, R.color.dark_pink);
        map.put(2, R.color.dark_purple);
        map.put(3, R.color.dark_indigo);
        map.put(4, R.color.dark_blue);
        map.put(5, R.color.dark_light_blue);
        map.put(6, R.color.dark_cyan);
        map.put(7, R.color.dark_teal);
        map.put(8, R.color.dark_green);
        map.put(9, R.color.dark_light_green);
        map.put(10, R.color.dark_lime);
        map.put(11, R.color.dark_yellow);
        map.put(12, R.color.dark_amber);
        map.put(13, R.color.dark_orange);
        map.put(14, R.color.dark_deep_orange);
        map.put(15, R.color.dark_brown);
        map.put(16, R.color.dark_grey);
        map.put(17, R.color.dark_blue_grey);
        STATUS_BAR_COLOR = Collections.unmodifiableMap(map);
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
