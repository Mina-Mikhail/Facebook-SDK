package com.mina_mikhail.facebook_sdk.data.model;

/*
 * *
 *  * Created by Mina Mikhail on 16/04/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
 */

import android.os.Parcel;
import android.os.Parcelable;

public class FacebookUser
    implements Parcelable {

  private String accessToken;
  private String userID;
  private String fullName;
  private String userEmail;
  private String userImage;

  public FacebookUser() {
  }

  private FacebookUser(Parcel in) {
    accessToken = in.readString();
    userID = in.readString();
    fullName = in.readString();
    userEmail = in.readString();
    userImage = in.readString();
  }

  public static final Creator<FacebookUser> CREATOR = new Creator<FacebookUser>() {
    @Override
    public FacebookUser createFromParcel(Parcel in) {
      return new FacebookUser(in);
    }

    @Override
    public FacebookUser[] newArray(int size) {
      return new FacebookUser[size];
    }
  };

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserImage() {
    return userImage;
  }

  public void setUserImage(String userImage) {
    this.userImage = userImage;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public static Creator<FacebookUser> getCREATOR() {
    return CREATOR;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(accessToken);
    dest.writeString(userID);
    dest.writeString(fullName);
    dest.writeString(userEmail);
    dest.writeString(userImage);
  }
}