package com.Mayank.MML;

import android.content.Context;
import android.content.SharedPreferences;

public class Savedata {
	SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME 	= "TestPref";
//.....****......////
    public Savedata(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void save_user(String user) {

        editor.putString("user_id", user);
        editor.commit();
    }
    public String getuser() {
        return pref.getString("user_id","user_id");
    }

    public void clearwork() {
        editor.clear();
        editor.commit();
    }


}
