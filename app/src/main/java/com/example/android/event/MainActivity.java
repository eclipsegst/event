package com.example.android.event;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mPopupHeaderColor;

    private ShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbarColor = ((Constants) getApplication()).getToolbarColor();
        mStatusBarColor = ((Constants) getApplication()).getStatusBarColor();
        mPopupHeaderColor = ((Constants) getApplication()).getPopupHeaderClor();

        if (mToolbarColor == -1 || mStatusBarColor == -1 || mPopupHeaderColor == -1) {
            int randomNumber = -1;
            randomNumber = utilities.randInt(0, ((Constants) getApplication()).TOOLBAR_COLOR.size());

            if (randomNumber != -1) {
                mToolbarColor = ((Constants) getApplication()).TOOLBAR_COLOR.get(randomNumber);
                mStatusBarColor = ((Constants) getApplication()).STATUS_BAR_COLOR.get(randomNumber);
                mPopupHeaderColor = ((Constants) getApplication()).PUPUP_COLOR.get(randomNumber);

            } else {
                mToolbarColor = -1;
                mStatusBarColor = -1;
                mPopupHeaderColor = -1;
            }

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

        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
//                Toast.makeText(MainActivity.this, "Shake ", Toast.LENGTH_LONG).show();
//                Toast toast = Toast.makeText(MainActivity.this, "Life is full of different colors. It's about about now.", Toast.LENGTH_LONG);
//                View view = toast.getView();
//                view.setBackgroundColor(getResources().getColor(mToolbarColor));
//
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
//
////                LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////                View view = inflater.inflate(R.layout.about, null);
////                toast.setView(view);
//                toast.show();
//
////                TextView text = (TextView) view.findViewById(android.R.id.message);
////                toast.show();

                Context context = getApplicationContext();
                LayoutInflater inflater = getLayoutInflater();

                View toastRoot = inflater.inflate(R.layout.about, null);
//                toastRoot.setBackgroundColor(getResources().getColor(mToolbarColor));

                CardView cardView = (CardView)toastRoot.findViewById(R.id.cardview);
                cardView.setBackgroundColor(getResources().getColor(mToolbarColor));
                Toast toast = new Toast(context);

                toast.setView(toastRoot);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                        0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }
        });

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

        //i
        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
