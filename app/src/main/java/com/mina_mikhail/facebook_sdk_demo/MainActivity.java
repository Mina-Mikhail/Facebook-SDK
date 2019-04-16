package com.mina_mikhail.facebook_sdk_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.mina_mikhail.facebook_sdk.Facebook;
import com.mina_mikhail.facebook_sdk.callback.FacebookResponse;
import com.mina_mikhail.facebook_sdk.data.model.FacebookUser;

public class MainActivity
    extends AppCompatActivity
    implements FacebookResponse {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.log_in_btn).setOnClickListener(
        v -> Facebook.getInstance().connect(MainActivity.this, this));
  }

  @Override
  public void onFbSignInSuccess(FacebookUser facebookUser) {
    Toast.makeText(this, facebookUser.getFullName(), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onFbSignInFail(int errorType, String errorMsg) {
    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
  }
}
