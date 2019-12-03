package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Fragment.ProfileFragments.InformationFragment;
import com.sourcey.materiallogindemo.Fragment.ProfileFragments.myOfferFragment;
import com.sourcey.materiallogindemo.Model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST =777 ;
    AppBarLayout toolbar;
    CircleImageView smallAvatar;
    ViewPager vp;
    TabLayout tab;
    CircleImageView profile_avatar;

    TextView namTxt;
    User user;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout dl;


    public void setUser(User user){
        this.user=user;
        dlName.setText(user.getName());
    }

    public TextView getNamTxt() {
        return this.namTxt;
    }

    public void setSmallAvatar(String image) {
        Glide.with(this).load(image).placeholder(R.drawable.avatar).into(this.smallAvatar);
    }

    public void setProfile_avatar(String image) {
        Glide.with(this).load(image).placeholder(R.drawable.avatar).into(this.profile_avatar);
        Glide.with(this).load(image).placeholder(R.drawable.avatar).into(this.navAvatar);

    }
    CircleImageView navAvatar;
    TextView dlName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navAvatar=findViewById(R.id.navAvatar);
        dlName=findViewById(R.id.dlName);
        dl = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        namTxt=findViewById(R.id.namTxt);
        profile_avatar=findViewById(R.id.profile_avatar);
        vp=findViewById(R.id.vp);
        tab=findViewById(R.id.tabs);
        vp.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tab.setupWithViewPager(vp);
        tab.getTabAt(0).setText("المعلومات الشخصيه");
        tab.getTabAt(1).setText("الطلبات");
        String []titles=new String[]{"المعلومات الشخصيه","الطلبات"};
        for(int i=0;i<tab.getTabCount();i++){
            TextView tv= (TextView) LayoutInflater.from(this).inflate(R.layout.text_viewtab,null);
            tv.setText(titles[i]);
            tab.getTabAt(i).setCustomView(tv);
        }
        ((TextView)tab.getTabAt(0).getCustomView().findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.primary));
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view=tab.getCustomView();
                TextView tv=view.findViewById(R.id.text1);
                tv.setTextColor(getResources().getColor(R.color.primary));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view=tab.getCustomView();
                TextView tv=view.findViewById(R.id.text1);
                tv.setTextColor(Color.parseColor("#000000"));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    public void change(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "اختار صور الصفحه الشخصية"), PICK_VIDEO_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK ) {
                Uri fileUri = data.getData();
                smallAvatar.setImageURI(fileUri);
            profile_avatar.setImageURI(fileUri);
            uploadImage(fileUri);
        }
    }
    private void uploadImage(Uri fileUri) {

        final KProgressHUD hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        FirebaseStorage.getInstance().getReference("profile").child(FirebaseAuth.getInstance().getCurrentUser().toString())
                .putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                user.setProfilePic(taskSnapshot.getDownloadUrl().toString());
                FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hud.dismiss();
                    }
                });
            }
        });
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
