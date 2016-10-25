package ucai.cn.day_filicenter.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.MainActivity;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.Result;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.dao.UserDao;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.OkHttpUtils;
import ucai.cn.day_filicenter.utils.OnSetAvatarListener;
import ucai.cn.day_filicenter.utils.SharedPreferencesUtils;

public class UserActivity extends AppCompatActivity {

    @Bind(R.id.user_av_back)
    ImageView userAvBack;
    @Bind(R.id.user_layout1)
    LinearLayout userLayout1;
    @Bind(R.id.user_iv_user)
    ImageView userIvUser;
    @Bind(R.id.user_tv_nick)
    TextView userTvNick;
    @Bind(R.id.user_layout2_user)
    LinearLayout userLayout2User;
    @Bind(R.id.user_layout3_nick)
    RelativeLayout userLayout3Nick;
    @Bind(R.id.user_layout4_ew)
    RelativeLayout userLayout4Ew;
    @Bind(R.id.user_bt_nologin)
    Button userBtNologin;

    OnSetAvatarListener monSetAvatarListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        UserAvatar user = FuLiCenterApplication.getUser();
        if (user != null && user.getMuserName() != null) {
            userTvNick.setText(user.getMuserNick());
            String url = I.SERVER_ROOT + I.REQUEST_DOWNLOAD_AVATAR;
            ImageLoader.build(url)
                    .addParam(I.NAME_OR_HXID, user.getMuserName())
                    .addParam(I.AVATAR_TYPE, user.getMavatarPath())
                    .addParam(I.AVATAR_SUFFIY, user.getMavatarSuffix())
                    .addParam(I.AVATAR_WIETH, 200 + "")
                    .addParam(I.AVATAR_HEIGHT, 200 + "")
                    .imageView(userIvUser)
                    .defaultPicture(R.drawable.icon_account)
                    .showImage(this);
        }
    }

    @OnClick({R.id.user_av_back, R.id.user_layout2_user, R.id.user_layout3_nick, R.id.user_layout4_ew, R.id.user_bt_nologin, R.id.user_iv_user})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.user_iv_user:
                monSetAvatarListener = new OnSetAvatarListener(this, R.id.user_main_layout, FuLiCenterApplication.getUser().getMuserName(), I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.user_av_back:
                finish();
                break;
            case R.id.user_layout2_user:
                break;
            case R.id.user_layout3_nick:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View view1 = View.inflate(this, R.layout.nick_dialog, null);
                builder.setTitle("设置昵称")
                        .setView(view1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText ed = (EditText) view1.findViewById(R.id.nick_dialog);
                                String nick = ed.getText().toString();
                                UserDao userDao = new UserDao(UserActivity.this);
                                final UserAvatar user = FuLiCenterApplication.getUser();
                                user.setMuserNick(nick);
                                userDao.updataUser(user);
                                OkHttpUtils<Result> utils = new OkHttpUtils<Result>(UserActivity.this);
                                utils.url(I.SERVER_ROOT + I.REQUEST_UPDATE_USER_NICK)
                                        .addParam(I.User.NICK, nick)
                                        .addParam(I.User.USER_NAME, user.getMuserName())
                                        .targetClass(Result.class)
                                        .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                                            @Override
                                            public void onSuccess(Result result) {
                                                initData();
                                            }

                                            @Override
                                            public void onError(String error) {

                                            }
                                        });
                            }
                        });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
                break;
            case R.id.user_layout4_ew:

                break;
            case R.id.user_bt_nologin:
                SharedPreferencesUtils.getInstance(this).delectName();
                FuLiCenterApplication.setUser(null);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        monSetAvatarListener.setAvatar(requestCode, data, userIvUser);
        L.i(requestCode + " aaa " + OnSetAvatarListener.REQUEST_CROP_PHOTO);
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updataAvatar();
        }
    }

    private void updataAvatar() {
        UserAvatar user = FuLiCenterApplication.getUser();
        File file = new File(OnSetAvatarListener.getAvatarPath(this, user.getMavatarPath() + "/" + user.getMuserName() + ".jpg"));
        L.i("ss"+file.getAbsolutePath());
        OkHttpUtils<Result> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addFile2(file)
                .addParam(I.NAME_OR_HXID, user.getMuserName())
                .addParam(I.AVATAR_TYPE, I.AVATAR_TYPE_USER_PATH)
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        ImageLoader.release();
                        Toast.makeText(UserActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
