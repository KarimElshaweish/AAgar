package com.sourcey.materiallogindemo;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sourcey.materiallogindemo.Adapter.MessageAdapter;
import com.sourcey.materiallogindemo.Model.Chat;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.Model.User;
import com.sourcey.materiallogindemo.Notification.Client;
import com.sourcey.materiallogindemo.Notification.Data;
import com.sourcey.materiallogindemo.Notification.MyResponse;
import com.sourcey.materiallogindemo.Notification.Sender;
import com.sourcey.materiallogindemo.Notification.Token;
import com.sourcey.materiallogindemo.Service.APIService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAct extends AppCompatActivity {

    TextView phoneumber;
    MessageAdapter messageAdapter;
    List<Chat>mChat;
    RecyclerView recyclerView;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    ImageView sentImage,img;
    EditText sentText;
    APIService apiService;
    boolean notify=false;
    LinearLayout bottom;
    TextView price,city,street,desc;
    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(FirebaseAuth.getInstance().getUid()).setValue(token1);
    }
    TextView order,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        order=findViewById(R.id.order);
        result=findViewById(R.id.result);
        result.setText(Shared.MyOffer.getType()+" "+Shared.MyOffer.getBuildingTyp()+" "+Shared.MyOffer.getPrice()+" "+"ريال");
        order.setText(Shared.offerKnow.getSpinnerType()+"  "+Shared.offerKnow.getBuildingType()+" "+Shared.offerKnow.getPrice()+" ريال ");
        if(Shared.offerKnow!=null) {
            String id = FirebaseAuth.getInstance().getUid();
            bottom = findViewById(R.id.bottom);
            bottom.setVisibility(View.GONE);
            price = findViewById(R.id.price);
            price.setText(Shared.offerKnow.getPrice());
            city = findViewById(R.id.city);
            city.setText(Shared.offerKnow.getCity());
            street = findViewById(R.id.street);
            street.setText(Shared.offerKnow.getStreet());
            desc = findViewById(R.id.desc);
            desc.setText(Shared.offerKnow.getDescription());
            img=findViewById(R.id.img);
            Glide.with(this).load(Shared.offerKnow.getImageList().get(0)).into(img);

        }else{
                View view =findViewById(R.id.view);
                view.setVisibility(View.GONE);
        }
        apiService=Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        phoneumber=findViewById(R.id.phoneumber);
        sentImage=findViewById(R.id.sentImage);
        sentText=findViewById(R.id.text_sent);
if(Shared.fristTime) {
    Shared.fristTime=false;
//            String msg = "مرحبا اخى...." +
//                    "كنت اود ان استعلم بخصوص عرضك   " +
//                    Shared.offerKnow.getSpinnerType() +
//                    "..المتواجد بمدينة.. " +
//                    Shared.offerKnow.getCity() +
//                    " ..وسعره.. " +
    String msg="";
                    Shared.offerKnow.getPrice();
            FirebaseDatabase.getInstance().getReference().child("user").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dt:dataSnapshot.getChildren()){
                        User user=dt.getValue(User.class);
                        if(user.getUserID().equals(Shared.offerKnow.getuID())){
                            phoneumber.setText(user.getName());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
   // phoneumber.setText(Shared.offerKnow.getSpinnerType());

    notify = true;
            if (!msg.equals(""))
                sentMeeage(FirebaseAuth.getInstance().getCurrentUser().getUid(), Shared.sent_id, msg);
            sentText.setText("");
        }

        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        readMessage(FirebaseAuth.getInstance().getUid(),Shared.sent_id,"defualt");

    }
    public void Finish(View view){
        finish();
    }
    private void readMessage(final String myID, final String userID, final String imageURL){
        mChat=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean check=dataSnapshot.child("Chats").exists();
                if(check)
                    database.getReference("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null && dataSnapshot.hasChild(Shared.offerKnow.getOfferID()))
                            {
                                DatabaseReference mReference=database.getReference("Chats").child(Shared.offerKnow.getOfferID());
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
                                            messageAdapter =new MessageAdapter(ChatAct.this,mChat,imageURL);
                                            recyclerView.setAdapter(messageAdapter);
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


        updateToken(FirebaseInstanceId.getInstance().getToken());
    }
    private void sentMeeage(String sender, final String receiver, String message){
        DatabaseReference mReference=database.getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciver",receiver);
        hashMap.put("message",message);


        mReference.child("Chats").child(Shared.offerKnow.getOfferID()).push().setValue(hashMap);



        final String  msg=message;
        mReference=FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(notify)
                sendNotificaion(receiver,user.getName(),msg);
                notify=false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private  void sendNotificaion(String reciver, final String name, final String msg){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(reciver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Token token = dt.getValue(Token.class);
                    Data data = new Data(FirebaseAuth.getInstance().getUid()
                            , R.mipmap.ic_launcher
                            , name + " : " + msg, "New Message", Shared.sent_id);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotificaiton(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(ChatAct.this, "failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    System.out.println(t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void Tost(View  view){
        String msg =sentText.getText().toString();
        phoneumber.setText(Shared.offerKnow.getSpinnerType());

        notify = true;
        if (!msg.equals(""))
            sentMeeage(FirebaseAuth.getInstance().getCurrentUser().getUid(), Shared.sent_id, msg);
        sentText.setText("");
    }

    public void openOffer(View view) {
        Dialog dialog=new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(getLayoutInflater().inflate(R.layout.list_item,null));

        bottom = dialog.findViewById(R.id.bottom);
        bottom.setVisibility(View.GONE);
        price = dialog.findViewById(R.id.price);
        price.setText(Shared.offerKnow.getPrice());
        city = dialog.findViewById(R.id.city);
        city.setText(Shared.offerKnow.getCity());
        street = dialog.findViewById(R.id.street);
        street.setText(Shared.offerKnow.getStreet());
        desc = dialog.findViewById(R.id.desc);
        desc.setText(Shared.offerKnow.getDescription());
        img=dialog.findViewById(R.id.img);
        Glide.with(this).load(Shared.offerKnow.getImageList().get(0)).into(img);

        dialog.show();
    }

    public void openOrder(View view) {
        Dialog dialog=new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(getLayoutInflater().inflate(R.layout.list_item,null));

        bottom = dialog.findViewById(R.id.bottom);
        bottom.setVisibility(View.GONE);
        price = dialog.findViewById(R.id.price);
        price.setText(Shared.MyOffer.getPrice());
        city = dialog.findViewById(R.id.city);
        city.setText(Shared.MyOffer.getCity());
        street = dialog.findViewById(R.id.street);
        street.setText(Shared.MyOffer.getStreet());
        desc = dialog.findViewById(R.id.desc);
        desc.setVisibility(View.GONE);
        img=dialog.findViewById(R.id.img);
        

        dialog.show();
    }

    public void openDetails(View view) {
        startActivity(new Intent(this,MyOrderChatDetia.class));
    }

    public void openOfferDetalis(View view) {
        startActivity(new Intent(this,DetailsChatAct.class));
    }

}
