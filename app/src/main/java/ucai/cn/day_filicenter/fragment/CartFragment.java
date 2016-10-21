package ucai.cn.day_filicenter.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.GoodsDetailsBean;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    View view;
    ArrayList<GoodsDetailsBean> myList = new ArrayList<>();
    ;
    MyCartAdapter myAdapter;
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView();
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView() {
        myAdapter = new MyCartAdapter(myList, getActivity());
        rv = (RecyclerView) view.findViewById(R.id.cart_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setAdapter(myAdapter);
        rv.setLayoutManager(linearLayoutManager);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv, add, delect;
        RadioButton check;
        TextView name, price, num;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.cart_item_iv);
            add = (ImageView) itemView.findViewById(R.id.cart_item_iv_add);
            delect = (ImageView) itemView.findViewById(R.id.cart_item_iv_delet);
            check = (RadioButton) itemView.findViewById(R.id.cart_item_rb_choos);
            name = (TextView) itemView.findViewById(R.id.cart_item_tv_name);
            price = (TextView) itemView.findViewById(R.id.cart_item_tv_price);
            num = (TextView) itemView.findViewById(R.id.cart_item_rv_num);
            view = itemView;
        }
    }

    class MyCartAdapter extends RecyclerView.Adapter<MyViewHolder> {

        ArrayList<GoodsDetailsBean> myList;
        Context context;
        RecyclerView parent;

        public MyCartAdapter(ArrayList<GoodsDetailsBean> myList, Context context) {
            this.myList = myList;
            this.context = context;
        }

        public void addList(ArrayList arrayList) {
            myList.addAll(arrayList);
            notifyDataSetChanged();
            L.e("111");
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.parent = (RecyclerView) parent;
            View view = View.inflate(context, R.layout.cart_item, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            GoodsDetailsBean goodsDetailsBean = myList.get(position);
            holder.name.setText(goodsDetailsBean.getGoodsName());
            holder.price.setText(goodsDetailsBean.getCurrencyPrice());
            ImageLoader.build(I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE)
                    .addParam(I.IMAGE_URL, goodsDetailsBean.getGoodsThumb())
                    .imageView(holder.iv)
                    .listener(parent)
                    .showImage(context);
            holder.view.setTag(position);
        }

        @Override
        public int getItemCount() {
            return myList == null ? 0 : myList.size();
        }
    }

}
