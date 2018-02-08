package com.happybananastudio.mgint.abxspectrum;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by mgint on 10/14/2017.
 */

public class activityAbout extends AppCompatActivity {

    Context thisContext;

    private ImageButton iButtonDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        thisContext = this;
        hideBars();

        initializeImageButton();

        setImageButtonDismissListener();
    }


    private void hideBars(){

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
    private void initializeImageButton() {
        iButtonDismiss = (ImageButton) findViewById(R.id.iButtonDismiss);
    }
    private void setImageButtonDismissListener() {
        iButtonDismiss.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                finish();
            }});
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideBars();
        }
    }
}
