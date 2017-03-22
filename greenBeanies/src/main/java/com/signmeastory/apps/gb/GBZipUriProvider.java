package com.signmeastory.apps.gb;

import com.android.vending.expansion.zipfile.APEZProvider;

public class GBZipUriProvider extends APEZProvider
{

  @Override
  public String getAuthority()
  {
    return "com.signmeastory.apps.gb.GBZipUriProvider";
  }

}