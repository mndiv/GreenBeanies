package com.signmeastory.apps.gb;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

public class GBDownloaderService extends DownloaderService
{
  // Public key belonging to your Play Store account
  public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAklnw+6StI1S4f4RjCQLtvzWMzUP35lhckWBMi33HKwfCB26hLYDAO4u2F3wIHiypazQw8o3I+5nig8yQmKcwkJbgcnhrUieSOuaB6j5U5b6Wk22eh5lq9OjsgKy7dpOstyrspSFfEil9xvWjVPIV7Of4JiGtgDf51fykPlB4z5f7flDE3rsWyreBVWmpojf3UU/HsjA5UPwex/M6nxrkc5mLCAPgjJnaOvWQLBkwTghtCdAap07GmylUKXhQVMboyMe/CioromEYebgEep7dZd/q1ImJIEgKfJ1LwaBsHCDJnDuJlwJ+ztVgFZ+VHmzuk6CRKBphJal0XB75uWO9CQIDAQAB";

  public static final byte[] SALT              = new byte[]{4, -8, 15, -16, 23, -42, 4, -8, 15, -16, -23, 42};

  @Override
  public String getPublicKey()
  {
    return BASE64_PUBLIC_KEY;
  }

  @Override
  public byte[] getSALT()
  {
    return SALT;
  }

  @Override
  public String getAlarmReceiverClassName()
  {
    return GBDownloadBroadcastReceiver.class.getName();
  }

}
