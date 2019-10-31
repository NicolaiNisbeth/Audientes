package a3.audientes.fragments;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                HeartingTest tab2 = new HeartingTest();
                return  tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
