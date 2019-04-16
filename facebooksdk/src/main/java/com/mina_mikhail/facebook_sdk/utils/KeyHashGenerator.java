package com.mina_mikhail.facebook_sdk.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * *
 *  * Created by Mina Mikhail on 16/04/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
 */

public class KeyHashGenerator {

  /**
   * To generate the key hash to add in the facebook account
   * https://developers.facebook.com/apps/
   *
   * @param activity the instance of the activity, where you gonna place the facebook login.
   */
  @SuppressLint("PackageManagerGetSignatures")
  public static String generateKey(Activity activity) {
    PackageInfo packageInfo;
    String key = null;
    try {
      //getting application package name, as defined in manifest
      String packageName = activity.getApplicationContext().getPackageName();

      //Retrieving package info
      packageInfo = activity.getPackageManager().getPackageInfo(packageName,
          PackageManager.GET_SIGNATURES);
      Log.i("Package Name= ", activity.getApplicationContext().getPackageName());
      for (Signature signature : packageInfo.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        key = new String(Base64.encode(md.digest(), 0));
        Log.i("Key Hash= ", key);
      }
    } catch (PackageManager.NameNotFoundException e1) {
      Log.e("Name not found", e1.toString());
    } catch (NoSuchAlgorithmException e) {
      Log.e("No such an algorithm", e.toString());
    } catch (Exception e) {
      Log.e("Exception", e.toString());
    }
    return key;
  }
}