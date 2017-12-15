package com.example.savvycom.filemanagersavvycom.activities.activities;

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
}
