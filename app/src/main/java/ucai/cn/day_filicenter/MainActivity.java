package ucai.cn.day_filicenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ucai.cn.day_filicenter.activity.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btniv(View view) {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }
}
