package com.happybananastudio.mgint.abxspectrum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.content.Intent;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class activityMain extends AppCompatActivity {

    private Context thisContext;

    private int numberOfHeader = 4;
    private int numberOfABX = 24;

    private String SHOW_COMMENT = "True";

    private ArrayList<String> arrayAbbreviationHeader;
    private ArrayList<String> arrayFileHeader;
    private ArrayList<String> arrayTitleHeader;
    private ArrayList<String> arrayInformationHeader;

    private ArrayList<String> arrayAbbreviationABX;
    private ArrayList<String> arrayFileABX;
    private ArrayList<String> arrayTitleABX;
    private ArrayList<String> arrayInformationABX;

    private LinearLayout lLayoutHeader;
    private LinearLayout lLayoutABX;

    private ImageButton iButtonResetComments;
    private ImageButton iButtonCommentVisibility;
    private ImageButton iButtonHelp;
    private ImageButton iButtonAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisContext = this;
        hideBars();

        initializeArraysHeader();
        initializeArraysABX();

        initializeUserFiles();

        initializeLayouts();
        initializeImageButtons();

        getPreferences();

        fillLayouts();
    }

    private void fillLayoutHeader() {
        int imageID, count;
        String title, info, file;
        ImageButton iButtonContent;

        count = arrayFileHeader.size();

        for( int i = 0; i < count; ++i)
        {
            imageID = getDrawableID(arrayFileHeader.get(i));
            file = arrayFileHeader.get(i);
            title = arrayTitleHeader.get(i);
            info = arrayInformationHeader.get(i);
            iButtonContent = createImageButton(imageID, file, title, info, 0);

            lLayoutHeader.addView(iButtonContent);

        }
    }

    private void fillLayoutABX(){
        int imageID, count;
        String title, info, file;
        ImageButton iButtonContent;

        count = arrayFileABX.size();

        for( int i = 0; i < count; ++i)
        {
            imageID = getDrawableID(arrayFileABX.get(i));
            file = arrayFileABX.get(i);
            title = arrayTitleABX.get(i);
            info = arrayInformationABX.get(i);
            iButtonContent = createImageButton(imageID, file, title, info, 1);

            lLayoutABX.addView(iButtonContent);
        }
    }

    private ImageButton createImageButton( int imageID,
                                           final String file,
                                           final String title,
                                           final String info,
                                           Integer style) {
        ImageButton iButton;
        LinearLayout.LayoutParams parameters;
        float weight;
        int width;

        if( file.equals("header_02_ana") || file.equals("header_03_aty"))
        {
            weight = .25f;
        }
        else
        {
            weight = 1f;
        }

        if( style == 0 )
        {
            width = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            width = LinearLayout.LayoutParams.MATCH_PARENT;
        }

        parameters = new TableRow.LayoutParams(width,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                weight);
        iButton = new ImageButton(thisContext, null,
                android.R.attr.borderlessButtonStyle);

        parameters.setMargins(0, 0, 0, 0);

        iButton.setPadding(0, 0, 0, 0);
        iButton.setImageResource(imageID);
        iButton.setScaleType(ImageView.ScaleType.FIT_XY);
        iButton.setAdjustViewBounds(true);
        iButton.setBackgroundColor(Color.TRANSPARENT);
        iButton.setLayoutParams(parameters);

        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisContext, activityPopUp.class);

                intent.putExtra("title", title);
                intent.putExtra("info", info);
                intent.putExtra("file", file);
                intent.putExtra("showComment", SHOW_COMMENT);

                startActivity(intent);
            }
        });

        return iButton;
    }

    private int getDrawableID(String fileName) {
        Resources resources = thisContext.getResources();

        final int resourceID = resources.getIdentifier(fileName,
                "drawable", thisContext.getPackageName());

        return resourceID;
    }

    private void setImageButtonListeners() {
        setResetCommentsListener();
        setCommentVisibilityListener();
        setAboutListener();
        setHelpListener();
    }
    private void setResetCommentsListener() {
        iButtonResetComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmCancelDelete();
            }
        });
    }
    private void setCommentVisibilityListener() {
        iButtonCommentVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File userFile;
                FileOutputStream oFile;

                if( SHOW_COMMENT.equals("True"))
                {
                    SHOW_COMMENT = "False";
                    iButtonCommentVisibility.setImageResource(R.drawable.enable);
                }
                else
                {
                    SHOW_COMMENT = "True";
                    iButtonCommentVisibility.setImageResource(R.drawable.disable);
                }

                userFile = new File(getFilesDir(), "userPreferences.txt");

                if( userFile.exists() )
                {
                    try
                    {
                        userFile.delete();
                        userFile.createNewFile();
                        oFile = new FileOutputStream(userFile, false);
                        oFile.write(SHOW_COMMENT.getBytes());

                        oFile.close();

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void setHelpListener() {
        iButtonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = getString(R.string.helpHeader);
                String info = getString(R.string.helpContent);
                String file = "";

                Intent intent = new Intent(thisContext, activityPopUp.class);

                intent.putExtra("title", title);
                intent.putExtra("info", info);
                intent.putExtra("file", file);
                intent.putExtra("showComment", "False");

                startActivity(intent);
            }
        });
    }
    private void setAboutListener() {
        iButtonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisContext, activityAbout.class);

                startActivity(intent);
            }
        });
    }

    private void fillLayouts() {
        fillLayoutHeader();
        fillLayoutABX();
    }

    private void initializeLayouts() {

        lLayoutHeader = (LinearLayout) findViewById(R.id.lLayoutHeader);
        lLayoutABX = (LinearLayout) findViewById(R.id.lLayoutABX);

        lLayoutHeader.setBackgroundColor(Color.TRANSPARENT);
        lLayoutABX.setBackgroundColor(Color.TRANSPARENT);
    }
    private void initializeImageButtons() {
        iButtonResetComments = (ImageButton) findViewById(R.id.iButtonResetComments);
        iButtonCommentVisibility = (ImageButton) findViewById(R.id.iButtonCommentVisibility);
        iButtonHelp = (ImageButton) findViewById(R.id.iButtonHelp);
        iButtonAbout = (ImageButton) findViewById(R.id.iButtonAbout);

        if( SHOW_COMMENT.equals("True"))
        {
            iButtonCommentVisibility.setImageResource(R.drawable.disable);
        }
        else
        {
            iButtonCommentVisibility.setImageResource(R.drawable.enable);
        }

        setImageButtonListeners();
    }
    private void initializeUserFiles() {
        setUpFiles( arrayFileHeader );
        setUpFiles( arrayFileABX );

    }
    private void initializeArraysHeader() {
        initializeArrayAbbreviationHeader();
        initializeArrayFileHeader();
        initializeArrayTitleHeader();
        initializeArrayInformationHeader();
    }
    private void initializeArraysABX() {
        initializeArrayAbbreviationABX();
        initializeArrayFileABX();
        initializeArrayTitleABX();
        initializeArrayInformationABX();
    }
    private void initializeArrayAbbreviationHeader() {
        arrayAbbreviationHeader = new ArrayList<>();
        arrayAbbreviationHeader.add("gpc");
        arrayAbbreviationHeader.add("gnb");
        arrayAbbreviationHeader.add("ana");
        arrayAbbreviationHeader.add("aty");
    }
    private void initializeArrayFileHeader() {
        arrayFileHeader = new ArrayList<>();

        for( int i = 0; i < numberOfHeader; ++i)
        {
            arrayFileHeader.add( "header_" + formatInteger( i ) + "_" + arrayAbbreviationHeader.get( i ));
        }
    }
    private void initializeArrayTitleHeader() {
        arrayTitleHeader = new ArrayList<>();

        arrayTitleHeader.add(getString(R.string.header_00_gpc_title));
        arrayTitleHeader.add(getString(R.string.header_01_gnb_title));
        arrayTitleHeader.add(getString(R.string.header_02_ana_title));
        arrayTitleHeader.add(getString(R.string.header_03_aty_title));
    }
    private void initializeArrayInformationHeader() {
        arrayInformationHeader = new ArrayList<>();

        arrayInformationHeader.add(getString(R.string.header_00_gpc_info));
        arrayInformationHeader.add(getString(R.string.header_01_gnb_info));
        arrayInformationHeader.add(getString(R.string.header_02_ana_info));
        arrayInformationHeader.add(getString(R.string.header_03_aty_info));
    }
    private void initializeArrayAbbreviationABX() {
        arrayAbbreviationABX = new ArrayList<>();
        arrayAbbreviationABX.add("pcn");
        arrayAbbreviationABX.add("amp");
        arrayAbbreviationABX.add("ant");
        arrayAbbreviationABX.add("aug");
        arrayAbbreviationABX.add("zos");
        arrayAbbreviationABX.add("1gc");
        arrayAbbreviationABX.add("2gc");
        arrayAbbreviationABX.add("3gc");
        arrayAbbreviationABX.add("4gc");
        arrayAbbreviationABX.add("5gc");
        arrayAbbreviationABX.add("avy");
        arrayAbbreviationABX.add("car");
        arrayAbbreviationABX.add("mon");
        arrayAbbreviationABX.add("ami");
        arrayAbbreviationABX.add("flu");
        arrayAbbreviationABX.add("mac");
        arrayAbbreviationABX.add("tet");
        arrayAbbreviationABX.add("van");
        arrayAbbreviationABX.add("oxa");
        arrayAbbreviationABX.add("dap");
        arrayAbbreviationABX.add("cli");
        arrayAbbreviationABX.add("tig");
        arrayAbbreviationABX.add("met");
        arrayAbbreviationABX.add("bac");
    }
    private void initializeArrayFileABX() {
        arrayFileABX = new ArrayList<>();

        for( int i = 0; i < numberOfABX; ++i)
        {
            arrayFileABX.add( "abx_" + formatInteger( i ) + "_" + arrayAbbreviationABX.get( i ));
        }
    }
    private void initializeArrayTitleABX() {
        arrayTitleABX = new ArrayList<>();

        arrayTitleABX.add(getString(R.string.abx_00_pcn_title));
        arrayTitleABX.add(getString(R.string.abx_01_amp_title));
        arrayTitleABX.add(getString(R.string.abx_02_ant_title));
        arrayTitleABX.add(getString(R.string.abx_03_aug_title));
        arrayTitleABX.add(getString(R.string.abx_04_zos_title));
        arrayTitleABX.add(getString(R.string.abx_05_1gc_title));
        arrayTitleABX.add(getString(R.string.abx_06_2gc_title));
        arrayTitleABX.add(getString(R.string.abx_07_3gc_title));
        arrayTitleABX.add(getString(R.string.abx_08_4gc_title));
        arrayTitleABX.add(getString(R.string.abx_09_5gc_title));
        arrayTitleABX.add(getString(R.string.abx_10_avy_title));
        arrayTitleABX.add(getString(R.string.abx_11_car_title));
        arrayTitleABX.add(getString(R.string.abx_12_mon_title));
        arrayTitleABX.add(getString(R.string.abx_13_ami_title));
        arrayTitleABX.add(getString(R.string.abx_14_flu_title));
        arrayTitleABX.add(getString(R.string.abx_15_mac_title));
        arrayTitleABX.add(getString(R.string.abx_16_tet_title));
        arrayTitleABX.add(getString(R.string.abx_17_van_title));
        arrayTitleABX.add(getString(R.string.abx_18_oxa_title));
        arrayTitleABX.add(getString(R.string.abx_19_dap_title));
        arrayTitleABX.add(getString(R.string.abx_20_cli_title));
        arrayTitleABX.add(getString(R.string.abx_21_tig_title));
        arrayTitleABX.add(getString(R.string.abx_22_met_title));
        arrayTitleABX.add(getString(R.string.abx_23_bac_title));
    }
    private void initializeArrayInformationABX() {
        arrayInformationABX = new ArrayList<>();

        arrayInformationABX.add(getString(R.string.abx_00_pcn_info));
        arrayInformationABX.add(getString(R.string.abx_01_amp_info));
        arrayInformationABX.add(getString(R.string.abx_02_ant_info));
        arrayInformationABX.add(getString(R.string.abx_03_aug_info));
        arrayInformationABX.add(getString(R.string.abx_04_zos_info));
        arrayInformationABX.add(getString(R.string.abx_05_1gc_info));
        arrayInformationABX.add(getString(R.string.abx_06_2gc_info));
        arrayInformationABX.add(getString(R.string.abx_07_3gc_info));
        arrayInformationABX.add(getString(R.string.abx_08_4gc_info));
        arrayInformationABX.add(getString(R.string.abx_09_5gc_info));
        arrayInformationABX.add(getString(R.string.abx_10_avy_info));
        arrayInformationABX.add(getString(R.string.abx_11_car_info));
        arrayInformationABX.add(getString(R.string.abx_12_mon_info));
        arrayInformationABX.add(getString(R.string.abx_13_ami_info));
        arrayInformationABX.add(getString(R.string.abx_14_flu_info));
        arrayInformationABX.add(getString(R.string.abx_15_mac_info));
        arrayInformationABX.add(getString(R.string.abx_16_tet_info));
        arrayInformationABX.add(getString(R.string.abx_17_van_info));
        arrayInformationABX.add(getString(R.string.abx_18_oxa_info));
        arrayInformationABX.add(getString(R.string.abx_19_dap_info));
        arrayInformationABX.add(getString(R.string.abx_20_cli_info));
        arrayInformationABX.add(getString(R.string.abx_21_tig_info));
        arrayInformationABX.add(getString(R.string.abx_22_met_info));
        arrayInformationABX.add(getString(R.string.abx_23_bac_info));
    }

    private String formatInteger( int num ) {
        if( num > 9 )
        {
            return Integer.toString(num);
        }
        else
        {
            return "0" + Integer.toString(num);
        }
    }
    private void setUpFiles( ArrayList<String> userFiles) {
        FileOutputStream oFile;
        String writeLine;
        File userFile;

        for( int i = 0; i < userFiles.size(); ++i )
        {
            userFile = new File(getFilesDir(), userFiles.get(i));

            if( !userFile.exists() )
            {
                try
                {
                    userFile.createNewFile();
                    oFile = new FileOutputStream(userFile, false);
                    writeLine = "<Empty>";
                    oFile.write(writeLine.getBytes());

                    oFile.close();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getPreferences() {
        FileOutputStream oFile;
        String writeLine;
        File preferenceFile;
        String fileName = "userPreferences.txt";
        String line;

        preferenceFile = new File(getFilesDir(), fileName);

        if( !preferenceFile.exists() )
        {
            SHOW_COMMENT = "True";
            iButtonCommentVisibility.setImageResource(R.drawable.disable);

            try
            {
                preferenceFile.createNewFile();
                oFile = new FileOutputStream(preferenceFile, false);
                writeLine = "True";
                oFile.write(writeLine.getBytes());

                oFile.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            File file = new File( getFilesDir(), fileName);
            BufferedReader bReaderFile = null;

            try
            {
                bReaderFile = new BufferedReader(new FileReader(file));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            StringBuffer inputBuffer = new StringBuffer();

            try
            {
                while ((line = bReaderFile.readLine()) != null)
                {
                    inputBuffer.append(line);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            SHOW_COMMENT = inputBuffer.toString();

            try
            {
                bReaderFile.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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

    private void dialogConfirmCancelDelete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(thisContext);
        TextView title = new TextView(thisContext);

        title.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);

        title.setPadding(0,30,0,0);
        title.setLayoutParams(lp);
        title.setText(getString(R.string.delete_comments));
        title.setGravity(Gravity.CENTER);

        dialog.setCustomTitle(title);

        dialog.setPositiveButton("Delete All Comment Files", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File userFile;
                FileOutputStream oFile;
                String writeLine;

                for( int i = 0; i < arrayFileHeader.size(); ++i )
                {
                    userFile = new File(getFilesDir(), arrayFileHeader.get(i));

                    if( userFile.exists() )
                    {
                        try
                        {
                            userFile.delete();
                            userFile.createNewFile();
                            oFile = new FileOutputStream(userFile, false);
                            writeLine = "<Empty>";
                            oFile.write(writeLine.getBytes());

                            oFile.close();

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                for( int i = 0; i < arrayFileABX.size(); ++i )
                {
                    userFile = new File(getFilesDir(), arrayFileABX.get(i));

                    if( userFile.exists() )
                    {
                        try
                        {
                            userFile.delete();
                            userFile.createNewFile();
                            oFile = new FileOutputStream(userFile, false);
                            writeLine = "<Empty>";
                            oFile.write(writeLine.getBytes());

                            oFile.close();

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        dialog.show();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideBars();
        }
    }
}
