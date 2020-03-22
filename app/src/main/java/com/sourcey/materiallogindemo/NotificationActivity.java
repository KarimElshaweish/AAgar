package com.sourcey.materiallogindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.NotificationAdapter;
import com.sourcey.materiallogindemo.Model.Notification;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notificationRv;
    NotificationAdapter adapter;
    List<Notification> notificationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notificationRv=findViewById(R.id.notification_rv);
        notificationRv.setHasFixedSize(true);
        notificationRv.setLayoutManager(new LinearLayoutManager(this));
        notificationList=new ArrayList<>();
        adapter=new NotificationAdapter(notificationList,this);
        notificationRv.setAdapter(adapter);
        getNotification();
    }
    public void back(View view) {
        finish();
    }
    private void getNotification(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notification");
        Query query=ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationList.clear();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    Notification notification=dt.getValue(Notification.class);
                    notificationList.add(notification);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
