package com.happybananastudio.mgint.abxspectrum;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mgint on 8/24/2017.
 */

public class activityPopUp extends AppCompatActivity {

    Context thisContext;

    private ImageButton iButtonDismiss;
    private ImageButton iButtonEdit;

    private TextView tViewUser;
    private TextView tViewMyComments;

    private String stringTitle;
    private String stringInfo;
    private String stringUser;
    private String showComment;
    private String stringFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        thisContext = this;
        hideBars();

        getStringExtras();

        initializeTextViews();
        initializeImageButton();

        setImageButtonDismissListener();
        setImageButtonEditListener();

        handleShowComments();
    }

    private void getStringExtras()
    {
        String line;

        stringFileName = getIntent().getStringExtra("file");
        stringTitle = getIntent().getStringExtra("title");
        stringInfo = getIntent().getStringExtra("info");
        showComment = getIntent().getStringExtra("showComment");

        if( !stringFileName.equals("")) {
            File file = new File(getFilesDir(), stringFileName);
            BufferedReader bReaderFile = null;

            try {
                bReaderFile = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            StringBuilder inputBuffer = new StringBuilder();

            try {
                if (bReaderFile != null) {
                    while ((line = bReaderFile.readLine()) != null) {
                        inputBuffer.append(line).append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            stringUser = inputBuffer.toString();

            try {
                if (bReaderFile != null) {
                    bReaderFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setImageButtonDismissListener()
    {
        iButtonDismiss.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                finish();
            }});
    }
    private void setImageButtonEditListener()
    {
        iButtonEdit.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                dialogConfirmCancelEdit();
            }});
    }

    private void initializeImageButton()
    {
        iButtonDismiss = (ImageButton) findViewById(R.id.iButtonDismiss);
        iButtonEdit = (ImageButton) findViewById(R.id.iButtonEdit);
    }
    private void initializeTextViews()
    {
        TextView tViewTitle = (TextView) findViewById(R.id.tViewDialogTitle);
        TextView tViewInfo = (TextView) findViewById(R.id.tViewInformation);
        tViewUser = (TextView) findViewById(R.id.tViewUser);
        tViewMyComments = (TextView) findViewById(R.id.tViewMyComments);

        tViewUser.setText(stringUser);
        tViewTitle.setText(stringTitle);
        tViewInfo.setText(stringInfo);
    }

    private void handleShowComments()
    {
        if( showComment.equals("False") )
        {
            tViewUser.setVisibility(View.GONE);
            tViewMyComments.setVisibility(View.GONE);
            iButtonEdit.setVisibility(View.GONE);
        }
        else
        {
            tViewUser.setVisibility(View.VISIBLE);
            tViewMyComments.setVisibility(View.VISIBLE);
            iButtonEdit.setVisibility(View.VISIBLE);
        }
    }

    private void dialogConfirmCancelEdit() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(thisContext);
        String temp;
        TextView title = new TextView(thisContext);
        final EditText input = new EditText(thisContext);

        input.setMaxLines(6);
        input.setSingleLine(false);
        dialogBuilder.setView(input);

        title.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);

        title.setPadding(0,30,0,0);
        title.setLayoutParams(lp);
        temp = getString(R.string.edit_text) + stringTitle;
        title.setText(temp);
        title.setGravity(Gravity.CENTER);

        dialogBuilder.setCustomTitle(title);
        dialogBuilder.setCancelable(false);

        if( !stringUser.equals("<Empty>\n")) {
            input.setText(stringUser);
        }

        dialogBuilder.setView(input);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                stringUser = s.toString();
            }
        });

        dialogBuilder.setPositiveButton("Save Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File userFile;
                FileOutputStream oFile;

                tViewUser.setText(stringUser);
                userFile = new File(getFilesDir(), stringFileName);

                if( userFile.exists() )
                {
                    try
                    {
                        userFile.delete();
                        userFile.createNewFile();
                        oFile = new FileOutputStream(userFile, false);
                        oFile.write(stringUser.getBytes());

                        oFile.close();

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                hideBars();
            }
        });

        dialogBuilder.setNegativeButton("Cancel Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideBars();}
        });

        dialogBuilder.show();

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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideBars();
        }
    }
}
