package ucai.cn.day_filicenter.fragment;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.activity.GoodsDetailsActivity;
import ucai.cn.day_filicenter.bean.NewGoodBean;
import ucai.cn.day_filicenter.bean.Result;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewgoodFragment extends Fragment {

    private int id = 0;
    View view;
    ArrayList<NewGoodBean> myList;
    MyAdapter myAdapter;
    SwipeRefreshLayout srl;
    RecyclerView rv;
    GridLayoutManager gridLayoutManager;
    TextView tv;
    View.OnClickListener myOnClickListener;
    int num = 1;
    int nNewstate;

    public NewgoodFragment() {
        // Required empty public constructor
    }

    public void setId(int a) {
        id = a;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newgood, container, false);
        initView();
        initData(I.ACTION_DOWNLOAD, num);
        setListener();
        return view;
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

    private void initData(final int type, int num) {
        final OkHttpUtils<NewGoodBean[]> utils = new OkHttpUtils<>(getActivity());
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam("cat_id", id + "")
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

    private void initView() {
        myList = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(), myList);
        rv = (RecyclerView) view.findViewById(R.id.newgood_rv);
        rv.setAdapter(myAdapter);
        gridLayoutManager = new GridLayoutManager(getActivity(), I.COLUM_NUM);
        //设置最后一列跨两行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == myAdapter.getItemCount() - 1 ? I.COLUM_NUM : 1;
            }
        });

        rv.setLayoutManager(gridLayoutManager);

        srl = (SwipeRefreshLayout) view.findViewById(R.id.newgood_srl);
        tv = (TextView) view.findViewById(R.id.newgood_head);

        myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
                intent.putExtra("goodsid", (Integer) v.getTag());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        };
    }


    class MyHolder1 extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView name, price;
        View view;

        public MyHolder1(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.newgood_item_iv);
            name = (TextView) itemView.findViewById(R.id.newgood_item_name);
            price = (TextView) itemView.findViewById(R.id.newgood_item_price);
            view = itemView;
            view.setOnClickListener(myOnClickListener);
        }
    }

    class MyHolder2 extends RecyclerView.ViewHolder {
        TextView tvfoot;

        public MyHolder2(View itemView) {
            super(itemView);
            tvfoot = (TextView) itemView.findViewById(R.id.foot);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
        ArrayList<NewGoodBean> myList;
        String foot;
        boolean isMore;
        RecyclerView parent;

        public MyAdapter(Context context, ArrayList myList) {
            this.context = context;
            this.myList = myList;
        }

        public void initList(ArrayList arrayList) {
            myList = arrayList;
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

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.parent = (RecyclerView) parent;
            RecyclerView.ViewHolder holder = null;
            View view;
            switch (viewType) {
                case 0:
                    view = View.inflate(context, R.layout.foot, null);
                    holder = new MyHolder2(view);
                    break;
                case 1:
                    view = View.inflate(context, R.layout.newgood_item, null);
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
                NewGoodBean goods = myList.get(position);
                MyHolder1 myHolder1 = (MyHolder1) holder;
                myHolder1.iv.setImageResource(R.drawable.category_child);
                myHolder1.name.setText(goods.getGoodsName());
                myHolder1.price.setText(goods.getShopPrice());
                myHolder1.view.setTag(goods.getGoodsId());
                String str = I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE;
                ImageLoader.build(str)
                        .addParam(I.IMAGE_URL, goods.getGoodsThumb())
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
