package com.sourcey.materiallogindemo.Fragment.OwnerFragmets;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourcey.materiallogindemo.Fragment.OwnerFragmets.MyOffersFragments.OwnerOffersFragment;
import com.sourcey.materiallogindemo.Fragment.OwnerFragmets.MyOffersFragments.OwnerSoldFragment;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.goodOffersAct;

public class MyAllOfferFragment extends Fragment {


    ViewPager vp;
    TabLayout tab;
    public class PagerAdapterFragment extends FragmentPagerAdapter {

        public PagerAdapterFragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new OwnerSoldFragment();
                case 0:
                default:
                    return new OwnerOffersFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void __init__(View root) {
        tab = root.findViewById(R.id.tb1);
        vp =  root.findViewById(R.id.vp1);
       vp.setAdapter(new PagerAdapterFragment(getActivity().getSupportFragmentManager()));
        tab.setupWithViewPager(vp);
        tab.getTabAt(0).setText("العروض النشطه");
        tab.getTabAt(1).setText("عروضى المباعه");
        for (int i = 0; i < tab.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custome_tab, null);
            tab.getTabAt(i).setCustomView(tv);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_my_all_offer, container, false);
        __init__(root);
        return root;
    }





}
