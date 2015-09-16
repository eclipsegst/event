package com.example.android.event;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mPopupHeaderColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbarColor = ((Constants) getApplication()).getToolbarColor();
        mStatusBarColor = ((Constants) getApplication()).getStatusBarColor();
        mPopupHeaderColor = ((Constants) getApplication()).getPopupHeaderClor();

        if (mToolbarColor == -1 || mStatusBarColor == -1 || mPopupHeaderColor == -1) {
            int randomNumber = utilities.randInt(0, ((Constants) getApplication()).TOOLBAR_COLOR.size());

            mToolbarColor = ((Constants) getApplication()).TOOLBAR_COLOR.get(randomNumber);
            mStatusBarColor = ((Constants) getApplication()).STATUS_BAR_COLOR.get(randomNumber);
            mPopupHeaderColor = ((Constants) getApplication()).PUPUP_COLOR.get(randomNumber);

            ((Constants) getApplication()).setToolbarColor(mToolbarColor);
            ((Constants) getApplication()).setStatusBarColor(mStatusBarColor);
            ((Constants) getApplication()).setStatusBarColor(mPopupHeaderColor);
        }

        // Set status bar and navigation bar color
        if (Build.VERSION.SDK_INT >= 21) {
            // Top status bar
            getWindow().setStatusBarColor(getResources().getColor(mStatusBarColor));

            // Bottom navigation bar
            getWindow().setNavigationBarColor(getResources().getColor(mToolbarColor));
            getWindow().getDecorView().getRootView().setBackgroundColor(getResources().getColor(mStatusBarColor));
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
