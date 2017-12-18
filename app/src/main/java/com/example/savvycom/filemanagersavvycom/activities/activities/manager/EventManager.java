package com.example.savvycom.filemanagersavvycom.activities.activities.manager;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.savvycom.filemanagersavvycom.activities.activities.adapter.DisplayFragmentAdapter;
import com.example.savvycom.filemanagersavvycom.activities.activities.fragment.DisplayFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by savvycom on 12/18/2017.
 */

public class EventManager {
    public static final String OPERATION_DELETE = "DELETE";

    private Context mContext;
    private FileManager mFileManager;
    private ArrayList<File> filesAndFolders;
    private DisplayFragment mDisplayFragment;
    private DisplayFragmentAdapter mAdapter;
    private static EventManager instance;

    private EventManager() {

    }

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void init(Context context, DisplayFragment displayFragment, DisplayFragmentAdapter displayFragmentAdapter, ArrayList<File> filesAndFolders) {
        this.filesAndFolders = filesAndFolders;
        this.mAdapter = displayFragmentAdapter;
        this.mContext = context;
        this.mDisplayFragment = displayFragment;
        mFileManager = new FileManager();
    }

    public void open(File file) {
        if (!file.canRead()) {
            Toast.makeText(mContext, "No support to read file", Toast.LENGTH_SHORT).show();
            return;
        }

        if (file.isFile()) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            Intent intentOpenFile = new Intent();
            intentOpenFile.setAction(Intent.ACTION_VIEW);
            String mimeType = mime.getExtensionFromMimeType(mFileManager.getExtension(file.getAbsolutePath()).substring(1));
            intentOpenFile.setDataAndType(Uri.fromFile(file), mimeType);
            intentOpenFile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                mContext.startActivity(intentOpenFile);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, "No handler this type of file", Toast.LENGTH_SHORT).show();
            }
        } else if (file.isDirectory()) {
            addFile(file);
            mDisplayFragment.getToolbar().setTitle(file.getName());
        }
    }

    private void addFile(File file) {
        filesAndFolders.clear();
        File[] files = file.listFiles();
        for (File f : files) {
            filesAndFolders.add(f);
        }
        mAdapter.notifyDataSetChanged();
    }

    public boolean deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory() && file.canWrite()) {
            ArrayList<File> subFiles = new ArrayList<>(Arrays.asList(file.listFiles()));

            for (File subFile : subFiles) {
                deleteFile(subFile);
            }
            file.delete();
        }
        return true;
    }

    public void delete(File files) {
        new BackgroundWork(OPERATION_DELETE, files, null).execute();
    }

    private class BackgroundWork extends AsyncTask<Void, String, Boolean> {

        private String operation;
        private ProgressDialog progressDialog;
        private File sources;
        private File destination;

        public BackgroundWork(String operation, File sources, File destination) {
            this.operation = operation;
            this.sources = sources;
            this.destination = destination;
        }

        @Override
        protected void onPreExecute() {
            switch (operation) {
                case OPERATION_DELETE:
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Deleting...");
                    progressDialog.show();
                    break;
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            switch (operation) {
                case OPERATION_DELETE:
                    try {
                        filesAndFolders.remove(sources);
                        deleteFile(sources);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            switch (operation) {

                case OPERATION_DELETE:
                    progressDialog.setMessage("Deleting  " + values[0]);
                    break;

            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            switch (operation) {


                case OPERATION_DELETE:
                    mAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Files successfully deleted", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

}
