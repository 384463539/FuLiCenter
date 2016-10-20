package ucai.cn.day_filicenter.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ucai.cn.day_filicenter.MainActivity;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.activity.GoodsDetailsActivity;
import ucai.cn.day_filicenter.bean.CategoryChildBean;


public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoMainActivity(Activity context) {
        startActivity(context, MainActivity.class);
    }

    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void startActivityB(Activity context, Class<?> cls, int id, String name, ArrayList<CategoryChildBean> list) {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.setClass(context, cls);
        if (list!=null){
            intent.putExtra("childList",list);
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
