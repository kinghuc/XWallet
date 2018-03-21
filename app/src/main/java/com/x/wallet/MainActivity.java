package com.x.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.x.wallet.ui.fragment.AccountListFragment;
import com.x.wallet.ui.fragment.MarketDetailFragment;
import com.x.wallet.ui.fragment.SettingsFragment;
import com.x.wallet.ui.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MenuItem mMenuItem;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mMenuItem != null) {
                    mMenuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                mMenuItem = mBottomNavigationView.getMenu().getItem(position);
                mMenuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager();

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_account:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_market_detail:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_setting:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        };

        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AccountListFragment());
        adapter.addFragment(new MarketDetailFragment());
        adapter.addFragment(new SettingsFragment());
        mViewPager.setAdapter(adapter);
    }
}