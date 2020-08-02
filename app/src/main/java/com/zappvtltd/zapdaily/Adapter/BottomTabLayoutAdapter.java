package com.zappvtltd.zapdaily.Adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zappvtltd.zapdaily.BottomSheetUI.BottomFragFrequency;
import com.zappvtltd.zapdaily.BottomSheetUI.BottomFragPay;
import com.zappvtltd.zapdaily.BottomSheetUI.BottomFragSelectAddress;
import com.zappvtltd.zapdaily.BottomSheetUI.BottomFragSubscribe;


public class BottomTabLayoutAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    String pid;

    public BottomTabLayoutAdapter(@NonNull FragmentManager fm, Context myContext, int totalTabs,String pid) {
        super(fm);
        this.myContext = myContext;
        this.totalTabs = totalTabs;
        this.pid=pid;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                BottomFragSubscribe bottomFragSubscribe =new BottomFragSubscribe();
                Bundle bundle=new Bundle();
                bundle.putString("pid",pid);
                bottomFragSubscribe.setArguments(bundle);

                return bottomFragSubscribe;
            case 1:
                BottomFragSelectAddress bottomFragSelectAddress =new BottomFragSelectAddress();
                Bundle bundleaddress =new Bundle();
                bundleaddress.putString("pid",pid);
                bottomFragSelectAddress.setArguments(bundleaddress);
                return bottomFragSelectAddress;

             case 2:
                 BottomFragFrequency bottomFragFrequency= new BottomFragFrequency();
                 Bundle bundlefre=new Bundle();
                 bundlefre.putString("pid",pid);
                 bottomFragFrequency.setArguments(bundlefre);
                 return bottomFragFrequency;

             case 3:
                 BottomFragPay bottomFragPay= new BottomFragPay();
                 Bundle bundlepay=new Bundle();
                 bundlepay.putString("pid",pid);
                 bottomFragPay.setArguments(bundlepay);

                 return bottomFragPay;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
