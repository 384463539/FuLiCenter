package ucai.cn.day_filicenter.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.BoutiqueBean;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {

    View view;
    MyAdapter myAdapter;
    ArrayList<BoutiqueBean> myList;
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    public BoutiqueFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_boutique, container, false);
        initView();
        initData();
        return view;
    }

    private void initData() {
        final OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<>(getActivity());
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
                    @Override
                    public void onSuccess(BoutiqueBean[] result) {
                        ArrayList<BoutiqueBean> list = utils.array2List(result);
                        myAdapter.initList(list);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initView() {
        myList = new ArrayList<>();
        myAdapter = new MyAdapter(myList, getActivity());
        rv = (RecyclerView) view.findViewById(R.id.boutique_rv);
        rv.setAdapter(myAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);
    }

    class MyHolder1 extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv_title, tv_description, tv_name;

        public MyHolder1(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.boutique_iv);
            tv_title = (TextView) itemView.findViewById(R.id.boutique_tv_title);
            tv_name = (TextView) itemView.findViewById(R.id.boutique_tv_name);
            tv_description = (TextView) itemView.findViewById(R.id.boutique_tv_description);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<BoutiqueBean> myList;
        Context context;
        RecyclerView parent;

        public MyAdapter(ArrayList<BoutiqueBean> myList, Context context) {
            this.myList = myList;
            this.context = context;
        }

        public void initList(ArrayList arrayList) {
            myList = arrayList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.parent = (RecyclerView) parent;
            RecyclerView.ViewHolder holder;
            View view;
            view = View.inflate(context, R.layout.boutique_item, null);
            holder = new MyHolder1(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            BoutiqueBean boutiqueBean = myList.get(position);
            MyHolder1 myHolder1 = (MyHolder1) holder;
            myHolder1.tv_title.setText(boutiqueBean.getTitle());
            myHolder1.tv_description.setText(boutiqueBean.getDescription());
            myHolder1.tv_name.setText(boutiqueBean.getName());
            String str = I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE;
            ImageLoader.build(str)
                    .addParam(I.IMAGE_URL, boutiqueBean.getImageurl())
                    .imageView(myHolder1.iv)
                    .listener(parent)
                    .showImage(context);

        }

        @Override
        public int getItemCount() {
            return myList == null ? 0 : myList.size();
        }
    }

}
