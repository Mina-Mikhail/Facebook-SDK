package com.mina_mikhail.facebook_sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.facebook.FacebookSdk;
import com.mina_mikhail.facebook_sdk.callback.FacebookResponse;
import com.mina_mikhail.facebook_sdk.utils.CommonUtils;
import java.util.Arrays;
import java.util.List;

import static com.mina_mikhail.facebook_sdk.utils.Constants.FB_USER_EMAIL;
import static com.mina_mikhail.facebook_sdk.utils.Constants.FB_USER_PROFILE_PIC;

public class FacebookSDK {

  @SuppressLint("StaticFieldLeak")
  private static volatile FacebookSDK INSTANCE;

  private Context context;
  private List<String> permissions;
  private FacebookResponse response;

  public static synchronized FacebookSDK getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FacebookSDK();
    }
    return INSTANCE;
  }

  public void logIn(Context context, @NonNull FacebookResponse response) {
    this.context = context;
    this.response = response;
    initFacebook();
  }

  private void initFacebook() {
    String fbAppId =
        CommonUtils.getMetaDataValue(context, context.getString(R.string.facebookAppId));
    if (!TextUtils.isEmpty(fbAppId)) {
      FacebookSdk.setApplicationId(fbAppId);
      permissions = Arrays.asList(FB_USER_EMAIL, FB_USER_PROFILE_PIC);
      FacebookLogInActivity.open(context);
    }
  }

  List<String> getPermissions() {
    return permissions;
  }

  FacebookResponse getResponse() {
    return response;
  }
}