package com.example.myapplication.salarytracker.SharedPreferences;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.salarytracker.UserDetails.Admin;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String SHARED_PREF_NAME = "session";
    public static final String NAME = "name", PASSWORD = "password", PHONENO = "phoneno", USERNAME = "username";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(Admin adminLoginData) {
        //save session whenever user is logged in...

        // Adding the credentials to Shared Preferences
        editor.putString(USERNAME, adminLoginData.getUsername());
        editor.putString(PASSWORD, adminLoginData.getPassword());
        editor.putString(PHONENO, adminLoginData.getPhoneno());
        editor.putString(NAME, adminLoginData.getName());

        editor.commit();

    }

    public Admin getSession() {
        // return roll number of user whose session is saved

        if(sharedPreferences.getString(USERNAME, "NA").compareTo("NA") == 0) return null;

        return new Admin(sharedPreferences.getString(NAME, "NA"),
                sharedPreferences.getString(USERNAME, "NA"),
                sharedPreferences.getString(PASSWORD, "NA"),
                sharedPreferences.getString(PHONENO, "NA"));
    }

    public void removeSession() {
        //editor.remove(ROLL_NO_OF_USER);
        editor.putString(USERNAME, "NA");
        editor.putString(PASSWORD, "NA");
        editor.putString(PHONENO, "NA");
        editor.putString(NAME, "NA");
        editor.apply();
    }

}

