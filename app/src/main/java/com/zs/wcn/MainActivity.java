package com.zs.wcn;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zs.wcn.bt.BleAct;
import com.zs.wcn.bt.ClassicBtAct;
import com.zs.wcn.base.BaseActivity;
import com.zs.wcn.main.BtFg;
import com.zs.wcn.main.LbsFg;
import com.zs.wcn.main.NfcFg;
import com.zs.wcn.main.UwbFg;
import com.zs.wcn.main.WifiFg;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        final ViewPager viewPager = findViewById(R.id.main_view_pager);
        final BottomNavigationView navigationView = findViewById(R.id.main_navigation_view);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new WifiFg());
        fragmentList.add(new BtFg());
        fragmentList.add(new LbsFg());
        fragmentList.add(new NfcFg());
        fragmentList.add(new UwbFg());
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(fragmentList.size() - 1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                navigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_bottom_tab_wifi:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.main_bottom_tab_bt:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.main_bottom_tab_lbs:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.main_bottom_tab_nfc:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.main_bottom_tab_uwb:
                        viewPager.setCurrentItem(4);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Has Location Permission", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "No Location Permission", Toast.LENGTH_SHORT).show();
        }
    }

    public void toClassicBt(View v) {
        startActivity(new Intent(MainActivity.this, ClassicBtAct.class));
    }

    public void toBle(View v) {
        startActivity(new Intent(MainActivity.this, BleAct.class));
    }

    private static class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList;

        TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mList.get(arg0);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}