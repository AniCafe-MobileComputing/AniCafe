package com.example.anigram.adapters.viewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.anigram.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ViewPager2FragmentAdapter extends FragmentStateAdapter {

    //Ist derzeit nirgendswo aktiv -> wurde rausgenommen, da wir stattdessen für die erste Version entschieden haben, aufgrund von ScrollProblemen in den Fragments.

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> stringArrayList = new ArrayList<>();
    TabLayout tabLayout;

    public ViewPager2FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, TabLayout tabLayout, ViewPager2 viewPager) {
        super(fragmentManager, lifecycle);
        this.tabLayout = tabLayout;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                View tabView = tabLayout.getTabAt(tab.getPosition()).getCustomView();
                if(tabView != null) {
                    TextView text = tabView.findViewById(R.id.text_bottom_bar);
                    ImageView circle = tabView.findViewById(R.id.circle_bottom_bar);
                    RelativeLayout relativeLayout = tabView.findViewById(R.id.relative_layout_bottom_bar);
                    ImageView icon = tabView.findViewById(R.id.icon_bottom_bar);

                    icon.animate().scaleX(1f).scaleY(1f);
                    relativeLayout.animate().translationY(-1f);
                    text.animate().scaleX(1f).scaleY(1f);
                    circle.animate().scaleX(1f).scaleY(1f);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tabLayout.getTabAt(tab.getPosition()).getCustomView();
                if(tabView != null) {
                    TextView text = tabView.findViewById(R.id.text_bottom_bar);
                    ImageView circle = tabView.findViewById(R.id.circle_bottom_bar);
                    RelativeLayout relativeLayout = tabView.findViewById(R.id.relative_layout_bottom_bar);
                    ImageView icon = tabView.findViewById(R.id.icon_bottom_bar);

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

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public void AddFragment(Fragment fragment, String s) {
        fragmentArrayList.add(fragment);
        stringArrayList.add(s);
        tabLayout.addTab(tabLayout.newTab().setText(s));
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
}