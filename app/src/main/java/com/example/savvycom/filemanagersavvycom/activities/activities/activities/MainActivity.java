package com.example.savvycom.filemanagersavvycom.activities.activities.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.savvycom.filemanagersavvycom.R;
import com.example.savvycom.filemanagersavvycom.activities.activities.fragment.DisplayFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String DISPLAY_FRAGMENT_TAG = "displayFragment";

    FragmentManager fragmentManager;
    Toolbar toolbar;

    private static final String TAG = "MainActivity";
    private List<File> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestForPermission();

        init();

        displayFragment();

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        ListDir(root);
    }

    private void displayFragment() {
        if (fragmentManager.findFragmentById(R.id.RelativeLayoutMain) == null) {
            DisplayFragment displayFragment = new DisplayFragment();
            setSupportActionBar(toolbar);

            fragmentManager.beginTransaction()
                    .add(R.id.RelativeLayoutMain, displayFragment, DISPLAY_FRAGMENT_TAG)
                    .addToBackStack(DISPLAY_FRAGMENT_TAG)
                    .commit();
        }
    }

    private void init() {
        fragmentManager = getFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    void ListDir(File f) {
        File[] files = f.listFiles();
        fileList.clear();
        for (File file : files) {
            fileList.add(file);
        }
    }


    private void requestForPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                promptForPermissionsDialog(getString(R.string.error_request_permission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                100);
                    }
                });

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
        }
    }

    private void promptForPermissionsDialog(String message, DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("CANCEL", null)
                .create()
                .show();

    }
}
