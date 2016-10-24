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
                if (user == null) {
                    UserDao userDao = new UserDao(SplashActivity.this);
                    SharedPreferences sharedPreferences = getSharedPreferences("loginname", MODE_PRIVATE);
                    user = userDao.getUser(sharedPreferences.getString("name", ""));
                    L.i("数据库" + user.toString());
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
