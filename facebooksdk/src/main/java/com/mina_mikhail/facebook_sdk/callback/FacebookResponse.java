package com.mina_mikhail.facebook_sdk.callback;

import com.mina_mikhail.facebook_sdk.data.model.FacebookUser;

public interface FacebookResponse {

  void onLogInSuccess(FacebookUser facebookUser);

  void onLogInFail(int errorType, String errorMsg);
}