package com.mina_mikhail.facebook_sdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mina_mikhail.facebook_sdk.callback.FacebookResponse;
import com.mina_mikhail.facebook_sdk.data.model.FacebookUser;
import com.mina_mikhail.facebook_sdk.utils.CommonUtils;
import com.mina_mikhail.facebook_sdk.utils.NetworkUtils;
import java.util.List;
import org.json.JSONObject;

import static com.mina_mikhail.facebook_sdk.data.enums.ErrorCode.ERROR_NO_INTERNET;
import static com.mina_mikhail.facebook_sdk.data.enums.ErrorCode.ERROR_OTHER;
import static com.mina_mikhail.facebook_sdk.data.enums.ErrorCode.ERROR_USER_CANCELLED;
import static com.mina_mikhail.facebook_sdk.utils.Constants.FB_USER_EMAIL;
import static com.mina_mikhail.facebook_sdk.utils.Constants.FB_USER_ID;
import static com.mina_mikhail.facebook_sdk.utils.Constants.FB_USER_NAME;
import static com.mina_mikhail.facebook_sdk.utils.Constants.PROFILE_PIC_URL;
import static com.mina_mikhail.facebook_sdk.utils.Constants.REQUEST_FIELDS;
import static com.mina_mikhail.facebook_sdk.utils.Constants.REQUEST_PARAMETERS;

/*
 * *
 *  * Created by Mina Mikhail on 16/04/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
 */

public class FacebookLogInActivity
    extends Activity
    implements FacebookCallback<LoginResult>,
    GraphRequest.GraphJSONObjectCallback {

  private CallbackManager callbackManager;
  private ProgressDialog mProgressDialog;

  public static void open(Context context) {
    Intent intent = new Intent(context, FacebookLogInActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    makeStatusBarTransparent();

    if (!NetworkUtils.isNetworkConnected(this)) {
      getResponse()
          .onFacebookLogInFail(ERROR_NO_INTERNET, getResources().getString(R.string.no_internet));
      finish();
    } else {
      initFacebook();
    }
  }

  private void makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
    }
  }

  private void initFacebook() {
    callbackManager = CallbackManager.Factory.create();
    LoginManager.getInstance().registerCallback(callbackManager, this);
    LoginManager.getInstance().logInWithReadPermissions(this, getPermissions());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onSuccess(LoginResult loginResult) {
    showLoading();

    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
    Bundle parameters = new Bundle();
    parameters.putString(REQUEST_FIELDS, REQUEST_PARAMETERS);
    request.setParameters(parameters);
    request.executeAsync();
  }

  @Override
  public void onCancel() {
    getResponse()
        .onFacebookLogInFail(ERROR_USER_CANCELLED,
            getResources().getString(R.string.user_cancelled));
    finish();
  }

  @Override
  public void onError(FacebookException error) {
    getResponse().onFacebookLogInFail(ERROR_OTHER, getResources().getString(R.string.some_error));

    if (error instanceof FacebookAuthorizationException) {
      logOut();
    }

    finish();
  }

  @Override
  public void onCompleted(JSONObject object, GraphResponse response) {
    FacebookUser user = new FacebookUser();
    user.setAccessToken(AccessToken.getCurrentAccessToken().getToken());
    user.setUserID(object.optString(FB_USER_ID));
    user.setFullName(object.optString(FB_USER_NAME));
    user.setUserEmail(object.optString(FB_USER_EMAIL));
    user.setUserProfilePicture(String.format(PROFILE_PIC_URL, user.getUserID()));

    new Handler().postDelayed(() -> {
      logOut();
      hideLoading();
      getResponse().onFacebookLogInSuccess(user);
      finish();
    }, 1000);
  }

  private void logOut() {
    LoginManager.getInstance().logOut();
  }

  private Facebook getFacebookInstance() {
    return Facebook.getInstance();
  }

  private List<String> getPermissions() {
    return getFacebookInstance().getPermissions();
  }

  private FacebookResponse getResponse() {
    return getFacebookInstance().getResponse();
  }

  public void showLoading() {
    hideLoading();
    mProgressDialog = CommonUtils.showLoadingDialog(this);
  }

  public void hideLoading() {
    CommonUtils.hideLoadingDialog(mProgressDialog, this);
  }

  @Override
  public void onBackPressed() {
    getResponse()
        .onFacebookLogInFail(ERROR_USER_CANCELLED,
            getResources().getString(R.string.user_cancelled));
    finish();
  }
}