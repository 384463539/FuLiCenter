package ucai.cn.day_filicenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.activity.RegisterActivity;
import ucai.cn.day_filicenter.bean.Result;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.dao.UserDao;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.MD5;
import ucai.cn.day_filicenter.utils.OkHttpUtils;
import ucai.cn.day_filicenter.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_av_name)
    EditText mainAvName;
    @Bind(R.id.main_av_password)
    EditText mainAvPassword;
    @Bind(R.id.main_btn_login)
    Button mainBtnLogin;
    @Bind(R.id.main_tv_z)
    TextView mainTvZ;

    String name, password;
    @Bind(R.id.title_layout)
    LinearLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mainAvName.setText(SharedPreferencesUtils.getInstance(this).getName());
    }

    @OnClick({R.id.main_btn_login, R.id.main_tv_z})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_login:
                name = mainAvName.getText().toString().trim();
                password = mainAvPassword.getText().toString().trim();
                if (name.length() > 0) {
                    SharedPreferencesUtils.getInstance(this).setName(name);
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    mainAvName.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    mainAvPassword.requestFocus();
                    return;
                }
                login();
                break;
            case R.id.main_tv_z:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    private void login() {
        OkHttpUtils<Result> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME, name)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        String json = result.getRetData().toString();
                        Gson gson = new Gson();
                        UserAvatar userAvatar = gson.fromJson(json, UserAvatar.class);
                        if (result == null) {
                            Toast.makeText(MainActivity.this, "未取得数据", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (result.getRetCode() == 0) {
                            FuLiCenterApplication.setUser(userAvatar);
                            UserDao userDao = new UserDao(MainActivity.this);
                            userDao.savaUser(userAvatar);
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(10, intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, "服务器未响应", Toast.LENGTH_SHORT).show();
                    }
                });
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

    @OnClick(R.id.title_layout)
    public void onClick() {
        finish();
    }
}
