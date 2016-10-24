package ucai.cn.day_filicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.utils.ImageLoader;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        UserAvatar user = FuLiCenterApplication.getUser();
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

    @OnClick({R.id.user_av_back, R.id.user_layout2_user, R.id.user_layout3_nick, R.id.user_layout4_ew, R.id.user_bt_nologin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_av_back:
                finish();
                break;
            case R.id.user_layout2_user:
                break;
            case R.id.user_layout3_nick:
                break;
            case R.id.user_layout4_ew:

                break;
            case R.id.user_bt_nologin:
                FuLiCenterApplication.setUser(null);
                break;
        }
    }
}
