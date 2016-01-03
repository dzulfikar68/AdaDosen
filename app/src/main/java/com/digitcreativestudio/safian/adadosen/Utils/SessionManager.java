package com.digitcreativestudio.safian.adadosen.Utils;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.digitcreativestudio.safian.adadosen.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AdaDosenPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_NIM = "name";
    public static final String KEY_NAME = "name";

    public static final String KEY_SYNC = "isSync";
    public static final String KEY_TOKEN = "token";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String nim, String name){
        String[] nameArray = name.split("\\s+");
        String newName = "";
        for(int i = 0; i < nameArray.length; i++){
            if(i>1){
                newName += " "+nameArray[i].charAt(0)+".";
            }else{
                newName += " "+nameArray[i];
            }
        }
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_ID, id);

        editor.putString(KEY_NIM, nim);

        editor.putString(KEY_NAME, newName);

        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_NIM, pref.getString(KEY_NIM, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        return user;
    }

    public void logoutUser(){
        editor.remove(IS_LOGIN);
        editor.remove(KEY_ID);
        editor.remove(KEY_NIM);
        editor.remove(KEY_NAME);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setRegistered(boolean registered){
        editor.remove(SENT_TOKEN_TO_SERVER);
        editor.putBoolean(SENT_TOKEN_TO_SERVER, registered);
        editor.commit();
    }

    public boolean isSync(){
        return pref.getBoolean(KEY_SYNC, false);
    }

    public void setIsSync(boolean isSync){
        editor.remove(KEY_SYNC);
        editor.putBoolean(KEY_SYNC, isSync);
        editor.commit();
    }

    public void setSentToken(boolean sent){
        editor.remove(SENT_TOKEN_TO_SERVER);
        editor.putBoolean(SENT_TOKEN_TO_SERVER, sent);
        editor.commit();
    }

    public boolean isTokenSent(){
        return pref.getBoolean(SENT_TOKEN_TO_SERVER, false);
    }

    public void setToken(String token){
        editor.remove(KEY_TOKEN);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public String getToken(){
        return pref.getString(KEY_TOKEN, "");
    }
}
