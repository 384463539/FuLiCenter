package ucai.cn.day_filicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.MainActivity;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.AlbumsBean;
import ucai.cn.day_filicenter.bean.GoodsDetailsBean;
import ucai.cn.day_filicenter.bean.MessageBean;
import ucai.cn.day_filicenter.bean.PropertiesBean;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.MFGT;
import ucai.cn.day_filicenter.utils.OkHttpUtils;
import ucai.cn.day_filicenter.views.FlowIndicator;

public class GoodsDetailsActivity extends AppCompatActivity {
    TextView details_tv_wname, details_tv_price, details_tv_cname, details_tv_brief;
    ImageView iv;
    FlowIndicator indicator;
    GestureDetector mgestureDetector;
    GoodsDetailsBean goodsDetailsBean;
    AlbumsBean[] aArr;
    Handler mainhandler;
    MyGesture myGesture;
    Thread mythread;
    Handler workhandler;
    boolean stop = false;
    int a = 0;
    PropertiesBean propertiesBean;
    boolean isCollect = false;

    @Bind(R.id.goods_iv_back)
    ImageView goodsIvBack;
    @Bind(R.id.goods_rb_add_cart)
    ImageView goodsRbAddCart;
    @Bind(R.id.goods_rb_add_collect)
    ImageView goodsRbAddCollect;
    @Bind(R.id.goods_rb_share)
    ImageView goodsRbShare;
    @Bind(R.id.details_tv_wname)
    TextView detailsTvWname;
    @Bind(R.id.details_tv_price)
    TextView detailsTvPrice;
    @Bind(R.id.details_zv_cator)
    FlowIndicator detailsZvCator;
    @Bind(R.id.details_tv_brief)
    TextView detailsTvBrief;
    int goodsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollect();
    }

    private void setThread() {
        if (mythread == null) {
            mythread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    workhandler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            runFor();
                            return false;
                        }
                    });
                    runFor();
                    Looper.loop();
                }

                public void runFor() {
                    for (int i = a + 1; i <= aArr.length && !stop; i++) {
                        SystemClock.sleep(800);
                        Message message = Message.obtain();
                        message.arg1 = i - 1;
                        mainhandler.sendMessage(message);
                        if (i == aArr.length) {
                            i = 0;
                        }
                    }
                }
            });
        }
        if (aArr.length > 1) {
            mythread.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mgestureDetector.onTouchEvent(event);
    }

    private void initData() {
        goodsid = getIntent().getIntExtra("goodsid", 7677);
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Goods.KEY_GOODS_ID, goodsid + "")
                .targetClass(GoodsDetailsBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
                    @Override
                    public void onSuccess(GoodsDetailsBean result) {
                        goodsDetailsBean = result;
                        details_tv_cname.setText(result.getGoodsName());
                        details_tv_wname.setText(result.getGoodsEnglishName());
                        details_tv_price.setText(result.getCurrencyPrice());
                        details_tv_brief.setText(result.getGoodsBrief());

                        PropertiesBean[] pArr = result.getProperties();
                        propertiesBean = pArr[0];
                        aArr = propertiesBean.getAlbums();
                        indicator.setCount(aArr.length);
                        initInMager(aArr[0].getImgUrl());
                        setThread();
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }

    public void initInMager(String url) {
        ImageLoader.build(I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE)
                .addParam(I.IMAGE_URL, url)
                .imageView(iv)
                .showImage(this);
    }

    private void initView() {
        iv = (ImageView) findViewById(R.id.details_iv);
        details_tv_cname = (TextView) findViewById(R.id.details_tv_cname);
        details_tv_wname = (TextView) findViewById(R.id.details_tv_wname);
        details_tv_brief = (TextView) findViewById(R.id.details_tv_brief);
        details_tv_price = (TextView) findViewById(R.id.details_tv_price);
        indicator = (FlowIndicator) findViewById(R.id.details_zv_cator);
        myGesture = new MyGesture();
        mgestureDetector = new GestureDetector(this, myGesture);
        mainhandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                myGesture.setView(msg.arg1);
                return false;
            }
        });
    }

    @OnClick({R.id.goods_iv_back, R.id.goods_rb_add_cart, R.id.goods_rb_add_collect, R.id.goods_rb_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_iv_back:
                MFGT.finish(this);
                break;
            case R.id.goods_rb_add_cart:
                break;
            case R.id.goods_rb_add_collect:
                UserAvatar user = FuLiCenterApplication.getUser();
                if (user != null) {
                    if (isCollect) {
                        deleteCollect(user);
                    } else {
                        addCollect(user);
                    }
                } else {
                    startActivity(new Intent(GoodsDetailsActivity.this, MainActivity.class));
                }
                break;
            case R.id.goods_rb_share:
                showShare();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    public void deleteCollect(UserAvatar user) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Cart.USER_NAME, user.getMuserName())
                .addParam(I.Cart.GOODS_ID, goodsid + "")
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect = false;
                            Toast.makeText(GoodsDetailsActivity.this, "删除收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            isCollect = true;
                        }
                        updataCollect();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    public void addCollect(UserAvatar user) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                .addParam(I.Cart.USER_NAME, user.getMuserName())
                .addParam(I.Cart.GOODS_ID, goodsid + "")
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect = true;
                            Toast.makeText(GoodsDetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            isCollect = false;
                        }
                        updataCollect();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    public void isCollect() {
        UserAvatar user = FuLiCenterApplication.getUser();
        if (user != null) {
            OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(this);
            utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                    .addParam(I.Cart.USER_NAME, user.getMuserName())
                    .addParam(I.Cart.GOODS_ID, goodsid + "")
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                isCollect = true;
                            } else {
                                isCollect = false;
                            }
                            updataCollect();
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }

    private void updataCollect() {
        if (isCollect) {
            goodsRbAddCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            goodsRbAddCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    class MyGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            if (aArr.length > 1) {
                stop = true;
            }
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() > e2.getX() && Math.abs(velocityX) > 30) {
                if (a != 0) {
                    a--;
                    setView(a);
                } else {
                    a = aArr.length - 1;
                    setView(a);
                }
            } else if (e1.getX() < e2.getX() && Math.abs(velocityX) > 30) {
                if (a != aArr.length - 1) {
                    a++;
                    setView(a);
                } else {
                    a = 0;
                    setView(a);
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        public void setView(int n) {
            indicator.setFocus(n);
            initInMager(aArr[n].getImgUrl());
        }
    }
}
