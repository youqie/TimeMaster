package bupt.id2017211631.timemasterbig;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BackupActivity extends AppCompatActivity {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        initData();
        initView();
    }

    private void initData() {
        tabs.add("备份");
        tabs.add("恢复");
        fragments.add(new TabFragment(this, tabs.get(0)));
        fragments.add(new TabFragment(this, tabs.get(1)));
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        //关联ViewPager和TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    class TabAdapter extends FragmentPagerAdapter {
        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        //显示标签上的文字
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }


}
