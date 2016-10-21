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

import ucai.cn.day_filicenter.FuLiCenterApplication;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.fragment.BoutiqueFragment;
import ucai.cn.day_filicenter.fragment.CartFragment;
import ucai.cn.day_filicenter.fragment.CategoryFragment;
import ucai.cn.day_filicenter.fragment.NewgoodFragment;
import ucai.cn.day_filicenter.fragment.PersonFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    RadioButton rb_newgood, rb_boutique, rb_category, rb_car, rb_personal;
    NewgoodFragment newgoodFragment;
    BoutiqueFragment boutiqueFragment;
    CategoryFragment categoryFragment;
    PersonFragment personFragment;
    CartFragment cartFragment;
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArrayList<RadioButton> radioList = new ArrayList<>();
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setListener();
        initFragment();
    }

    private void setFragment() {
        viewPager.setCurrentItem(4);
        choose(4);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initFragment() {
        newgoodFragment = new NewgoodFragment();
        fragmentList.add(newgoodFragment);
        boutiqueFragment = new BoutiqueFragment();
        fragmentList.add(boutiqueFragment);
        categoryFragment = new CategoryFragment();
        fragmentList.add(categoryFragment);
        cartFragment = new CartFragment();
        fragmentList.add(cartFragment);
        personFragment = new PersonFragment();
        fragmentList.add(personFragment);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    private void setListener() {
        rb_newgood.setOnClickListener(this);
        rb_boutique.setOnClickListener(this);
        rb_category.setOnClickListener(this);
        rb_car.setOnClickListener(this);
        rb_personal.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (!isLogin()) {
                    return;
                }
                choose(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void initView() {
        rb_newgood = (RadioButton) findViewById(R.id.rb_newgood);
        rb_boutique = (RadioButton) findViewById(R.id.rb_boutique);
        rb_category = (RadioButton) findViewById(R.id.rb_category);
        rb_car = (RadioButton) findViewById(R.id.rb_car);
        rb_personal = (RadioButton) findViewById(R.id.rb_personal);
        radioList.add(rb_newgood);
        radioList.add(rb_boutique);
        radioList.add(rb_category);
        radioList.add(rb_car);
        radioList.add(rb_personal);
        viewPager = (ViewPager) findViewById(R.id.home_viewpager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_newgood:
                choose(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_boutique:
                choose(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.rb_category:
                viewPager.setCurrentItem(2);
                choose(2);
                break;
            case R.id.rb_car:
                viewPager.setCurrentItem(3);
                choose(3);
                break;
            case R.id.rb_personal:
                if (!isLogin()) {
                    return;
                }
                viewPager.setCurrentItem(4);
                choose(4);
                break;
        }
    }

    public void choose(int a) {
        for (int i = 0; i < radioList.size(); i++) {
            if (i == a) {
                radioList.get(i).setChecked(true);
            } else {
                radioList.get(i).setChecked(false);
            }
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

    public boolean isLogin() {
        return true;
    }
}
