package com.example.savvycom.filemanagersavvycom.activities.activities.manager;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by savvycom on 12/15/2017.
 */

public class FileManager {

    public FileManager(){}

    public String getExtension(String url) {


        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();
        }
    }

    public boolean newFolder(File directory, String name){
        return new File(directory, name).mkdir();
    }


}
