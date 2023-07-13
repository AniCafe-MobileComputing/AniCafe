package com.example.anigram.adapters.viewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.anigram.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter_Main extends FragmentPagerAdapter {

    //ViewPager Klasse die dafür verantwortlich ist -> alle Fragments einer Activity zu speichern.
    //In dem Fall ist es unser Menü in in der MainActivity. -> Search, Home, Profile.

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private TabLayout tabLayout;

    public void AddFragment(Fragment fragment, String s) {
        fragmentArrayList.add(fragment);
        stringArrayList.add(s);
        tabLayout.addTab(tabLayout.newTab().setText(s));
    }

    public ViewPagerFragmentAdapter_Main(@NonNull FragmentManager fm, TabLayout tabLayout, ViewPager viewPager) {
        super(fm);
        this.tabLayout = tabLayout;
        initAnimationOnTabListener(viewPager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringArrayList.get(position);
    }

    //Ist für die Animation verantwortlich, wenn auf ein Tab gedrückt wird. -> onTabSelected und onTabUnselected
    private void initAnimationOnTabListener(ViewPager viewPager) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            //Wenn auf ein Tab gedrückt wird.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition()); //Switcht auf das Fragment

                //Bekommt die CustomView des Tabs
                View tabView = tabLayout.getTabAt(tab.getPosition()).getCustomView();
                if(tabView != null) {

                    //Setzt die UI Elemente
                    TextView text = tabView.findViewById(R.id.text_bottom_bar);
                    ImageView circle = tabView.findViewById(R.id.circle_bottom_bar);
                    RelativeLayout relativeLayout = tabView.findViewById(R.id.relative_layout_bottom_bar);
                    ImageView icon = tabView.findViewById(R.id.icon_bottom_bar);

                    //Animiert die UI Elemente
                    relativeLayout.animate().translationY(-1f);
                    icon.animate().scaleX(1f).scaleY(1f);
                    text.animate().scaleX(1f).scaleY(1f);
                    circle.animate().scaleX(1f).scaleY(1f);
                }

            }

            //Wenn ein Tab nicht mehr aktiv ist.
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                //Bekommt die CustomView des Tabs
                View tabView = tabLayout.getTabAt(tab.getPosition()).getCustomView();
                if(tabView != null) {

                    //Setzt die UI Elemente
                    TextView text = tabView.findViewById(R.id.text_bottom_bar);
                    ImageView circle = tabView.findViewById(R.id.circle_bottom_bar);
                    RelativeLayout relativeLayout = tabView.findViewById(R.id.relative_layout_bottom_bar);
                    ImageView icon = tabView.findViewById(R.id.icon_bottom_bar);

                    //Animiert die UI Elemente
                    icon.animate().scaleX(1.8f).scaleY(1.8f);
                    relativeLayout.animate().translationY((tabView.getHeight()/6.5f));
                    text.animate().scaleX(0f).scaleY(0f);
                    circle.animate().scaleX(0f).scaleY(0f);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
