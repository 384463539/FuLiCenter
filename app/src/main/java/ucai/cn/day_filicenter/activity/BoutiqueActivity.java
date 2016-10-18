package ucai.cn.day_filicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.day_filicenter.R;
import ucai.cn.day_filicenter.fragment.NewgoodFragment;

public class BoutiqueActivity extends AppCompatActivity {
    String name;
    int id;
    NewgoodFragment newgoodFragment;
    TextView title_name;
    @Bind(R.id.boutique_at_tv_title)
    TextView boutiqueAtTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getIntExtra("id", 262);
        intiView();
        initData();
    }

    private void initData() {
        title_name.setText(name);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.boutique_at_layout, newgoodFragment);
        transaction.commit();
    }

    private void intiView() {
        newgoodFragment = new NewgoodFragment();
        newgoodFragment.setId(id);
        title_name = (TextView) findViewById(R.id.boutique_at_tv_title);
    }

    @OnClick(R.id.boutique_at_tv_title)
    public void onClick() {
        this.finish();
    }
}
