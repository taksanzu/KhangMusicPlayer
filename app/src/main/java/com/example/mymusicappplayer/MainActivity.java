package com.example.mymusicappplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActionBar toolbar;
    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.vp_Music);
        //Set thông tin toolbar
        toolbar = getSupportActionBar();
        toolbar.setTitle("Trang Chủ");
        loadFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Tương tác ẩn/hiện navigation bar
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_mainpage:
                        toolbar.setTitle("Trang chủ");
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_album:
                        toolbar.setTitle("Album");
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_folder:
                        toolbar.setTitle("Thư mục");
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_setting:
                        toolbar.setTitle("Cài đặt");
                        viewPager.setCurrentItem(3);
                        return true;
                }
                return true;
            }
            });

    }
    private void loadFragment() {
        // load fragment
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        toolbar.setTitle("Trang chủ");
                        bottomNavigationView.getMenu().findItem(R.id.navigation_mainpage).setChecked(true);
                        break;
                    case 1:
                        toolbar.setTitle("Album");
                        bottomNavigationView.getMenu().findItem(R.id.navigation_album).setChecked(true);
                        break;
                    case 2:
                        toolbar.setTitle("Thư mục");
                        bottomNavigationView.getMenu().findItem(R.id.navigation_folder).setChecked(true);
                        break;
                    case 3:
                        toolbar.setTitle("Cài đặt");
                        bottomNavigationView.getMenu().findItem(R.id.navigation_setting).setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}