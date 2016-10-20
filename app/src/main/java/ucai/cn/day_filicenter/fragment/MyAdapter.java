package ucai.cn.day_filicenter.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.NewGoodBean;
import ucai.cn.day_filicenter.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/19.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<NewGoodBean> myList;
    String foot;
    public boolean isMore;
    RecyclerView parent;
    View.OnClickListener myOnClickListener;

    public MyAdapter(Context context, ArrayList myList,View.OnClickListener myOnClickListener) {
        this.context = context;
        this.myList = myList;
        this.myOnClickListener = myOnClickListener;
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
            myHolder1.price.setText(goods.getCurrencyPrice());
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
}
