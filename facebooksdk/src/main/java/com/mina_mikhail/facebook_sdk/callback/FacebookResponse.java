package com.mina_mikhail.facebook_sdk.callback;

import com.mina_mikhail.facebook_sdk.data.model.FacebookUser;

public interface FacebookResponse {

  void onFbSignInSuccess(FacebookUser facebookUser);

  void onFbSignInFail(int errorType, String errorMsg);
}