package com.example.anigram.ui.mainFragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.R;
import com.example.anigram.ui.profileFragments.FriendsFragment;
import com.example.anigram.ui.profileFragments.ListFragment;
import com.example.anigram.adapters.viewPager.ViewPagerAdapter_Profile;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//Fragment für Main (Profile)
public class ProfileFragment extends Fragment {

    //Deklarierung der UI - Elemente
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CircleImageView profilePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        //Setzung der UI - Elemente
        tabLayout = v.findViewById(R.id.profile_tab_layout);
        viewPager = v.findViewById(R.id.view_pager_profile);
        profilePicture = v.findViewById(R.id.profile_picture);

        //Setzung der Welt in die UI - Elemente
        Glide.with(v.getContext()).load(GlobalVariables.mal.getAuthenticatedUser().getPictureURL()).into(profilePicture);
        ((TextView) v.findViewById(R.id.username)).setText(GlobalVariables.authenticatedUser.getName());
        ((TextView) v.findViewById(R.id.rating_text)).setText(GlobalVariables.authenticatedUser.getAnimeStatistics().getMeanScore().toString());
        ((TextView) v.findViewById(R.id.entries_text)).setText("(" + GlobalVariables.authenticatedUser.getAnimeStatistics().getItems().toString() + " Entries)");
        ((TextView) v.findViewById(R.id.days_text)).setText(GlobalVariables.authenticatedUser.getAnimeStatistics().getDays() + " Days");
        ((TextView) v.findViewById(R.id.eps_text)).setText(GlobalVariables.authenticatedUser.getAnimeStatistics().getEpisodes() + " Episodes");

        //Initialisiere die Tabs - Methode -> List, Friends.
        ViewPagerAdapter_Profile viewPagerAdapter_profile = new ViewPagerAdapter_Profile(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter_profile.addFragment(new ListFragment(), "List");
        viewPagerAdapter_profile.addFragment(new FriendsFragment(), "Friends");

        viewPager.setAdapter(viewPagerAdapter_profile);
        tabLayout.setupWithViewPager(viewPager);


        //PIECHART Inspiration: https://youtu.be/fsVdzURuo_Y
        PieChart pieChart = v.findViewById(R.id.profile_chart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(GlobalVariables.mal.getAuthenticatedUser().getAnimeStatistics().getItemsCompleted(), "Completed"));
        entries.add(new PieEntry(GlobalVariables.mal.getAuthenticatedUser().getAnimeStatistics().getItemsPlanToWatch(), "Plan to Watch"));
        entries.add(new PieEntry(GlobalVariables.mal.getAuthenticatedUser().getAnimeStatistics().getItemsWatching(), "Watching"));
        entries.add(new PieEntry(GlobalVariables.mal.getAuthenticatedUser().getAnimeStatistics().getItemsOnHold(), "On Hold"));
        entries.add(new PieEntry(GlobalVariables.mal.getAuthenticatedUser().getAnimeStatistics().getItemsDropped(), "Dropped"));


        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieDataSet.setColors(
                            Color.rgb(101, 0, 164),
                            Color.rgb(189, 147, 216),
                            Color.rgb(78, 165, 217),
                            Color.rgb(191, 56, 255),
                            Color.rgb(81,68,91));


        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.animateY(1000);
        pieDataSet.setDrawValues(false);

        //PIECHART LEGEND SETTINGS
        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(8f);
        legend.setYEntrySpace(0f);
        legend.setWordWrapEnabled(true);
        return v;
    }

}