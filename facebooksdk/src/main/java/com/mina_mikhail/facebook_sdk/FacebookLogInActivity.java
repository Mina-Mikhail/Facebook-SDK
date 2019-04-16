package com.mina_mikhail.facebook_sdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mina_mikhail.FacebookData;
import com.mina_mikhail.facebook_sdk.callback.FacebookResponse;
import com.mina_mikhail.facebook_sdk.data.enums.ErrorCode;
import com.mina_mikhail.facebook_sdk.data.model.FacebookUser;
import com.mina_mikhail.facebook_sdk.utils.CommonUtils;
import com.mina_mikhail.facebook_sdk.utils.NetworkUtils;
import java.util.List;
import org.json.JSONObject;

/*
 * *
 *  * Created by Mina Mikhail on 16/04/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
 */

public class FacebookLogInActivity
    extends AppCompatActivity
    implements FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

  private static final String PROFILE_PIC_URL =
      "https://graph.facebook.com/%1$s/picture?type=large";

  private CallbackManager callbackManager;
  private ProgressDialog mProgressDialog;

  public static void start(Context context) {
    Intent intent = new Intent(context, FacebookLogInActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (!NetworkUtils.isNetworkConnected(this)) {
      getResponse().onFbSignInFail(ErrorCode.ERROR_NO_INTERNET,
          getResources().getString(R.string.no_internet));
      finish();
    } else {
      supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

      callbackManager = CallbackManager.Factory.create();
      LoginManager.getInstance().registerCallback(callbackManager, this);
      LoginManager.getInstance().logInWithReadPermissions(this, getScopes());
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  protected FacebookData getAuthData() {
    return Facebook.getInstance().getFacebookData();
  }

  @Override
  public void onSuccess(LoginResult loginResult) {
    showLoading();

    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,email,link,gender,birthday");
    request.setParameters(parameters);
    request.executeAsync();
  }

  @Override
  public void onCancel() {
    getResponse().onFbSignInFail(ErrorCode.ERROR_USER_CANCELLED,
        getResources().getString(R.string.user_cancelled));
    finish();
  }

  @Override
  public void onError(FacebookException error) {
    getResponse().onFbSignInFail(ErrorCode.ERROR_OTHER,
        getResources().getString(R.string.some_error));

    if (error instanceof FacebookAuthorizationException) {
      logOut();
    }

    finish();
  }

  @Override
  public void onCompleted(JSONObject object, GraphResponse response) {
    FacebookUser user = new FacebookUser();
    user.setUserID(object.optString("id"));
    user.setAccessToken(AccessToken.getCurrentAccessToken().getToken());
    user.setUserImage(String.format(PROFILE_PIC_URL, user.getUserID()));
    user.setUserEmail(object.optString("email"));
    user.setFullName(object.optString("name"));

    new Handler().postDelayed(() -> {
      logOut();
      hideLoading();
      getAuthData().getResponse().onFbSignInSuccess(user);
      finish();
    }, 1000);
  }

  private void logOut() {
    LoginManager.getInstance().logOut();
  }

  private List<String> getScopes() {
    return getAuthData().getScopes();
  }

  private FacebookResponse getResponse() {
    return getAuthData().getResponse();
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
    getResponse().onFbSignInFail(ErrorCode.ERROR_USER_CANCELLED,
        getResources().getString(R.string.user_cancelled));
    finish();
  }
}