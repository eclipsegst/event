package com.example.android.event;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.location.Location;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;


/**
 * A placeholder fragment containing a simple view.
 */
public class EventActivityFragment extends Fragment implements
        FloatingActionButton.OnCheckedChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private String LOG_TAG = this.getClass().getSimpleName();

    private Toolbar mToolbar;
    private PausableChronometer mChronometer;
    private long mStartTime;
    private long mEndTime;

    private FloatingActionButton mFabStart;
    private FloatingActionButton mFabPause;
    private FloatingActionButton mFabEnd;

    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";

    // Nested class
//    private AddressResultReceiver mResultReceiver;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected TextView mLatTextView;
    protected TextView mLonTextView;
    protected TextView mAddressTextview;

    Button mFetchAddressButtons;

    public EventActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_event, container, false);

//        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mChronometer = (PausableChronometer) rootView.findViewById(R.id.chronometer);

        mFabStart = (FloatingActionButton) rootView.findViewById(R.id.fab_start);
        mFabPause = (FloatingActionButton) rootView.findViewById(R.id.fab_pause);
        mFabEnd = (FloatingActionButton) rootView.findViewById(R.id.fab_end);
//        fab1.setBackgroundColor(getResources().getColor(TOOLBAR_COLOR.get(randomNumber)));

        mFabStart.setOnCheckedChangeListener(this);
        mFabPause.setOnCheckedChangeListener(this);
        mFabEnd.setOnCheckedChangeListener(this);

        mFabStart.setVisibility(View.GONE);
        mFabPause.setVisibility(View.VISIBLE);
        mFabEnd.setVisibility(View.GONE);


        // Start the chronometer when launching the activity
        mChronometer.start();
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        Log.d(LOG_TAG, "start time: " + System.currentTimeMillis());
        System.out.println(getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm:ss.SSS"));

        mLatTextView = (TextView) rootView.findViewById(R.id.latitude_text);
        mLonTextView = (TextView) rootView.findViewById(R.id.longitude_text);

        Log.d(LOG_TAG, "on create");
        buildGoogleApiClient();

        return rootView;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public void onDisconnected() {
        Log.i(LOG_TAG, "Disconnected");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(LOG_TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(LOG_TAG, "onConnected");
        if (mLastLocation != null) {

            mLatTextView.setText("latitude: " + String.valueOf(mLastLocation.getLatitude()));
            mLonTextView.setText("longitude: " + String.valueOf(mLastLocation.getLongitude()));
        } else {
            Log.d(LOG_TAG, "No location data");
        }

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mToolbar.setTitle("ongoing");
    }

    @Override
    public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {
        // When a FAB is toggled, log the action.
        switch (fabView.getId()){
            case R.id.fab_start:
                Log.d(LOG_TAG, String.format("FAB start was %s.", isChecked ? "checked" : "unchecked"));

                mFabStart.setVisibility(View.GONE);
                mFabPause.setVisibility(View.VISIBLE);
                mFabEnd.setVisibility(View.GONE);

                mChronometer.start();


                break;
            case R.id.fab_pause:
                Log.d(LOG_TAG, String.format("FAB pause was %s.", isChecked ? "checked" : "unchecked"));

                mFabStart.setVisibility(View.VISIBLE);
                mFabPause.setVisibility(View.GONE);
                mFabEnd.setVisibility(View.VISIBLE);

                mChronometer.stop();

                break;
            case R.id.fab_end:
                Log.d(LOG_TAG, String.format("FAB end was %s.", isChecked ? "checked" : "unchecked"));
//                Intent intent = new Intent(this, EventActivity.class);
//                startActivity(intent);
                mChronometer.stop();
                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                Log.d(LOG_TAG, "end time: " + System.currentTimeMillis());
                System.out.println(getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm:ss.SSS"));
                break;

            default:
                break;
        }
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
