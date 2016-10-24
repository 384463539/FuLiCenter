package ucai.cn.day_filicenter.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SharedPreferencesUtils {
    static final String KEY = "name";
    static final String S_NAME = "loginname";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static SharedPreferencesUtils preferencesUtils;

    public SharedPreferencesUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(S_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesUtils getInstance(Context context) {
        if (preferencesUtils == null) {
            preferencesUtils = new SharedPreferencesUtils(context);
        }
        return preferencesUtils;
    }

    public void setName(String name) {
        editor.putString(KEY, name);
        editor.commit();
    }

    public String getName() {
        return sharedPreferences.getString(KEY, "");
    }
}
