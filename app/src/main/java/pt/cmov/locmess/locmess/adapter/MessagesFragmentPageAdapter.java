package pt.cmov.locmess.locmess.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import pt.cmov.locmess.locmess.fragment.MessagesReceivedFragment;
import pt.cmov.locmess.locmess.fragment.MessagesSentFragment;

/**
 * Created by snackk on 04/05/2017.
 */

public class MessagesFragmentPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList();

    private final List<String> mFragmentTitleNames = new ArrayList();

    public MessagesFragmentPageAdapter(FragmentManager fm){
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleNames.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleNames.get(position);
    }
}