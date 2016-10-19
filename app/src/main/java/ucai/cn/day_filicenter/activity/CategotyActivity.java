package ucai.cn.day_filicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.NewGoodBean;
import ucai.cn.day_filicenter.fragment.MyAdapter;
import ucai.cn.day_filicenter.fragment.NewgoodFragment;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

public class CategotyActivity extends AppCompatActivity {
    ArrayList<NewGoodBean> myList;
    int id = 0;
    String name;
    MyAdapter myAdapter;
    RecyclerView rv;
    GridLayoutManager gridLayoutManager;
    int num = 1;
    SwipeRefreshLayout srl;
    TextView tv;

    NewgoodFragment newgoodFragment;
    @Bind(R.id.category_av_title)
    RelativeLayout categoryAvTitle;
    @Bind(R.id.category_av_tv1)
    TextView categoryAvTv1;
    @Bind(R.id.category_av_iv1)
    ImageView categoryAvIv1;
    @Bind(R.id.category_av_tv2)
    TextView categoryAvTv2;
    @Bind(R.id.category_av_iv2)
    ImageView categoryAvIv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoty);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 774);
        name = intent.getStringExtra("name");
        initView();
        initData(I.ACTION_DOWNLOAD, 1);
        setListener();
    }

    private void setListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int a = gridLayoutManager.findLastVisibleItemPosition();
                if (a >= myAdapter.getItemCount() - 1 && myAdapter.isMore && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    num++;
                    initData(I.ACTION_PULL_UP, num);
                }
            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                num = 1;
                tv.setVisibility(View.VISIBLE);
                srl.setRefreshing(true);
                srl.setEnabled(true);
                initData(I.ACTION_PULL_DOWN, num);
            }
        });
    }

    private void initView() {
        myList = new ArrayList<>();
        myAdapter = new MyAdapter(this, myList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rv = (RecyclerView) findViewById(R.id.category_av_rv);
        rv.setAdapter(myAdapter);
        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == myAdapter.getItemCount() - 1 ? I.COLUM_NUM : 1;
            }
        });
        rv.setLayoutManager(gridLayoutManager);

        srl = (SwipeRefreshLayout) findViewById(R.id.category_av_srl);
        tv = (TextView) findViewById(R.id.category_av_tvhead);
    }

    private void initData(final int type, int num) {
        final OkHttpUtils<NewGoodBean[]> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.GoodsDetails.KEY_CAT_ID, id + "")
                .addParam(I.PAGE_ID, num + "")
                .addParam(I.PAGE_SIZE, 4 + "")
                .targetClass(NewGoodBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<NewGoodBean[]>() {
                    @Override
                    public void onSuccess(NewGoodBean[] result) {
                        myAdapter.isMore = result != null && result.length > 0;
                        if (!myAdapter.isMore) {
                            if (type == I.ACTION_PULL_UP) {
                                myAdapter.setFoot("没有更多商品了！");
                            }
                            return;
                        }
                        ArrayList<NewGoodBean> list = utils.array2List(result);
                        myAdapter.setFoot("加载更多商品...");
                        switch (type) {
                            case I.ACTION_DOWNLOAD:
                                myAdapter.initList(list);
                                break;
                            case I.ACTION_PULL_DOWN:
                                tv.setVisibility(View.GONE);
                                srl.setRefreshing(false);
                                myAdapter.initList(list);
                                ImageLoader.release();
                                break;
                            case I.ACTION_PULL_UP:
                                myAdapter.addList(list);
                                break;
                        }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }


    @OnClick({R.id.category_av_title, R.id.category_av_tv1, R.id.category_av_iv1, R.id.category_av_tv2, R.id.category_av_iv2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_av_title:
                break;
            case R.id.category_av_tv1:
                break;
            case R.id.category_av_iv1:
                break;
            case R.id.category_av_tv2:
                break;
            case R.id.category_av_iv2:
                break;
        }
    }
}
