package com.example.android.event;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A placeholder fragment containing a simple view.
 */
public class EventActivityFragment extends Fragment {

    private String LOG_TAG = this.getClass().getSimpleName();

    private Toolbar toolbar;
    private PausableChronometer mChronometer;
    private long mStartTime;
    private long mEndTime;

    public EventActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

//        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitle("ongoing");
        mChronometer = (PausableChronometer) rootView.findViewById(R.id.chronometer);

        final Button buttonStart = (Button) rootView.findViewById(R.id.button_start);
        final Button buttonPause = (Button) rootView.findViewById(R.id.button_pause);
        final Button buttonResume = (Button) rootView.findViewById(R.id.button_resume);
        final Button buttonStop = (Button) rootView.findViewById(R.id.button_stop);

        buttonPause.setVisibility(View.GONE);
        buttonResume.setVisibility(View.GONE);
        buttonStop.setVisibility(View.GONE);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mChronometer.start();
                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                Log.d(LOG_TAG, "start time: " + System.currentTimeMillis());
                System.out.println(getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm:ss.SSS"));
                buttonStart.setVisibility(View.GONE);
                buttonPause.setVisibility(View.VISIBLE);
            }
        });


        buttonPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mChronometer.stop();
                buttonPause.setVisibility(View.GONE);
                buttonResume.setVisibility(View.VISIBLE);
                buttonStop.setVisibility(View.VISIBLE);
            }
        });


        buttonResume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mChronometer.start();
                buttonPause.setVisibility(View.VISIBLE);
                buttonResume.setVisibility(View.GONE);
                buttonStop.setVisibility(View.GONE);
            }
        });


        buttonStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mChronometer.stop();
                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                Log.d(LOG_TAG, "end time: " + System.currentTimeMillis());
                System.out.println(getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm:ss.SSS"));

                buttonStart.setVisibility(View.GONE);
                buttonPause.setVisibility(View.GONE);
                buttonResume.setVisibility(View.GONE);
                buttonStop.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
