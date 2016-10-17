package ucai.cn.day_filicenter.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.BoutiqueBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {

    View view;

    public BoutiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_boutique, container, false);
        return view;
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

    class MyHolder2 extends RecyclerView.ViewHolder {
        TextView tvfoot;

        public MyHolder2(View itemView) {
            super(itemView);
            tvfoot = (TextView) itemView.findViewById(R.id.foot);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<BoutiqueBean> myList;
        Context context;

        public MyAdapter(ArrayList<BoutiqueBean> myList, Context context) {
            this.myList = myList;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
