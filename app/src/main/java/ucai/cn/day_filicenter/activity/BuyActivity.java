package ucai.cn.day_filicenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.bean.CartBean;
import ucai.cn.day_filicenter.utils.L;

public class BuyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Intent intent = getIntent();
        ArrayList cartlist = (ArrayList) intent.getSerializableExtra("cartlist");
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {

    }
}
