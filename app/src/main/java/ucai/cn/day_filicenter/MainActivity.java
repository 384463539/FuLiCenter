package ucai.cn.day_filicenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.activity.HomeActivity;
import ucai.cn.day_filicenter.activity.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_av_name)
    EditText mainAvName;
    @Bind(R.id.main_av_password)
    EditText mainAvPassword;
    @Bind(R.id.main_btn_login)
    Button mainBtnLogin;
    @Bind(R.id.main_tv_z)
    TextView mainTvZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void btniv(View view) {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }

    @OnClick({R.id.main_btn_login, R.id.main_tv_z})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_login:
                break;
            case R.id.main_tv_z:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    mainAvName.setText(data.getStringExtra("name"));
                    mainAvPassword.setText(data.getStringExtra("passwrod"));
                }
                break;
        }
    }
}
