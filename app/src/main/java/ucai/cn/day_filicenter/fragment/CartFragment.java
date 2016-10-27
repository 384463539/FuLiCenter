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
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.CartBean;
import ucai.cn.day_filicenter.bean.GoodsDetailsBean;
import ucai.cn.day_filicenter.bean.MessageBean;
import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    View view;
    ArrayList<CartBean> myList = new ArrayList<>();
    ;
    MyCartAdapter myAdapter;
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    View.OnClickListener listener;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView();
        setListener();
        return view;
    }

    private void setListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(getActivity());
                switch (v.getId()) {
                    case R.id.cart_item_iv_add:
                        final CartBean cb = (CartBean) v.getTag();
                        final int count = cb.getCount();
                        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                                .addParam(I.Cart.ID, cb.getId() + "")
                                .addParam(I.Cart.COUNT, count + 1 + "")
                                .targetClass(MessageBean.class)
                                .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                                    @Override
                                    public void onSuccess(MessageBean result) {
                                        if (result != null) {
                                            Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                            cb.setCount(count + 1);
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                    }
                                });
                        break;
                    case R.id.cart_item_iv_delet:
                        final CartBean cb2 = (CartBean) v.getTag();
                        final int a = cb2.getCount();
                        if (cb2.getCount() == 1) {
                            utils.setRequestUrl(I.REQUEST_DELETE_CART)
                                    .addParam(I.Cart.ID, cb2.getId() + "")
                                    .targetClass(MessageBean.class)
                                    .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                                        @Override
                                        public void onSuccess(MessageBean result) {
                                            if (result != null) {
                                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                                myAdapter.deleteCart(cb2);
                                            }
                                        }

                                        @Override
                                        public void onError(String error) {

                                        }
                                    });
                        } else {
                            utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                                    .addParam(I.Cart.ID, cb2.getId() + "")
                                    .addParam(I.Cart.COUNT, a - 1 + "")
                                    .targetClass(MessageBean.class)
                                    .execute(new OkHttpUtils.OnCompleteListener<MessageBean>() {
                                        @Override
                                        public void onSuccess(MessageBean result) {
                                            if (result != null) {
                                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                                cb2.setCount(a - 1);
                                                myAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onError(String error) {
                                        }
                                    });
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        UserAvatar user = FuLiCenterApplication.getUser();
        if (user != null) {
            final OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(getActivity());
            utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                    .addParam(I.Cart.USER_NAME, user.getMuserName())
                    .targetClass(CartBean[].class)
                    .execute(new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                        @Override
                        public void onSuccess(CartBean[] result) {
                            ArrayList<CartBean> list = utils.array2List(result);
                            myAdapter.initList(list);
                        }

                        @Override
                        public void onError(String error) {
                        }
                    });
        }
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
            add.setOnClickListener(listener);
            delect.setOnClickListener(listener);
        }
    }

    class MyCartAdapter extends RecyclerView.Adapter<MyViewHolder> {

        ArrayList<CartBean> myList;
        Context context;
        RecyclerView parent;

        public MyCartAdapter(ArrayList<CartBean> myList, Context context) {
            this.myList = myList;
            this.context = context;
        }

        public void initList(ArrayList arrayList) {
            myList = arrayList;
            notifyDataSetChanged();
        }

        public void deleteCart(CartBean cartBean) {
            myList.remove(cartBean);
            notifyDataSetChanged();
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
            CartBean cartBean = myList.get(position);
            GoodsDetailsBean goods = cartBean.getGoods();
            holder.name.setText(goods.getGoodsName());
            holder.price.setText(goods.getCurrencyPrice());
            holder.num.setText("(" + cartBean.getCount() + ")");
            holder.add.setTag(cartBean);
            holder.delect.setTag(cartBean);
            ImageLoader.build(I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE)
                    .addParam(I.IMAGE_URL, goods.getGoodsThumb())
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
