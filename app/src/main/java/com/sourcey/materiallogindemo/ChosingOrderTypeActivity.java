package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.databinding.ActivityChosingOrderTypeBinding;

public class ChosingOrderTypeActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityChosingOrderTypeBinding activityChosingOrderTypeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChosingOrderTypeBinding=ActivityChosingOrderTypeBinding.inflate(getLayoutInflater());
        setContentView(activityChosingOrderTypeBinding.getRoot());

        activityChosingOrderTypeBinding.cvBuy.setOnClickListener(this);
        activityChosingOrderTypeBinding.cvRent.setOnClickListener(this);
        getUserName();
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this,Add_Offers.class);
        if (view.getId() == R.id.cvRent) {
            intent.putExtra("type", 0);
        } else {
            intent.putExtra("type", 1);
        }
        startActivity(intent);
    }
    void getUserName(){
        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        activityChosingOrderTypeBinding.userName.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
