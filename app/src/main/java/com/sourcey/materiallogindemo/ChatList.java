package com.sourcey.materiallogindemo;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        useList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                useList.clear();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    Chat chat=dt.getValue(Chat.class);
                    if (chat.getSender().equals(firebaseUser.getUid()))
                        useList.add(chat.getReciver());
                    if(chat.getReciver().equals(firebaseUser.getUid()))
                        useList.add(chat.getSender());

                }
                readCaht();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
