package com.elena.noisecalibration;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends Activity {
    ViewFlipper mFlipper;
    private final String TAG = "Flipper App";
    String pathToImages = "/sdcard/ImagesForApp";
    String imgType = "all";
    int flipTime = 3000;
    ArrayList<String> mFiles = new ArrayList<String>();

    //intent to run app from command line is
    // adb shell am start -a android.intent.action.VIEW -c android.intent.category.DEFAULT -e path /sdcard/ImagesForApp1 -e type jpg -e delay 3000 -n com.elena.noisecalibration/com.elena.noisecalibration.MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
        mFlipper = (ViewFlipper) findViewById(R.id.mFlipper);

        Bundle cmdExtras = this.getIntent().getExtras();
        if (cmdExtras != null) {
            if (cmdExtras.containsKey("path")) {
                pathToImages = cmdExtras.getString("path");
            } else {
                pathToImages = "/sdcard/ImagesForApp";
            }
            Log.d(TAG, "path is " + pathToImages);

            if (cmdExtras.containsKey("type")) {
                imgType = cmdExtras.getString("type");
            } else {
                imgType = "all";
            }
            Log.d(TAG, "Image type is " + imgType);

            if (cmdExtras.containsKey("delay")) {
                flipTime = Integer.parseInt(cmdExtras.getString("delay"));
            } else {
                flipTime = 3000;
            }
            Log.d(TAG, "Flip interval: " + flipTime + " ms");
        } else {
            Log.d (TAG, "No extras in cmd line, defaults: path=" + pathToImages + "; type=all; time interval=" + flipTime + "ms");
        }


        File mImgs = new File(pathToImages);
        getFilesList(mImgs.getAbsolutePath(), imgType);

        if (mFiles.size() > 0) {
            for (int i = 0; i < mFiles.size(); i++) {
                //Log.d(TAG, "inside " + i + ": " + mFiles.get(i));
                setFlipperImage(i);
            }
        } else {
            Log.d(TAG, "no files in folder");
        }

        mFlipper.setFlipInterval(flipTime);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d(TAG, "onResume called");
        mFlipper.startFlipping();
    }

    private void setFlipperImage (int im) {
        File imgFile = new File(mFiles.get(im));

        ImageView mImage = new ImageView(getApplicationContext());
        BitmapFactory.Options mOpts = new BitmapFactory.Options();
        mOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), mOpts);
        mImage.setImageBitmap(mBitmap);
        mImage.setScaleType(ImageView.ScaleType.FIT_XY);

        mFlipper.addView(mImage);
       // Log.d(TAG, "images in flipper: " + mFlipper.getChildCount());
    }

    private void getFilesList(String mPath, String imgType) {

        File root = new File(mPath);
        File[] list = root.listFiles();

        if (list != null) {
            for (File f : list) {
                if (imgType.equals("all")) {
                    mFiles.add(f.getAbsolutePath());
                    Log.d(TAG, "file " + f.getAbsolutePath());
                } else if (f.getAbsolutePath().endsWith(imgType)) {
                    mFiles.add(f.getAbsolutePath());
                    Log.d(TAG, "file " + f.getAbsolutePath());
                }
            }
        }

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
