package io.jachoteam.kaska;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;
    String uid;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs, String uid)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
        this.uid = uid;
    }




    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);

        switch(position)
        {

            case 0:
                TabFragment tab1 = new TabFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                Tab2Fragment tab2 = new Tab2Fragment();
                tab2.setArguments(bundle);
                return  tab2;
            case 2:
                Tab3Fragment tab3 = new Tab3Fragment();
                tab3.setArguments(bundle);
                return  tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}