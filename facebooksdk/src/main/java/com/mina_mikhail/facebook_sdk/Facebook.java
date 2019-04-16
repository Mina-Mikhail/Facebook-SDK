package com.mina_mikhail.facebook_sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.facebook.FacebookSdk;
import com.mina_mikhail.FacebookData;
import com.mina_mikhail.facebook_sdk.callback.FacebookResponse;
import com.mina_mikhail.facebook_sdk.utils.CommonUtils;
import java.util.Arrays;

public class Facebook {

  @SuppressLint("StaticFieldLeak")
  private static volatile Facebook INSTANCE;

  private Context context;
  private FacebookData facebookSocialData;

  public static synchronized Facebook getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Facebook();
    }
    return INSTANCE;
  }

  public void connect(Context context, @NonNull FacebookResponse response) {
    Context appContext = context.getApplicationContext();
    getInstance().context = context;
    getInstance().initFacebook(appContext);

    connectFacebook(response);
  }

  private void initFacebook(Context appContext) {
    String fbAppId = CommonUtils.getMetaDataValue(appContext,
        appContext.getString(R.string.com_mina_mikhail_facebook_sdk_facebookAppId));
    if (!TextUtils.isEmpty(fbAppId)) {
      FacebookSdk.setApplicationId(fbAppId);
    }
  }

  private void connectFacebook(@NonNull FacebookResponse response) {
    facebookSocialData = new FacebookData(Arrays.asList("email", "public_profile"), response);
    FacebookLogInActivity.start(context);
  }

  FacebookData getFacebookData() {
    return facebookSocialData;
  }
}