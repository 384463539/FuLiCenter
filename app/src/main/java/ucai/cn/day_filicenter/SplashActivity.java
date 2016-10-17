package ucai.cn.day_filicenter;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
                MFGT.startActivity(SplashActivity.this, MainActivity.class);
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}