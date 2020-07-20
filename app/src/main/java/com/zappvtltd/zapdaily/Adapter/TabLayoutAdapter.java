package com.zappvtltd.zapdaily.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zappvtltd.zapdaily.TabContact.ContactFragment;
import com.zappvtltd.zapdaily.TabContact.FaqFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabLayoutAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                ContactFragment contactFragment=new ContactFragment();
                return contactFragment;
            case 1:
                FaqFragment faqFragment= new FaqFragment();
                return faqFragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
