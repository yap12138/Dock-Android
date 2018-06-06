package com.yaphets.dock.model.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.yaphets.dock.DockApplication;
import com.yaphets.dock.R;
import com.yaphets.dock.model.Accessible;
import com.yaphets.dock.util.PhotoUtils;

public class UserInfo implements Accessible {

    private static UserInfo mUserInfo;

	private String email;
	private String password;

	private String nickname;

	private byte[] thumb;

	private Bitmap thumbBitmap;

	private UserInfo() {

	}

	private UserInfo (String email, String password) {
		this.email = email;
		this.password = password;
	}

	private UserInfo (String email, String password, String nickname) {
		this(email, password);
		this.nickname = nickname;
	}

	private UserInfo(String email, String password, String nickname, byte[] thumb) {
		this(email, password, nickname);
		this.thumb = thumb;
	}

	/**
	 * singleton
	 * @return
	 * global instance
	 */
	public static UserInfo getInstance() {
	    if (mUserInfo == null) {
	        mUserInfo = new UserInfo();
        }
        return mUserInfo;
    }

	/**
	 * temp instance
	 * @param email email
	 * @param password password
	 * @param nickname nickname
	 * @return temp instance
	 */
    @NonNull
    public static UserInfo createInstance(String email, String password, String nickname) {
	    return new UserInfo(email, password, nickname);
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public byte[] getThumb() {
		return thumb;
	}

	public void setThumb(byte[] thumb) {
		this.thumb = thumb;
	}

	public Bitmap getThumbBitmap() {
		if (thumbBitmap == null) {
			if (thumb == null || thumb.length == 0) {
				this.thumbBitmap = BitmapFactory.decodeResource(DockApplication.getContext().getResources(), R.drawable.default_thumb);
			} else {
				this.thumbBitmap = PhotoUtils.getBitmap(thumb);
			}
		}
		return thumbBitmap;
	}

	public void setThumbBitmap(Bitmap bitmap) {
    	this.thumbBitmap = bitmap;
    	this.thumb = PhotoUtils.getBytes(bitmap);
	}
}
