package com.sourcey.materiallogindemo.Fragment.ProfileFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.Profile;
import com.sourcey.materiallogindemo.R;

public class InformationFragment extends Fragment {
    User user=new User();
    public User getUser(){
        return this.user;
    }
    private void  getUserData(){
        final KProgressHUD hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        hud.dismiss();
                        user=dataSnapshot.getValue(User.class);
                        setData();
                        ((Profile)getActivity()).setUser(user);
                        ((Profile)getActivity()).setProfile_avatar(user.getProfilePic());
                        ((Profile)getActivity()).setSmallAvatar(user.getProfilePic());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    private void setData(){
        name.setText(user.getName());
        type.setText(user.getType());
        email.setText(user.getEmail());
        city.setText(user.getCity());
        ((Profile)getActivity()).getNamTxt().setText(name.getText());
    }
    TextView name,type,email,city;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v=inflater.inflate(R.layout.fragment_information, container, false);
         getUserData();
        name=v.findViewById(R.id.name);
        type=v.findViewById(R.id.type);
        email=v.findViewById(R.id.email);
        city=v.findViewById(R.id.city);
         return  v;
    }

}
