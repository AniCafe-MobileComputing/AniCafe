package com.example.anigram.adapters.viewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter_Anime_Details extends FragmentPagerAdapter {

    //ViewPager Klasse die dafür verantwortlich ist -> alle Fragments einer Activity zu speichern.
    //In dem Fall die Fragments in der einzelnen Details Anzeige für ein Anime -> Details, Reviews, Suggestions.

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> stringArrayList = new ArrayList<>();

    public void AddFragment(Fragment fragment, String s) {
        fragmentArrayList.add(fragment);
        stringArrayList.add(s);
    }

    public ViewPagerFragmentAdapter_Anime_Details(@NonNull FragmentManager fm) {
        super(fm);
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
}
