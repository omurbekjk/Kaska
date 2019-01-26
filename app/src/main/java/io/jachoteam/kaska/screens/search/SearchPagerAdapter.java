package io.jachoteam.kaska.screens.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.jachoteam.kaska.Tab2Fragment;
import io.jachoteam.kaska.TabFragment;

public class SearchPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public SearchPagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }




    @Override
    public Fragment getItem(int position) {

        switch(position)
        {

            case 0:
                SearchFeedFragment tab1 = new SearchFeedFragment();
                return tab1;
            case 1:
                SearchPeopleFragment tab2 = new SearchPeopleFragment();
                return  tab2;
           /* case 2:
                Tab3Fragment tab3 = new Tab3Fragment();
                tab3.setArguments(bundle);
                return  tab3;*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}