package ucai.cn.day_filicenter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioButton;

import ucai.cn.day_filicenter.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    RadioButton rb_newgood, rb_boutique, rb_category, rb_car, rb_personal;
    ExpandableListView elv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setListener();
        elv.setAdapter(new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return 0;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 0;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return null;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return null;
            }

            @Override
            public long getGroupId(int groupPosition) {
                return 0;
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
                return null;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }
        });
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

        elv = (ExpandableListView) findViewById(R.id.elv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_newgood:
                hc();
                break;
            case R.id.rb_boutique:
                hc();
                break;
            case R.id.rb_category:
                hc();
                break;
            case R.id.rb_car:
                rb_newgood.setChecked(false);
                rb_boutique.setChecked(false);
                rb_category.setChecked(false);
                rb_personal.setChecked(false);
                break;
            case R.id.rb_personal:
                hc();
                break;

        }
    }
    public void hc(){
        if (rb_car.isChecked()){
            rb_car.setChecked(false);
        }
    }
}
