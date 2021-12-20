package com.sourcey.materiallogindemo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.MessageAdapter;
import com.sourcey.materiallogindemo.model.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class feedback extends AppCompatActivity {

    EditText text_sent;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        try{
            text_sent=findViewById(R.id.text_sent);
            rv=findViewById(R.id.rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            rv.setLayoutManager(linearLayoutManager);
            readMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),"admin","defualt");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }

    public void finish(View view) {
        finish();
    }
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    List<Chat> mChat;
    public void sendMessage(View view) {
        String message=text_sent.getText().toString();
        if(message.isEmpty()){
            Toast.makeText(this,"من فضلك ادخل الرسالة",Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference mReference = database.getReference();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("reciver", "admin");
            hashMap.put("message", message);
            MediaPlayer mp = MediaPlayer.create(this, R.raw.sent);
            mp.start();
            mReference.child("feedBack").push().setValue(hashMap);
            text_sent.setText("");
        }
    }
    MessageAdapter messageAdapter;
    private void readMessage(final String myID, final String userID, final String imageURL){
        mChat=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean check=dataSnapshot.child("feedBack").exists();
                if(check)
                    database.getReference("feedBack").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null)
//                                    && dataSnapshot.hasChild(Shared.offerKnow.getOfferID()))
                            {
                                DatabaseReference mReference=database.getReference("feedBack");
//                                        .child(Shared.offerKnow.getOfferID());
                                mReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mChat.clear();
                                        for(DataSnapshot dt:dataSnapshot.getChildren()){
                                            Chat chat=dt.getValue(Chat.class);
                                            if(chat!=null)
                                                if(chat.getSender().equals(myID)&&chat.getReciver().equals(userID)||
                                                        chat.getSender().equals(userID)&&chat.getReciver().equals(myID)){
                                                            mChat.add(chat);

                                                }
                                            messageAdapter =new MessageAdapter(feedback.this,mChat,imageURL);
                                            rv.setAdapter(messageAdapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
