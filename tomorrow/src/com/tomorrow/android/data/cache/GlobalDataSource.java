package com.tomorrow.android.data.cache;

import android.content.Context;
import android.content.SharedPreferences;
import com.tomorrow.android.data.model.User;
import com.xengine.android.data.cache.XDataSource;

/**
 * Created by jasontujun on 14-7-13.
 */
public class GlobalDataSource  implements XDataSource {

    private static final String PREF_NAME = "tomorrow";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private SharedPreferences pref;

    public User mCurrentUser;


    /**
     * 全局状态数据源
     * @param context 请使用由getApplicationContext()获得的context
     */
    public GlobalDataSource(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User mCurrentUser) {
        this.mCurrentUser = mCurrentUser;
    }

    public void rememberUsernamePassword(String username,
                                         String password) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    /**
     * 返回当前登陆的用户的用户名
     */
    public String getRememberUserName() {
        return pref.getString(USERNAME, null);
    }

    /**
     * 返回当前用户使用的密码
     */
    public String getRememberPassword() {
        return pref.getString(PASSWORD, null);
    }


    @Override
    public String getSourceName() {
        return SourceName.GLOBAL_DATA;
    }
}
