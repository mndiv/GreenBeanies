package com.signmeastory.apps.gb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;

public class GBDownloadBroadcastReceiver extends BroadcastReceiver
{

  @Override
  public void onReceive(Context context, Intent intent)
  {
    try
    {
      DownloaderClientMarshaller.startDownloadServiceIfRequired(context, intent, GBDownloadBroadcastReceiver.class);
    }
    catch (NameNotFoundException e)
    {
      Log.e("GBBroadcastReceiver", e.getMessage(), e);
    }
  }
}