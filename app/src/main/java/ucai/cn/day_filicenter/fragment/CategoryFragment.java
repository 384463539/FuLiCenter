package ucai.cn.day_filicenter.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ucai.cn.day_filicenter.I;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.CategoryChildBean;
import ucai.cn.day_filicenter.bean.CategoryGroupBean;
import ucai.cn.day_filicenter.utils.ImageLoader;
import ucai.cn.day_filicenter.utils.L;
import ucai.cn.day_filicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    View view;
    ArrayList<CategoryGroupBean> groupList = new ArrayList<>();
    ArrayList<ArrayList<CategoryChildBean>> childList = new ArrayList<>();
    ExpandableListView elv;
    MyExpandableAdapter myExpandableAdapter;
    int pagid = 1;
    int pagsize = 20;

    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        initData();
//        setListener();
        return view;
    }

    private void setListener() {
//        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(getActivity(), groupPosition + " " + childPosition, Toast.LENGTH_SHORT).show();
//
//                return false;
//            }
//        });
    }

    private void initData() {
        final OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<>(getActivity());
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
                    @Override
                    public void onSuccess(CategoryGroupBean[] result) {
                        ArrayList<CategoryGroupBean> list = utils.array2List(result);
                        groupList = list;
                        initChildeData(groupList);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initChildeData(ArrayList<CategoryGroupBean> list) {
        final OkHttpUtils<CategoryChildBean[]> chileutils = new OkHttpUtils<>(getActivity());
        for (CategoryGroupBean c : list) {
            SystemClock.sleep(200);
            chileutils.url(I.SERVER_ROOT + I.REQUEST_FIND_CATEGORY_CHILDREN)
                    .addParam(I.CategoryChild.PARENT_ID, c.getId() + "")
                    .addParam(I.PAGE_ID, pagid + "")
                    .addParam(I.PAGE_SIZE, pagsize + "")
                    .targetClass(CategoryChildBean[].class)
                    .execute(new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                        @Override
                        public void onSuccess(CategoryChildBean[] result) {
                            ArrayList<CategoryChildBean> clist = chileutils.array2List(result);
                            childList.add(clist);
                        }

                        @Override
                        public void onError(String error) {
                        }
                    });
        }
        initView();
    }

    private void initView() {
        elv = (ExpandableListView) view.findViewById(R.id.category_elv);
        myExpandableAdapter = new MyExpandableAdapter(getActivity(), groupList, childList);
        elv.setAdapter(myExpandableAdapter);
    }

    class MyExpandableAdapter extends BaseExpandableListAdapter {

        Context context;
        ArrayList<CategoryGroupBean> groupList;
        ArrayList<ArrayList<CategoryChildBean>> childList;

        public MyExpandableAdapter(Context context, ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
            this.context = context;
            this.groupList = groupList;
            this.childList = childList;
        }

        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view;
            GroupViewHolder gholder;
            if (convertView == null) {
                gholder = new GroupViewHolder();
                view = View.inflate(context, R.layout.category_group, null);
                gholder.iv = (ImageView) view.findViewById(R.id.category_group_iv);
                gholder.iv2 = (ImageView) view.findViewById(R.id.category_group_iv2);
                gholder.name = (TextView) view.findViewById(R.id.category_group_name);
                view.setTag(gholder);
            } else {
                view = convertView;
                gholder = (GroupViewHolder) view.getTag();
            }
            CategoryGroupBean categoryGroupBean = groupList.get(groupPosition);
            gholder.name.setText(categoryGroupBean.getName());
            if (isExpanded) {
                gholder.iv2.setImageResource(R.mipmap.expand_off);
            } else {
                gholder.iv2.setImageResource(R.mipmap.expand_on);
            }
            ImageLoader.build(I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE)
                    .addParam(I.IMAGE_URL, categoryGroupBean.getImageUrl())
                    .imageView(gholder.iv)
                    .showImage(context);
            return view;
        }

        class GroupViewHolder {
            ImageView iv, iv2;
            TextView name;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view;
            ChildViewHolder cholder;
            if (convertView == null) {
                view = View.inflate(context, R.layout.category_child, null);
                cholder = new ChildViewHolder();
                cholder.civ = (ImageView) view.findViewById(R.id.category_child_iv);
                cholder.name = (TextView) view.findViewById(R.id.category_child_name);
                view.setTag(cholder);
            } else {
                view = convertView;
                cholder = (ChildViewHolder) view.getTag();
            }
            CategoryChildBean categoryChildBean = childList.get(groupPosition).get(childPosition);
            cholder.name.setText(categoryChildBean.getName());
            ImageLoader.build(I.SERVER_ROOT + I.REQUEST_DOWNLOAD_IMAGE)
                    .addParam(I.IMAGE_URL, categoryChildBean.getImageUrl())
                    .imageView(cholder.civ)
                    .showImage(context);
            return view;
        }

        class ChildViewHolder {
            ImageView civ;
            TextView name;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
