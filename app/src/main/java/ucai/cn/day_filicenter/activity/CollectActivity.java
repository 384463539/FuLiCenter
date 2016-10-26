package ucai.cn.day_filicenter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.CollectBean;
import ucai.cn.day_filicenter.bean.MessageBean;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

public class CollectActivity extends AppCompatActivity {

    MyCollectAdapter myAdapter;
    ArrayList<CollectBean> myList;
    RecyclerView rv;
    SwipeRefreshLayout srl;
    TextView tv;
    int num = 1;
    GridLayoutManager gridLayoutManager;
    UserAvatar user;
    View.OnClickListener delete_listener;
    View.OnClickListener view_listener;
    @Bind(R.id.collect_av_layout1)
    LinearLayout collectAvLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        initView();
        initData(I.ACTION_DOWNLOAD, num);
        setListener();
    }

    private void setListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPostion = gridLayoutManager.findLastVisibleItemPosition();
                if (lastPostion >= myAdapter.getItemCount() - 1 && myAdapter.isMore && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    num++;
                    initData(I.ACTION_PULL_UP, num);
                }
            }
        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                num = 1;
                srl.setRefreshing(true);
                srl.setEnabled(true);
                tv.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN, num);
            }
        });
    }

    private void initView() {
        myList = new ArrayList<>();
        myAdapter = new MyCollectAdapter(this, myList);
        rv = (RecyclerView) findViewById(R.id.collect_av_rv);
        gridLayoutManager = new GridLayoutManager(this, I.COLUM_NUM);
        rv.setAdapter(myAdapter);
        //设置最后一列跨两行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == myAdapter.getItemCount() - 1 ? I.COLUM_NUM : 1;
            }
        });
        rv.setLayoutManager(gridLayoutManager);
        srl = (SwipeRefreshLayout) findViewById(R.id.collect_av_srl);
        tv = (TextView) findViewById(R.id.collect_av_head);

        delete_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CollectBean cb = (CollectBean) v.getTag();
                OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(CollectActivity.this);
                utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                        .addParam(I.Cart.USER_NAME, cb.getUserName())
                        .addParam(I.Cart.GOODS_ID, cb.getGoodsId() + "")
                        .targetClass(MessageBean.class)
                        .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result.isSuccess()) {
                                    myAdapter.removeGood(cb);
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
            }
        };
        view_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int goodsId = (int) v.getTag();
                Intent intent = new Intent(CollectActivity.this, GoodsDetailsActivity.class);
                intent.putExtra("goodsid", goodsId);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initData(final int type, int num) {
        user = FuLiCenterApplication.getUser();
        final OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(this);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Cart.USER_NAME, user.getMuserName())
                .addParam(I.PAGE_ID, num + "")
                .addParam(I.PAGE_SIZE, 4 + "")
                .targetClass(CollectBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
                    @Override
                    public void onSuccess(CollectBean[] result) {
                        myAdapter.isMore = result != null && result.length > 0;
                        if (!myAdapter.isMore) {
                            if (type == I.ACTION_PULL_UP) {
                                myAdapter.setFoot("没有更多宝贝了");
                            }
                            return;
                        }
                        ArrayList<CollectBean> list = utils.array2List(result);
                        L.i(list.toString());
                        myAdapter.setFoot("加载更多商品...");
                        switch (type) {
                            case I.ACTION_DOWNLOAD:
                                myAdapter.initList(list);
                                L.i("进入");
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

    @OnClick(R.id.collect_av_layout1)
    public void onClick() {
        finish();
    }


    class MyHolder1 extends RecyclerView.ViewHolder {
        ImageView iv, delete;
        TextView name;
        LinearLayout ly;

        public MyHolder1(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.collect_item_iv);
            delete = (ImageView) itemView.findViewById(R.id.collect_item_delete);
            name = (TextView) itemView.findViewById(R.id.collect_item_name);
            ly = (LinearLayout) itemView.findViewById(R.id.collect_layout_iv);
            ly.setOnClickListener(view_listener);
            delete.setOnClickListener(delete_listener);
        }
    }

    class MyHolder2 extends RecyclerView.ViewHolder {
        TextView tvfoot;

        public MyHolder2(View itemView) {
            super(itemView);
            tvfoot = (TextView) itemView.findViewById(R.id.foot);
        }
    }


    class MyCollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
        ArrayList<CollectBean> myList;
        String foot;
        boolean isMore;
        RecyclerView parent;

        public MyCollectAdapter(Context context, ArrayList myListr) {
            this.context = context;
            this.myList = myListr;
        }

        public void initList(ArrayList List) {
            myList = List;
            notifyDataSetChanged();
        }

        public void addList(ArrayList arrayList) {
            myList.addAll(arrayList);
            notifyDataSetChanged();
        }

        public void setFoot(String str) {
            foot = str;
            notifyDataSetChanged();
        }

        public void removeGood(CollectBean cb) {
            myList.remove(cb);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            L.i("creatcreat");
            this.parent = (RecyclerView) parent;
            RecyclerView.ViewHolder holder = null;
            View view;
            switch (viewType) {
                case 0:
                    view = View.inflate(context, R.layout.foot, null);
                    holder = new MyHolder2(view);
                    break;
                case 1:
                    view = View.inflate(context, R.layout.collect_item, null);
                    holder = new MyHolder1(view);
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == myList.size()) {
                MyHolder2 myHolder2 = (MyHolder2) holder;
                myHolder2.tvfoot.setText(foot);
            } else {
                CollectBean cb = myList.get(position);
                MyHolder1 myHolder1 = (MyHolder1) holder;
                myHolder1.name.setText(cb.getGoodsName());
                myHolder1.ly.setTag(cb.getGoodsId());
                myHolder1.delete.setTag(cb);
                String str = I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE;
                ImageLoader.build(str)
                        .addParam(I.IMAGE_URL, cb.getGoodsThumb())
                        .imageView(myHolder1.iv)
                        .listener(parent)
                        .showImage(context);
            }
        }

        @Override
        public int getItemCount() {
            return myList == null ? 0 : myList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == myList.size()) {
                return 0;
            } else {
                return 1;
            }
        }
    }

}
