package com.mina_mikhail.facebook_sdk.callback;

import com.mina_mikhail.facebook_sdk.data.model.FacebookUser;

public interface FacebookResponse {

  void onFacebookLogInSuccess(FacebookUser facebookUser);

  void onFacebookLogInFail(int errorType, String errorMsg);
}