package com.mina_mikhail;

import com.mina_mikhail.facebook_sdk.callback.FacebookResponse;
import java.util.ArrayList;
import java.util.List;

public class FacebookData {

  private List<String> scopes;
  private FacebookResponse response;

  public FacebookData(List<String> scopes, FacebookResponse response) {
    this.scopes = new ArrayList<>(scopes);
    this.response = response;
  }

  public List<String> getScopes() {
    return scopes;
  }

  public FacebookResponse getResponse() {
    return response;
  }
}
