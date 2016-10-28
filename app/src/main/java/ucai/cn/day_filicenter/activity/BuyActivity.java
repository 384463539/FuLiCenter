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

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.CartBean;
import ucai.cn.day_filicenter.bean.MessageBean;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

public class BuyActivity extends AppCompatActivity implements PaymentHandler {
    private static String URL = "http://218.244.151.190/demo/charge";
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

        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});
        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
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
                if (buyEtName.getText().toString().equals("") && buyEtName.getText().toString().length() <= 0) {
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
                // 产生个订单号
                String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                        .format(new Date());

                // 构建账单json对象
                JSONObject bill = new JSONObject();

                // 自定义的额外信息 选填
                JSONObject extras = new JSONObject();
                try {
                    extras.put("extra1", "extra1");
                    extras.put("extra2", "extra2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    bill.put("order_no", orderNo);
                    bill.put("amount", ringprice * 100);
                    bill.put("extras", extras);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //壹收款: 创建支付通道的对话框
                PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);

                break;
        }
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {
            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果
            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    PingppLog.d("result::" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            int resultcode = data.getExtras().getInt("code");
            switch (resultcode) {
                case 1:
                    paySuccess();
                    break;
                case 2:
                    finish();
                    break;
            }
        }
    }

    private void paySuccess() {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(this);
        for (CartBean cb2 : cartlist) {
            utils.setRequestUrl(I.REQUEST_DELETE_CART)
                    .addParam(I.Cart.ID, cb2.getId() + "")
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null) {
                            }
                        }
                        @Override
                        public void onError(String error) {
                        }
                    });
        }
    }

}
