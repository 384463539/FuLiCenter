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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.UserAvatar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    View view;
    UserAvatar userAvatar;
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

    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person, container, false);
        Intent intent = getActivity().getIntent();
        userAvatar = (UserAvatar) intent.getSerializableExtra("userAvatar");
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.person_fragment_iv_sz, R.id.person_fragment_tv_sz, R.id.person_fragment_iv_user, R.id.person_fragment_tv_num1, R.id.person_fragment_tv_num2, R.id.person_fragment_tv_num3, R.id.person_fragment_layout_buy, R.id.person_fragment_tv_pay, R.id.person_fragment_tv_hair, R.id.person_fragment_tv_get, R.id.person_fragment_tv_evaluation, R.id.person_fragment_tv_after, R.id.person_fragment_layout_card, R.id.person_fragment_layout_life, R.id.person_fragment_layout_store, R.id.person_fragment_layout_vip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_fragment_iv_sz:
                FuLiCenterApplication.setUser(null);
                break;
            case R.id.person_fragment_tv_sz:
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
