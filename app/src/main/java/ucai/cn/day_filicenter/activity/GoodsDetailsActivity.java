package ucai.cn.day_filicenter.activity;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Map;

import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.AlbumsBean;
import ucai.cn.day_filicenter.bean.GoodsDetailsBean;
import ucai.cn.day_filicenter.bean.PropertiesBean;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.OkHttpUtils;
import ucai.cn.day_filicenter.views.FlowIndicator;

public class GoodsDetailsActivity extends AppCompatActivity {
    TextView details_tv_wname, details_tv_price, details_tv_cname, details_tv_brief;
    ImageView iv;
    FlowIndicator indicator;
    GestureDetector mgestureDetector;
    AlbumsBean[] aArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        initView();
        initData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mgestureDetector.onTouchEvent(event);
    }

    private void initData() {
        final int goodsid = getIntent().getIntExtra("goodsid", 7677);
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Goods.KEY_GOODS_ID, goodsid + "")
                .targetClass(GoodsDetailsBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
                    @Override
                    public void onSuccess(GoodsDetailsBean result) {
                        details_tv_cname.setText(result.getGoodsName());
                        details_tv_wname.setText(result.getGoodsEnglishName());
                        details_tv_price.setText(result.getShopPrice());
                        details_tv_brief.setText(result.getGoodsBrief());

                        PropertiesBean[] pArr = result.getProperties();
                        PropertiesBean propertiesBean = pArr[0];
                        aArr = propertiesBean.getAlbums();
                        indicator.setCount(aArr.length);
                        initInMager(aArr[0].getImgUrl());
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
        mgestureDetector = new GestureDetector(this, new MyGesture());
    }

    class MyGesture extends GestureDetector.SimpleOnGestureListener {
        int a = 0;

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
                    setView(a);;
                } else {
                    a = 0;
                    setView(a);
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
        public void setView(int n){
            indicator.setFocus(n);
            initInMager(aArr[n].getImgUrl());
        }
    }
}