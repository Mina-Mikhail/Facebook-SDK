/*
 * *
 *  * Created by Mina Mikhail on 3/4/19 10:30 AM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 3/4/19 10:30 AM
 *
 */

package com.mina_mikhail.facebook_sdk.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.mina_mikhail.facebook_sdk.R;

/*
 * *
 *  * Created by Mina Mikhail on 16/04/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
 */

public final class CommonUtils {

  private static final String TAG = "CommonUtils";

  private CommonUtils() {
    // This utility class is not publicly instantiable
  }

  public static ProgressDialog showLoadingDialog(Activity activity) {
    if (activity == null || activity.isFinishing()) {
      return null;
    }

    ProgressDialog progressDialog = new ProgressDialog(activity);
    progressDialog.show();
    if (progressDialog.getWindow() != null) {
      progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    progressDialog.setContentView(R.layout.progress_dialog);

    ProgressBar progressBar = progressDialog.findViewById(R.id.loading);
    Sprite circle = new Circle();
    circle.setColor(activity.getResources().getColor(R.color.libAccentColor));
    progressBar.setIndeterminateDrawable(circle);

    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(false);
    progressDialog.setCanceledOnTouchOutside(false);
    return progressDialog;
  }

  public static void hideLoadingDialog(ProgressDialog mProgressDialog, Activity activity) {
    if (activity != null && !activity.isFinishing() && mProgressDialog != null && mProgressDialog
        .isShowing()) {
      mProgressDialog.dismiss();
    }
  }

  @Nullable
  public static String getMetaDataValue(Context context, String name) {
    ApplicationInfo applicationInfo;
    try {
      applicationInfo = context.getPackageManager().getApplicationInfo(
          context.getPackageName(), PackageManager.GET_META_DATA);
    } catch (PackageManager.NameNotFoundException e) {
      return null;
    }

    //noinspection ConstantConditions
    return ((String) applicationInfo.metaData.get(name)).trim();
  }
}