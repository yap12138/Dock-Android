package com.yaphets.dock.model;

public class UserInfo implements Accessible {

	private String mEmail;
	private String mPassword;
	private String mNickname;

	public UserInfo (String email, String password) {
		this.mEmail = email;
		this.mPassword = password;
	}

	public UserInfo (String email, String password, String nickname) {
		this(email, password);
		this.mNickname = nickname;
	}

	public String getEmail() { return mEmail; }

	public String getPassword() {
		return mPassword;
	}

	public String getNickname() {
		return mNickname;
	}
}
