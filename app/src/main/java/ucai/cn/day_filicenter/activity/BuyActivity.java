package ucai.cn.day_filicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.CartBean;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

public class BuyActivity extends AppCompatActivity {
    ArrayList<CartBean> cartlist;
    ArrayList<CartBean> newlist;
    int ringprice = 0;
    int sum = 0;
    @Bind(R.id.buy_av_back)
    ImageView buyAvBack;
    @Bind(R.id.buy_et_name)
    EditText buyEtName;
    @Bind(R.id.buy_et_phone)
    EditText buyEtPhone;
    @Bind(R.id.buy_et_dz)
    EditText buyEtDz;
    @Bind(R.id.buy_et_price)
    TextView buyEtPrice;
    @Bind(R.id.buy_bt_ok)
    Button buyBtOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        cartlist = (ArrayList) intent.getSerializableExtra("cartlist");
        sum = intent.getIntExtra("sumprince", 0);
        findCart();
    }

    private void initData() {
        sunPrice();
        buyEtPrice.setText("支付：" + ringprice);
    }

    private void findCart() {
        UserAvatar user = FuLiCenterApplication.getUser();
        final OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME, user.getMuserName())
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        ArrayList<CartBean> list = utils.array2List(result);
                        newlist = list;
                        initData();
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }

    public void sunPrice() {
        if (FuLiCenterApplication.getUser() != null) {
            for (CartBean c : newlist) {
                for (CartBean cb : cartlist) {
                    if (c.getGoodsId() == cb.getGoodsId()) {
                        ringprice += c.getCount() * Integer.parseInt(c.getGoods().getRankPrice().substring(1));
                    }
                }
            }
        }
    }

    @OnClick({R.id.buy_av_back, R.id.buy_bt_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buy_av_back:
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.buy_bt_ok:
                if (buyEtName.getText().toString().equals("") && buyEtName.getText().toString().length() <= 6 && buyEtName.getText().toString().matches("[a-zA-Z]{1}[a-zA-Z0-9]{5,16}")) {
                    Toast.makeText(BuyActivity.this, "请输入正确的账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (buyEtPhone.getText().toString().equals("") && buyEtPhone.getText().toString().length() <= 0) {
                    Toast.makeText(BuyActivity.this, "请输入号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (buyEtDz.getText().toString().equals("") && buyEtDz.getText().toString().length() <= 0) {
                    Toast.makeText(BuyActivity.this, "请输地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sum != ringprice) {
                    Toast.makeText(BuyActivity.this, "支付价格异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(BuyActivity.this, "ok", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
