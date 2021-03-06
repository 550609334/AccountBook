package com.accountbook.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.accountbook.R;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.view.fragment.ClassifyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClassifyActivity extends AppCompatActivity {
    @Bind(R.id.Toolbar)
    Toolbar mToolbar;
    @Bind(R.id.TabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.ViewPager)
    ViewPager mViewPager;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classify_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", ConstantContainer.EDIT_RECROD_REQUEST);

        init(type);
        eventBind();
    }

    private void init(int type) {
        mToolbar.setTitle(R.string.classify);
        setSupportActionBar(mToolbar);
        ClassifyAdapter adapter = new ClassifyAdapter(getSupportFragmentManager());

        mFragments = new ArrayList<>();

        switch (type) {
            case ConstantContainer.BUDGET_REQUEST:
                mFragments.add(ClassifyFragment.newInstance(ConstantContainer.EXPEND));
                mViewPager.setAdapter(adapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mTabLayout.getTabAt(0).setText(R.string.expend);
                break;
            case ConstantContainer.EDIT_RECROD_REQUEST:
                mFragments.add(ClassifyFragment.newInstance(ConstantContainer.EXPEND));
                mFragments.add(ClassifyFragment.newInstance(ConstantContainer.INCOME));
                mFragments.add(ClassifyFragment.newInstance(ConstantContainer.BORROW));
                mFragments.add(ClassifyFragment.newInstance(ConstantContainer.LEND));
                mViewPager.setAdapter(adapter);
                mTabLayout.setupWithViewPager(mViewPager);
                //初始化顶部tab
                mTabLayout.getTabAt(0).setText(R.string.expend);
                mTabLayout.getTabAt(1).setText(R.string.income);
                mTabLayout.getTabAt(2).setText(R.string.borrow);
                mTabLayout.getTabAt(3).setText(R.string.lend);
                break;
        }
    }

    private void eventBind() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class ClassifyAdapter extends FragmentPagerAdapter {
        public ClassifyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
