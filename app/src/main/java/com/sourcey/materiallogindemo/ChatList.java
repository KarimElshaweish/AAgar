package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.UserAdapter;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.Model.Chat;
import com.sourcey.materiallogindemo.Model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatList extends AppCompatActivity {

    RecyclerView recyclerView;
    List<User>musers;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    List<String>useList;
    UserAdapter userAdapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mReference=database.getReference("linkOffer");
    List<String>list;
    String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    private void getData(){
        list=new ArrayList<>();
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUser)) {
                    for (DataSnapshot dt:dataSnapshot.getChildren()) {
                        if(dt.hasChild(Shared.offerID)) {
                            for (DataSnapshot dt1 : dt.getChildren()) {
                                if(dt1.getKey().equals(Shared.offerID)){
                                    for(DataSnapshot dt2:dt1.getChildren()) {
                                        com.sourcey.materiallogindemo.Model.OfferResult offer = dt2.getValue(com.sourcey.materiallogindemo.Model.OfferResult.class);
                                        list.add(offer.getOfferID());
                                    }
                                }
                            }
                        }
                    }

                }
                getUsers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        useList=new ArrayList<>();
        Intent intent=getIntent();
        String offerId=intent.getStringExtra("id");
        getUsers();

    }

    private void getUsers() {
      //  final List<String>Formatuser=getUsersFroamted();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                useList.clear();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    for(DataSnapshot dt1:dt.getChildren()) {
                        Chat chat=dt1.getValue(Chat.class);
//                        if (list.contains(dt.getKey())) {
                            if (chat.getSender().equals(firebaseUser.getUid()))
                                useList.add(chat.getReciver());
                            if (chat.getReciver().equals(firebaseUser.getUid()))
                                useList.add(chat.getSender());
                     //   }
                    }

                }
                readCaht();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private List<String> getUsersFroamted() {
        List<String>newlist=new ArrayList<>();
        for(String s:list){
            String []split=s.split("\\*");
            newlist.add(split[0]);
        }
        return newlist;
    }

    public  void Finish(View view){
        finish();
    }
    private void readCaht(){
        musers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                musers.clear();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    User user=dt.getValue(User.class);
                    for(String id:useList){
                        if(user.getUserID().equals(id)){
                            if(musers.size()!=0){
                                for (User user1:musers){
                                    if(!user.getUserID().equals(user1.getUserID())){
                                            musers.add(user);
                                    }
                                }
                            }else {
                                musers.add(user);
                            }
                        }
                    }
                }
                userAdapter=new UserAdapter(getBaseContext(),musers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
