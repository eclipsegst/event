package com.example.android.event;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.event.data.NotaContract;
import com.example.android.event.data.NotaDbHelper;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.zhaolongzhong.backend.momentApi.MomentApi;
import com.zhaolongzhong.backend.momentApi.model.Moment;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mPopupHeaderColor;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private ShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbarColor = ((Constants) getApplication()).getToolbarColor();
        mStatusBarColor = ((Constants) getApplication()).getStatusBarColor();
        mPopupHeaderColor = ((Constants) getApplication()).getPopupHeaderColor();

        Log.d(LOG_TAG, "mToolbarColor:" + mToolbarColor);
        Log.d(LOG_TAG, "mStatusBarColor:" + mStatusBarColor);
        Log.d(LOG_TAG, "mPopupHeaderColor:" + mPopupHeaderColor);

        if (mToolbarColor == -1 || mStatusBarColor == -1 || mPopupHeaderColor == -1) {
            int randomNumber = -1;
            try {
                randomNumber = utilities.randInt(0, ((Constants) getApplication()).TOOLBAR_COLOR.size() - 1);
            } catch (Exception e) {
                Log.e(LOG_TAG, "cannot generate random number");
            }

            if (randomNumber != -1) {
                Log.d(LOG_TAG, "randomNumber:" + randomNumber);
                mToolbarColor = ((Constants) getApplication()).TOOLBAR_COLOR.get(randomNumber);
                mStatusBarColor = ((Constants) getApplication()).STATUS_BAR_COLOR.get(randomNumber);
                mPopupHeaderColor = ((Constants) getApplication()).PUPUP_COLOR.get(randomNumber);

            } else {
                mToolbarColor = ((Constants) getApplication()).TOOLBAR_COLOR.get(7);
                mStatusBarColor = ((Constants) getApplication()).STATUS_BAR_COLOR.get(7);
                mPopupHeaderColor = ((Constants) getApplication()).PUPUP_COLOR.get(7);
            }

            Log.d(LOG_TAG, "mToolbarColor:" + mToolbarColor);
            Log.d(LOG_TAG, "mStatusBarColor:" + mStatusBarColor);
            Log.d(LOG_TAG, "mPopupHeaderColor:" + mPopupHeaderColor);

            ((Constants) getApplication()).setToolbarColor(mToolbarColor);
            ((Constants) getApplication()).setStatusBarColor(mStatusBarColor);
            ((Constants) getApplication()).setPopupHeaderColor(mPopupHeaderColor);
        }

        // Set status bar and navigation bar color
        if (Build.VERSION.SDK_INT >= 21) {
            // Top status bar
            getWindow().setStatusBarColor(getResources().getColor(mStatusBarColor));

            // Bottom navigation bar
            getWindow().setNavigationBarColor(getResources().getColor(mToolbarColor));
            getWindow().getDecorView().getRootView().setBackgroundColor(getResources().getColor(mStatusBarColor));
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(mToolbarColor));
        mToolbar.setTitle("");
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("now");
        setSupportActionBar(mToolbar);

        FloatingActionButton mFabNew = (FloatingActionButton) findViewById(R.id.fab_new);
        Drawable mBackground = (Drawable) getResources().getDrawable(R.drawable.fab_background);
        mFabNew.setBackground(mBackground);
        mFabNew.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.d(LOG_TAG, "zztg2: getMeasuredWidth=" + mFabNew.getMeasuredWidth() + "getMeasuredHeight=" + mFabNew.getMeasuredHeight());

        int[] mFabNewLocation = new int[2];

        mFabNew.getLocationInWindow(mFabNewLocation);

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(mToolbarColor));

        Bitmap bg = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        canvas.drawCircle(400, 400, 400, paint);

        Paint eraser = new Paint();
        eraser.setStrokeWidth(12);
        eraser.setStyle(Paint.Style.FILL);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawLine(400, 550, 400, 250, eraser);
        canvas.drawLine(550, 400, 250, 400, eraser);

        canvas.drawCircle(mFabNewLocation[0], mFabNewLocation[1], 100, paint);


        mFabNew.setBackground(new BitmapDrawable(bg));
        mFabNew.setBackgroundColor(getResources().getColor(mToolbarColor));

        mFabNew.setBackgroundDrawable(new BitmapDrawable(bg));

//        mShakeListener = new ShakeListener(this);
//        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
//            public void onShake() {
//
//                Context context = getApplicationContext();
//                LayoutInflater inflater = getLayoutInflater();
//
//                View toastRoot = inflater.inflate(R.layout.about, null);
////                toastRoot.setBackgroundColor(getResources().getColor(mToolbarColor));
//
//                CardView cardView = (CardView)toastRoot.findViewById(R.id.cardview);
//                cardView.setBackgroundColor(getResources().getColor(mToolbarColor));
//                Toast toast = new Toast(context);
//
//                toast.setView(toastRoot);
//                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
//                        0, 0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.show();
//            }
//        });

        //Initializing NavigationView
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.inflateHeaderView(R.layout.header);


//        mNavigationView.setBackgroundColor(getResources().getColor(mPopupHeaderColor));
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            FragmentManager fragmentManager = getFragmentManager();
//            Fragment fragment = new MatchFragment();

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    case R.id.action_settings:
//                        toolbar.setTitle("Dota 2 Analysis");
//                        if (((Constants) getApplication()).getSteamid() != null) {
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.container, new SummaryFragment())
//                                    .commit();
//                        }

                        return true;

//                    case R.id.action_settings:
//                        toolbar.setTitle("Latest Match");
//                        if (((Constants) getApplication()).getSteamid() != null) {
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.container, new MatchFragment())
//                                    .commit();
//                        }
//                        return true;

                     case R.id.play:
                         Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                         startActivity(intent);
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

//        final ActionBar mActionBar = getSupportActionBar();
//        mActionBar.setHomeAsUpIndicator(R.drawable.ic_add);
//        mActionBar.setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,mToolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        mDrawerToggle.syncState();

//        if (savedInstanceState == null) {
//            mNavigationView.getMenu().getItem(0).setChecked(true);
//            if (((Constants) getApplication()).getSteamid() != null) {
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.container, new SummaryFragment())
//                        .commit();
//            }
//        }

        new MomentAsyncTask(this).execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MomentAsyncTask extends AsyncTask<Void, Void, List<Moment>> {
        private MomentApi myApiService = null;
        private Context context;

        MomentAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Moment> doInBackground(Void... params) {
            if (myApiService == null) {  // Only do this once
//                MomentApi.Builder builder = new MomentApi.Builder(AndroidHttp.newCompatibleTransport(),
//                    new AndroidJsonFactory(), null)
//                    // options for running against local devappserver
//                    // - 10.0.2.2 is localhost's IP address in Android emulator
//                    // - turn off compression when running against local devappserver
//                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
//                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                        @Override
//                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                            abstractGoogleClientRequest.setDisableGZipContent(true);
//                        }
//                    });
            // end options for devappserver

                MomentApi.Builder builder = new MomentApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://now-livethemoment.appspot.com/_ah/api/");

                myApiService = builder.build();
            } else {
                Log.d(LOG_TAG, "myApiService is null");
            }

            try {
                return myApiService.list().execute().getItems();
            } catch (IOException e) {
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        protected void onPostExecute(List<Moment> result) {
            Log.d(LOG_TAG, "result size:" + result.size());
            for (Moment moment : result) {
                Toast.makeText(context, moment.getSubject() + ", " + moment.getNote(), Toast.LENGTH_LONG).show();

                long categoryRowId = -1;
                categoryRowId = getCategoryDefaultRowId(this.context);

                ContentValues newNotaValues = new ContentValues();
                newNotaValues.put(NotaContract.NotaEntry.COLUMN_CAT_KEY, categoryRowId);
                newNotaValues.put(NotaContract.NotaEntry.COLUMN_SUBJECT, moment.getSubject());
                newNotaValues.put(NotaContract.NotaEntry.COLUMN_NOTE, moment.getNote());
                newNotaValues.put(NotaContract.NotaEntry.COLUMN_START, System.currentTimeMillis());
                newNotaValues.put(NotaContract.NotaEntry.COLUMN_END, System.currentTimeMillis());
                newNotaValues.put(NotaContract.NotaEntry.COLUMN_DURATION, System.currentTimeMillis());
                newNotaValues.put(NotaContract.NotaEntry.COLUMN_LAT, moment.getLatitude());
                newNotaValues.put(NotaContract.NotaEntry.COLUMN_LON, moment.getLongitude());

                long notaRowId = -1;

                notaRowId = insertNewNota(this.context, newNotaValues);
                Log.d(LOG_TAG, "notaId: " + notaRowId);
                MainActivityFragment.mNotaAdapter.notifyDataSetChanged();
                MainActivityFragment.mRecyclerView.setAdapter(MainActivityFragment.mNotaAdapter);

            }

        }
    }

    private long insertNewNota(Context context, ContentValues contentValues) {
        NotaDbHelper dbHelper = new NotaDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long notaRowId = -1;
        notaRowId = db.insert(NotaContract.NotaEntry.TABLE_NAME, null, contentValues);

        return notaRowId;
    }

    static ContentValues createNotaValues(long categoryRowId) {

        ContentValues testValuesNota = new ContentValues();
        testValuesNota.put(NotaContract.NotaEntry.COLUMN_CAT_KEY, categoryRowId);
        testValuesNota.put(NotaContract.NotaEntry.COLUMN_SUBJECT, "running");
        testValuesNota.put(NotaContract.NotaEntry.COLUMN_NOTE, "I'm feeling good!");
        testValuesNota.put(NotaContract.NotaEntry.COLUMN_START, 1441822024L);
        testValuesNota.put(NotaContract.NotaEntry.COLUMN_END, 1441822024L);
        testValuesNota.put(NotaContract.NotaEntry.COLUMN_DURATION, 365);
        testValuesNota.put(NotaContract.NotaEntry.COLUMN_LAT, 38.9338);
        testValuesNota.put(NotaContract.NotaEntry.COLUMN_LON, -92.3183);

        return testValuesNota;
    }

    private long getCategoryDefaultRowId(Context context) {
        NotaDbHelper dbHelper = new NotaDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursorCategory = context.getContentResolver().query(
                NotaContract.CategoryEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns
                NotaContract.CategoryEntry.COLUMN_NAME + " = " + "'default'", // columns for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        long categoryRowId = -1;

        if (cursorCategory.moveToFirst()) {
            // get the default category row id
            int columnNameIndex = cursorCategory.getColumnIndex(NotaContract.CategoryEntry._ID);
            categoryRowId = cursorCategory.getLong(columnNameIndex);
            Log.d(LOG_TAG, "We have a default category with row id: " + categoryRowId);
        } else {
            // create default category
            ContentValues testValuesCategory = createCategoryValues();
            categoryRowId = db.insert(NotaContract.CategoryEntry.TABLE_NAME, null, testValuesCategory);
            Log.d(LOG_TAG, "We don't have a default category. So we create the default category with row id: " + categoryRowId);
        }
        return categoryRowId;
    }

    private ContentValues createCategoryValues() {

        ContentValues testValuesCategory = new ContentValues();
        testValuesCategory.put(NotaContract.CategoryEntry.COLUMN_NAME, "default");
        testValuesCategory.put(NotaContract.CategoryEntry.COLUMN_TOTAL_TIME, 0);

        return testValuesCategory;
    }
}
