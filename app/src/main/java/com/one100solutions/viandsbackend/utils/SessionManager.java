package com.one100solutions.viandsbackend.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.one100solutions.viandsbackend.activities.MainActivity;
import com.one100solutions.viandsbackend.objects.UserObject;

/**
 * Created by sujith on 10/3/15.
 */
public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Viands";
    private static final String IS_LOGIN = "isLoggedIn";


    public static final String KEY_NAME = "name";

    public static final String KEY_TOKEN = "token";
    public static final String KEY_GCM_SENDER_ID = "gcmid";
    public static final String KEY_GCM_APP_VERSION = "appversion";

    public SessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(UserObject user) {

        editor.putBoolean(IS_LOGIN, true);


        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_TOKEN, user.getToken());

        // commit changes
        editor.commit();

        // delete any temporary databases - CartSQLite
        this.mContext.deleteDatabase(DatabaseConstants.DATABASE_CART);
    }


    public UserObject getUserDetails() {
        UserObject user = new UserObject();

        user.setName(pref.getString(KEY_NAME, null));
        user.setToken(pref.getString(KEY_TOKEN, null));

        return user;
    }


    public void logoutUser() {

        editor.clear();
        editor.commit();

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        ((Activity) mContext).finish();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void storeGCMSenderId(String gcmSenderId) {
        editor.putString(KEY_GCM_SENDER_ID, gcmSenderId);
        editor.commit();
    }

    public void storeAppVersion(int appVersion) {
        editor.putInt(KEY_GCM_APP_VERSION, appVersion);
        editor.commit();
    }

    public String getGCMSenderId() {
        return pref.getString(KEY_GCM_SENDER_ID, "");
    }

    public int getAppVersion() {
        return pref.getInt(KEY_GCM_APP_VERSION, Integer.MIN_VALUE);
    }


}
