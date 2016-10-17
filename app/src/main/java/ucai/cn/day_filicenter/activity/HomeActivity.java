package ucai.cn.day_filicenter.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioButton;

import java.util.ArrayList;

import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.fragment.NewgoodFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    RadioButton rb_newgood, rb_boutique, rb_category, rb_car, rb_personal;
    NewgoodFragment newgoodFragment;
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setListener();
        initFragment();
    }

    private void initFragment() {
       viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        newgoodFragment = new NewgoodFragment();
        fragmentList.add(newgoodFragment);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    private void setListener() {
        rb_newgood.setOnClickListener(this);
        rb_boutique.setOnClickListener(this);
        rb_category.setOnClickListener(this);
        rb_car.setOnClickListener(this);
        rb_personal.setOnClickListener(this);

    }

    private void initView() {
        rb_newgood = (RadioButton) findViewById(R.id.rb_newgood);
        rb_boutique = (RadioButton) findViewById(R.id.rb_boutique);
        rb_category = (RadioButton) findViewById(R.id.rb_category);
        rb_car = (RadioButton) findViewById(R.id.rb_car);
        rb_personal = (RadioButton) findViewById(R.id.rb_personal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_newgood:
                hc();
                rb_personal.setChecked(true);
                rb_personal.setChecked(false);
                rb_newgood.setChecked(true);
//                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_layout, newgoodFragment);
//                fragmentTransaction.commit();
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_boutique:
                hc();

                break;
            case R.id.rb_category:
                hc();

                break;
            case R.id.rb_car:
                rb_newgood.setChecked(true);
                rb_newgood.setChecked(false);

                break;
            case R.id.rb_personal:
                hc();

                break;
        }
    }

    public void hc() {
        if (rb_car.isChecked()) {
            rb_car.setChecked(false);
        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
