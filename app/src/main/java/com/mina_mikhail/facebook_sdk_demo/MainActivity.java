package com.mina_mikhail.facebook_sdk_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mina_mikhail.facebook_sdk.Facebook;
import com.mina_mikhail.facebook_sdk.callback.FacebookResponse;
import com.mina_mikhail.facebook_sdk.data.model.FacebookUser;
import com.squareup.picasso.Picasso;

public class MainActivity
    extends AppCompatActivity
    implements FacebookResponse {

  private View userData;
  private ImageView userPhoto;
  private TextView userID;
  private TextView userAccessToken;
  private TextView userName;
  private TextView userEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    userData = findViewById(R.id.ll_user_data);
    userPhoto = findViewById(R.id.iv_user_photo);
    userID = findViewById(R.id.tv_user_id);
    userAccessToken = findViewById(R.id.tv_user_access_token);
    userName = findViewById(R.id.tv_user_name);
    userEmail = findViewById(R.id.tv_user_email);

    findViewById(R.id.btn_log_in).setOnClickListener(
        v -> Facebook.getInstance().logIn(MainActivity.this, this));
  }

  @Override
  public void onFacebookLogInSuccess(FacebookUser facebookUser) {
    Picasso.get().load(facebookUser.getUserProfilePicture()).into(userPhoto);
    userID.setText(facebookUser.getUserID());
    userAccessToken.setText(facebookUser.getAccessToken());
    userName.setText(facebookUser.getFullName());
    userEmail.setText(facebookUser.getUserEmail());

    userData.setVisibility(View.VISIBLE);
  }

  @Override
  public void onFacebookLogInFail(int errorType, String errorMsg) {
    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
  }
}
