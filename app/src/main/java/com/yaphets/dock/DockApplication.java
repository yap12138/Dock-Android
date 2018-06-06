package com.yaphets.dock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class DockApplication extends Application {

    private static Context _context;

    private SharedPreferences _loginShared;

    public static String _email;
    public static String _password;

    private static HashMap<String, Object> _attribute = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        _context = getApplicationContext();

        SharedPreferences shared = getLoginShared();
        _email = shared.getString(getString(R.string.const_email), null);
        _password = shared.getString(getString(R.string.const_password), null);
    }

    public static Context getContext(){
        return _context;
    }

    public static Object getAttribute(String key) {
        return _attribute.get(key);
    }

    public static void setAttribute(String key, Object value) {
        _attribute.put(key, value);
    }

    public SharedPreferences getLoginShared() {
        //根据lastest_login.xml中当前登陆成功的用户名，获取对应的<用户名>.xml
        SharedPreferences shared = getSharedPreferences("lastest_login", Context.MODE_PRIVATE);
        String email = shared.getString(getString(R.string.const_email),"");

        this._loginShared = getSharedPreferences(email, Context.MODE_PRIVATE);
        return  this._loginShared;
    }

    public void setLoginShared(String email) {
        SharedPreferences shared = this.getSharedPreferences("lastest_login", Context.MODE_PRIVATE);
        //保存登陆成功的用户名到lastest_login.xml中
        shared.edit().putString(getString(R.string.const_email), email).apply();
    }
}
