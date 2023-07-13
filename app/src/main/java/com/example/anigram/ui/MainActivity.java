package com.example.anigram.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.anigram.R;
import com.example.anigram.adapters.viewPager.ViewPagerFragmentAdapter_Main;
import com.example.anigram.ui.mainFragments.HomeFragment;
import com.example.anigram.ui.mainFragments.ProfileFragment;
import com.example.anigram.ui.mainFragments.SearchFragment;
import com.google.android.material.tabs.TabLayout;

//Activity für die drei Hauptfragments -> kümmert sich um das TabLayout : hält Search, Home, Profile fest
public class MainActivity extends AppCompatActivity {

    //Deklarierung der Variablen
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerFragmentAdapter_Main viewPagerAdapter;
    private int firstPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ruft die Methode auf um die Tabs zu setzen.
        setupTabs();
    }

    //Methode um die Tabs mit den Fragments zu setzen.
    private void setupTabs() {
        //Deklariert die UI - Elemente
        tabLayout = findViewById(R.id.tab_layout_main);
        viewPager = findViewById(R.id.view_pager_main);

        //Deklariert den Adapter der die TItel und Fragments festhält.
        viewPagerAdapter = new ViewPagerFragmentAdapter_Main(getSupportFragmentManager(), tabLayout, viewPager);

        //Addet die Fragments
        viewPagerAdapter.AddFragment(new SearchFragment(), "Search");
        viewPagerAdapter.AddFragment(new HomeFragment(), "Home");
        viewPagerAdapter.AddFragment(new ProfileFragment(), "Profile");

        //Setze die Custom Tabs -> Custom Icon und Custom Text
        setCustomTabs(0, R.drawable.search_icon, "Search");
        setCustomTabs(1, R.drawable.home_icon, "Home");
        setCustomTabs(2, R.drawable.user_icon, "Profile");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3); //Performance Gründen -> sonst laggy

        //Rufe die erste Seite auf -> in dem Fall Home, ansonsten würde Search angezeigt werden.
        initFirstPage();
    }

    //Methode um dafür zu sorgen, dass jedes Tab anders aussieht
    private void setCustomTabs(int index, int icon, String text) {
        tabLayout.getTabAt(index).setCustomView(R.layout.bottom_nav_item);

        View tabView = tabLayout.getTabAt(index).getCustomView();
        TextView textView = (TextView) tabView.findViewById(R.id.text_bottom_bar);
        textView.setText(text);

        ImageView imageView = (ImageView) tabView.findViewById(R.id.icon_bottom_bar);
        imageView.setImageResource(icon);
    }

    //Setze Delayed den ersten Tab.
    private void initFirstPage() {
        //Delay essential because of wrong translation for search icon at the start
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(firstPageIndex);
            }
        }, 50);
    }
}