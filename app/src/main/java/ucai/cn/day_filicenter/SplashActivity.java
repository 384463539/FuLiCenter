package ucai.cn.day_filicenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ucai.cn.day_filicenter.activity.HomeActivity;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.dao.UserDao;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.MFGT;
import ucai.cn.day_filicenter.utils.SharedPreferencesUtils;

public class SplashActivity extends AppCompatActivity {
    long a = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long timeStart = System.currentTimeMillis();
                long time = System.currentTimeMillis() - timeStart;
                if (a - time > 0) {
                    try {
                        Thread.sleep(a - time);
                        Log.i("main", "ssss");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                UserAvatar user = FuLiCenterApplication.getUser();
                String username = SharedPreferencesUtils.getInstance(SplashActivity.this).getName();
                if (user == null && username != null) {
                    UserDao userDao = new UserDao(SplashActivity.this);
                    user = userDao.getUser(username);
                    L.i("数据库" + user.toString());
                    if (user != null) {
                        L.i("sss" + user.toString());
                        FuLiCenterApplication.setUser(user);
                    }
                }
                MFGT.startActivity(SplashActivity.this, HomeActivity.class);
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
