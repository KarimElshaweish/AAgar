package com.sourcey.materiallogindemo;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.sourcey.materiallogindemo.Fragment.InformationFragment;
import com.sourcey.materiallogindemo.Fragment.myOfferFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    AppBarLayout toolbar;
    CircleImageView smallAvatar;
    ViewPager vp;
    TabLayout tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        vp=findViewById(R.id.vp);
        tab=findViewById(R.id.tabs);
        vp.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tab.setupWithViewPager(vp);
        tab.getTabAt(0).setText("المعلومات الشخصيه");
        tab.getTabAt(1).setText("الطلبات");
        smallAvatar=findViewById(R.id.profile_avatar_small);
        toolbar=findViewById(R.id.appBar);
        toolbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(Math.abs(i)==appBarLayout.getTotalScrollRange()){
                    smallAvatar.setVisibility(View.VISIBLE);
                }else if(i==0){
                    smallAvatar.setVisibility(View.GONE);
                }else {
                    smallAvatar.setAlpha(125);
                }
            }
        });
    }

    public void finish(View view) {
        finish();
    }
    public class  PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new myOfferFragment();
                case 0:
                default:
                    return new InformationFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
