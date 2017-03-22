package com.signmeastory.apps.gb;

import java.io.File;

import android.net.Uri;

import com.android.vending.expansion.zipfile.APEZProvider;

public class ZipContentProvider extends APEZProvider {
private static final String AUTHORITY = "com.signmeastory.apps.gb";

public static Uri buildUri(String pathIntoApk) {
//public static String buildUri(String pathIntoApk) {
    StringBuilder contentPath = new StringBuilder("content://");

    contentPath.append(AUTHORITY);
    contentPath.append(File.separator);
    contentPath.append(pathIntoApk);

    return Uri.parse(contentPath.toString());
    //return contentPath.toString();
}

@Override
public String getAuthority() {
    return AUTHORITY;
}
}