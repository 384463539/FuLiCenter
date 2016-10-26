package ucai.cn.day_filicenter.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.MainActivity;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.activity.CollectActivity;
import ucai.cn.day_filicenter.activity.UserActivity;
import ucai.cn.day_filicenter.bean.MessageBean;
import ucai.cn.day_filicenter.bean.Result;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.dao.UserDao;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    View view;
    @Bind(R.id.person_fragment_iv_sz)
    ImageView personFragmentIvSz;
    @Bind(R.id.person_fragment_tv_sz)
    TextView personFragmentTvSz;
    @Bind(R.id.person_fragment_iv_user)
    ImageView personFragmentIvUser;
    @Bind(R.id.person_fragment_tv_nick)
    TextView personFragmentTvNick;
    @Bind(R.id.person_fragment_iv_erw)
    ImageView personFragmentIvErw;
    @Bind(R.id.person_fragment_tv_num1)
    TextView personFragmentTvNum1;
    @Bind(R.id.person_fragment_tv_num2)
    TextView personFragmentTvNum2;
    @Bind(R.id.person_fragment_tv_num3)
    TextView personFragmentTvNum3;
    @Bind(R.id.person_fragment_layout_buy)
    RelativeLayout personFragmentLayoutBuy;
    @Bind(R.id.person_fragment_tv_pay)
    ImageView personFragmentTvPay;
    @Bind(R.id.person_fragment_tv_hair)
    ImageView personFragmentTvHair;
    @Bind(R.id.person_fragment_tv_get)
    ImageView personFragmentTvGet;
    @Bind(R.id.person_fragment_tv_evaluation)
    ImageView personFragmentTvEvaluation;
    @Bind(R.id.person_fragment_tv_after)
    ImageView personFragmentTvAfter;
    @Bind(R.id.person_fragment_layout_card)
    RelativeLayout personFragmentLayoutCard;
    @Bind(R.id.person_fragment_layout_life)
    RelativeLayout personFragmentLayoutLife;
    @Bind(R.id.person_fragment_layout_store)
    RelativeLayout personFragmentLayoutStore;
    @Bind(R.id.person_fragment_layout_vip)
    RelativeLayout personFragmentLayoutVip;

    UserAvatar user;

    public PersonFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        L.i("个人中心数据刷新");
        user = FuLiCenterApplication.getUser();
        if (user != null && user.getMuserName() != null) {
            setAvatar();
            OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(getActivity());
            utils.url(I.SERVER_ROOT + I.REQUEST_FIND_COLLECT_COUNT)
                    .addParam(I.Cart.USER_NAME, user.getMuserName())
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                personFragmentTvNum1.setText(result.getMsg());
                            } else {
                                personFragmentTvNum1.setText("0");
                            }
                        }

                        @Override
                        public void onError(String error) {
                        }
                    });
            updata(user.getMuserName());
        }
    }

    public void setAvatar() {
        personFragmentTvNick.setText(user.getMuserNick());
        String url = I.SERVER_ROOT + I.REQUEST_DOWNLOAD_AVATAR + "?" + I.NAME_OR_HXID + "=" + user.getMuserName()
                + I.AND + I.AVATAR_TYPE + "=" + user.getMavatarPath() + I.AND + I.AVATAR_SUFFIY + "=" + user.getMavatarSuffix()
                + I.AND + "width=200&height=200" + I.AND + user.getMavatarLastUpdateTime();
        ImageLoader.build(url)
                .imageView(personFragmentIvUser)
                .defaultPicture(R.drawable.icon_account)
                .showImage(getActivity());
        L.i("下载");
    }

    public void updata(String name) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>(getActivity());
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME, name)
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null) {
                            String json = result.getRetData().toString();
                            Gson gson = new Gson();
                            UserAvatar userAvatar = gson.fromJson(json, UserAvatar.class);
                            if (!userAvatar.equals(user)) {
                                UserDao userDao = new UserDao(getActivity());
                                boolean b = userDao.updataUser(userAvatar);
                                if (b) {
                                    FuLiCenterApplication.setUser(userAvatar);
                                    user = userAvatar;
                                    L.i("不同");
                                    setAvatar();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.person_fragment_layout_my, R.id.person_fragment_iv_sz, R.id.person_fragment_tv_sz, R.id.person_fragment_iv_user, R.id.person_fragment_tv_num1, R.id.person_fragment_tv_num2, R.id.person_fragment_tv_num3, R.id.person_fragment_layout_buy, R.id.person_fragment_tv_pay, R.id.person_fragment_tv_hair, R.id.person_fragment_tv_get, R.id.person_fragment_tv_evaluation, R.id.person_fragment_tv_after, R.id.person_fragment_layout_card, R.id.person_fragment_layout_life, R.id.person_fragment_layout_store, R.id.person_fragment_layout_vip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_fragment_layout_my:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.person_fragment_iv_sz:
                startActivity(new Intent(getActivity(), UserActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.person_fragment_tv_sz:
                startActivity(new Intent(getActivity(), UserActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.person_fragment_iv_user:
                break;
            case R.id.person_fragment_tv_num1:
                break;
            case R.id.person_fragment_tv_num2:
                break;
            case R.id.person_fragment_tv_num3:
                break;
            case R.id.person_fragment_layout_buy:
                break;
            case R.id.person_fragment_tv_pay:
                break;
            case R.id.person_fragment_tv_hair:
                break;
            case R.id.person_fragment_tv_get:
                break;
            case R.id.person_fragment_tv_evaluation:
                break;
            case R.id.person_fragment_tv_after:
                break;
            case R.id.person_fragment_layout_card:
                break;
            case R.id.person_fragment_layout_life:
                break;
            case R.id.person_fragment_layout_store:
                break;
            case R.id.person_fragment_layout_vip:
                break;
        }
    }
}
