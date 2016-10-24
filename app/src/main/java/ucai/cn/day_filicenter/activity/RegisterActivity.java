package ucai.cn.day_filicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.Result;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.MD5;
import ucai.cn.day_filicenter.utils.MFGT;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.register_iv_back)
    ImageView registerIvBack;
    @Bind(R.id.register_name)
    EditText registerName;
    @Bind(R.id.register_nick)
    EditText registerNick;
    @Bind(R.id.register_password)
    EditText registerPassword;
    @Bind(R.id.register_okparssword)
    EditText registerOkparssword;
    @Bind(R.id.register_ok)
    Button registerOk;

    String name, nick, password, okpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    private void initData() {
        name = registerName.getText().toString().trim();
        nick = registerNick.getText().toString().trim();
        password = registerPassword.getText().toString().trim();
        okpassword = registerOkparssword.getText().toString().trim();
    }

    @OnClick({R.id.register_ok, R.id.register_iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_ok:
                initData();
                if (name == null || name.length() < 6 || name.length() > 16 || !name.matches("[a-zA-Z]{1}[a-zA-Z0-9]{5,16}")) {
                    Toast.makeText(RegisterActivity.this, "请输入规定的账户名", Toast.LENGTH_SHORT).show();
                    registerName.setError("!");
                    return;
                }
                if (nick == null || nick.length() < 0 || nick.length() > 10) {
                    Toast.makeText(RegisterActivity.this, "请输入规定的昵称", Toast.LENGTH_SHORT).show();
                    registerNick.setError("!");
                    return;
                }
                if (password == null || password.length() < 0 || password.length() > 10) {
                    Toast.makeText(RegisterActivity.this, "请输入规定的密码", Toast.LENGTH_SHORT).show();
                    registerPassword.setError("!");
                    return;
                }
                if (okpassword == null || !okpassword.equals(password)) {
                    Toast.makeText(RegisterActivity.this, "密码不匹配", Toast.LENGTH_SHORT).show();
                    registerOkparssword.setError("!");
                    return;
                }
                initLogin();
                break;
            case R.id.register_iv_back:
                MFGT.finish(RegisterActivity.this);
                break;
        }
    }

    private void initLogin() {
        OkHttpUtils<Result> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_REGISTER).post()
                .addParam(I.User.USER_NAME, name)
                .addParam(I.User.NICK, nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        L.e(result.toString());
                        int retCode = result.getRetCode();
                        switch (retCode) {
                            case 0:
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("name", name);
                                intent.putExtra("passwrod", password);
                                setResult(1, intent);
                                MFGT.finish(RegisterActivity.this);
                                break;
                            case I.MSG_REGISTER_USERNAME_EXISTS:
                                Toast.makeText(RegisterActivity.this, "账号已经存在", Toast.LENGTH_SHORT).show();
                                break;
                            case I.MSG_REGISTER_FAIL:
                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(RegisterActivity.this, "服务器未响应", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
