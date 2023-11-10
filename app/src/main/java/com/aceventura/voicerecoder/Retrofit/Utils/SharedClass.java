package com.aceventura.voicerecoder.Retrofit.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedClass {
    Context mContext;
    private static SharedClass mInstance;
    private  static SharedPreferences preferences;

    public SharedClass(Context mContext) {
        super();
        this.mContext = mContext;
    }

//    private static final String KEY_USER_NAME = "logname";
    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_EMP_NAME = "isLoggedIn";
    private static final String KEY_USER_ID = "emp_id";
    static final String KEY_EMP_CODE = "emp_code";
    public static final String KEY_REPORTTO = "reportingTo";
    public static final String KEY_DEPARTMENT = "department";
    public static final String KEY_ROLE = "role";
    public static final String LOGGED_IN = "logged_in_status";

    public static synchronized SharedClass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedClass(context);
            preferences=context.getSharedPreferences("userinfo3",Context.MODE_PRIVATE);
        }
        return mInstance;
    }

    public void setString(String key,String value){
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key){
        return preferences.getString(key,"");
    }


   public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null) != null;
    }

    public boolean userLogin(String emp_id, String emp_code, String emp_name, String reportingTo, String department, String role) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, emp_id);
        editor.putInt(KEY_EMP_CODE, Integer.parseInt(emp_code));
        editor.putString(KEY_EMP_NAME, emp_name);
        editor.putString(KEY_REPORTTO, reportingTo);
        editor.putString(KEY_DEPARTMENT, department);
        editor.putString(KEY_ROLE, role);
        editor.apply();
//
        return true;
    }
}
