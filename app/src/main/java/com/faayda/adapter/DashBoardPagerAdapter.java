package com.faayda.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.faayda.fragment.dashboard.SpendFragment;
import com.faayda.fragment.dashboard.TransactionFragment;
import com.faayda.fragment.dashboard.TrendFragment;

import java.util.ArrayList;

/**
 * Created by Aashutosh @ vinove on 8/5/2015.
 */
public final class DashBoardPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragmentList;

    public DashBoardPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentList.add(new SpendFragment());
        fragmentList.add(new TransactionFragment());
        fragmentList.add(new TrendFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Spend";
            case 1:
                return "Transactions";
            case 2:
                return "Trends";
            default:
                return "Testing";
        }
    }
}
