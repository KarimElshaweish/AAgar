package com.sourcey.materiallogindemo;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.kaopiz.kprogresshud.KProgressHUD;
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

import de.hdodenhof.circleimageview.CircleImageView;
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
    ValueEventListener sentListener;
    CircleImageView avatar;
    String phoneNumber;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Shared.AllMesage=false;
        Shared.chatOfferId=null;
    }

    @Override
    protected void onPause() {
        Shared.AllMesage=false;
        if(sentListener!=null)
        FirebaseDatabase.getInstance().getReference("Chats").removeEventListener(sentListener);
        super.onPause();
    }

    private void seenMessage(final String userid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        sentListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                        Chat chat=dt.getValue(Chat.class);
                        if(chat!=null) {
                            String[] offid = chat.getId().split("\\*");
                            if (chat.getReciver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    && chat.getSender().equals(userid) ||
                                    chat.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            && chat.getReciver().equals(userid)
                            ) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("isseen", true);
                                dt.getRef().updateChildren(hashMap);
                            }
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(FirebaseAuth.getInstance().getUid()).setValue(token1);
    }
    TextView order,result;
    User user;
    CardView cv1,cv2;
    private void  getUserData(String usreID){
        final KProgressHUD hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        FirebaseDatabase.getInstance().getReference("user").child(usreID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        hud.dismiss();
                        user=dataSnapshot.getValue(User.class);
                        Glide.with(ChatAct.this).load(user.getProfilePic()).placeholder(R.drawable.avatar)
                                .into(avatar);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    LinearLayout linTop;
    TextView blockingTxt;//ubblockingTxt;
    RelativeLayout btt;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));
                    startActivity(callIntent);
                } else {
                }                    Log.d("TAG", "Call Permission Not Granted");

                break;

            default:
                break;
        }
    }
    private void getOffer(){
        String offID=Shared.offerKnow!=null?Shared.offerKnow.getOfferID():Shared.chatOfferId;
        String uID=(offID.split("\\*"))[0];
        String oID=(offID.split("\\*"))[1];
        FirebaseDatabase.getInstance().getReference("OfferResult")
                .child(uID)
                .child(oID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OfferResult offerResult=dataSnapshot.getValue(OfferResult.class);
                if(offerResult!=null) {
                    result.setText(offerResult.getBuildingType());
                    Shared.offerKnow = offerResult;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    String []IDS ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bottom = findViewById(R.id.bottom);
       // ubblockingTxt=findViewById(R.id.ubblocking);
        btt=findViewById(R.id.btt);
        blockingTxt=findViewById(R.id.blocking);
        blockingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btt.setVisibility(View.GONE);
                blockingTxt.setVisibility(View.GONE);
               // ubblockingTxt.setVisibility(View.VISIBLE);
                block(FirebaseAuth.getInstance().getCurrentUser().getUid(), Shared.sent_id, "block");
            }
        });
        linTop=findViewById(R.id.liTop);
        cv1=findViewById(R.id.cv1);
        cv2=findViewById(R.id.cv2);
        avatar=findViewById(R.id.avatar);
        order=findViewById(R.id.order);
        result=findViewById(R.id.result);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Shared.customer) {
                    Shared.offerID = Shared.offerNeed.getOfferID();
                    Intent intent = new Intent(ChatAct.this, com.sourcey.materiallogindemo.OfferResult.class);
                    intent.putExtra("type", Shared.offerNeed.getType());
                    intent.putExtra("build_type", Shared.offerNeed.getBuildingTyp());
                    intent.putExtra("price", Shared.offerNeed.getPrice());
                    intent.putExtra("city", Shared.offerNeed.getCity());
                    Shared.putOfferOnMap = Shared.offerNeed;
                    startActivity(intent);
                    Shared.MyOffer = Shared.offerNeed;

                } else {
                    Shared.toID = Shared.offerNeed.getUID();
                    Shared.offer = Shared.offerNeed;
                    Shared.offerID =Shared.offerNeed.getOfferID();
                    Intent intent = new Intent(ChatAct.this, com.sourcey.materiallogindemo.OfferResult.class);
                    intent.putExtra("type", Shared.offerNeed.getType());
                    intent.putExtra("build_type", Shared.offerNeed.getBuildingTyp());
                    intent.putExtra("price",Shared.offerNeed.getPrice());
                    intent.putExtra("city", Shared.offerNeed.getCity());
                    intent.putExtra("userID", Shared.offerNeed.getUID());
                    intent.putExtra("offerID",Shared.offerNeed.getOfferID());
                    Shared.putOfferOnMap = Shared.offerNeed;
                    startActivity(intent);
                    Shared.MyOffer =Shared.offerNeed;
                }
            }
        });
        getOffer();
        if(Shared.MyOffer!=null&&Shared.MyOffer.getType()!=null)
        result.setText(Shared.MyOffer.getType()+" "+Shared.MyOffer.getBuildingTyp()+" "+Shared.MyOffer.getPrice()+" "+"ريال");
        if(Shared.offerNeed!=null) {
            order.setText(Shared.offerNeed.getType() + "  " + Shared.offerNeed.getBuildingTyp() + " " + Shared.offerNeed.getPrice() + " ريال ");
        }if(Shared.offerKnow!=null) {
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
        phoneumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(ChatAct.this, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ChatAct.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            Integer.parseInt("123"));
                } else {
                    startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+phoneNumber)));
                }

                String number=phoneumber.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));
                startActivity(callIntent);
            }
        });
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
                            phoneNumber=user.getPhoneNumber();
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
        else if(Shared.chatOfferId!=null){
            if(Shared.chatOfferId!=null)
                IDS=(Shared.chatOfferId.split("\\*"));
            final String finalID = IDS[0];
            FirebaseDatabase.getInstance().getReference().child("user").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dt:dataSnapshot.getChildren()){
                        User user=dt.getValue(User.class);
                        if(user.getUserID().equals(finalID)){
                            phoneumber.setText(user.getName());
                            phoneNumber=user.getPhoneNumber();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
            readMessage(FirebaseAuth.getInstance().getUid(),Shared.sent_id,"defualt");
        getUserData(Shared.sent_id);
        if(Shared.AllMesage){
         //   blockingTxt.setVisibility(View.GONE);
          //  cv1.setVisibility(View.GONE);
       //     cv2.setVisibility(View.GONE);
        }
    }
    public void Finish(View view){
        finish();
    }
    private void readMessage(final String myID, final String userID, final String imageURL){
        mChat=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean check=dataSnapshot.child("Chats").exists();
                if(check)
                    database.getReference("Chats").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null)
//                                    && dataSnapshot.hasChild(Shared.offerKnow.getOfferID()))
                            {
                                DatabaseReference mReference=database.getReference("Chats");
//                                        .child(Shared.offerKnow.getOfferID());
                                mReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mChat.clear();
                                        for(DataSnapshot dt:dataSnapshot.getChildren()){
                                            Chat chat=dt.getValue(Chat.class);
                                            if(chat!=null)
                                                if(chat.getSender().equals(myID)&&chat.getReciver().equals(userID)||
                                                        chat.getSender().equals(userID)&&chat.getReciver().equals(myID)) {
                                                    if (chat.isIsblock()) {
                                                        btt.setVisibility(View.GONE);
                                                        blockingTxt.setVisibility(View.GONE);
                                                        //ubblockingTxt.setVisibility(View.VISIBLE);
                                                    }
                                                    if (chat != null && chat.getId() != null)
                                                        if (chat.getId().equals(Shared.chatOfferId)
                                                                || (Shared.offerKnow != null &&
                                                                chat.getId().equals(Shared.offerKnow.getOfferID())
                                                        )) {
                                                            mChat.add(chat);
                                                            Shared.offerNeed = chat.getOfferNeed();
                                                            order.setText(Shared.offerNeed.getType() + "-" + Shared.offerNeed.getBuildingTyp() + "-" + Shared.offerNeed.getPrice() + " ريال ");
                                                        }
                                                }
                                            seenMessage(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
    private void block(String sender, final String receiver, String message){
        bottom.setVisibility(View.GONE);
        DatabaseReference mReference=database.getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        hashMap.put("isblock",true);
        hashMap.put("offerNeed",Shared.offerNeed);

        mReference.child("Chats").push().setValue(hashMap);



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
    private void unblock(String sender, final String receiver, String message){

        bottom.setVisibility(View.GONE);
        DatabaseReference mReference=database.getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        hashMap.put("isblock",false);
        hashMap.put("offerNeed",Shared.offerNeed);
        mReference.child("Chats").push().setValue(hashMap);



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
    private void sentMeeage(String sender, final String receiver, String message){
        DatabaseReference mReference=database.getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciver",receiver);
        hashMap.put("message",message);
        hashMap.put("id",Shared.offerKnow!=null?Shared.offerKnow.getOfferID():Shared.chatOfferId);
        hashMap.put("isseen",false);
        hashMap.put("isblock",false);
        hashMap.put("offerNeed",Shared.offerNeed);
        MediaPlayer mp = MediaPlayer.create(this,R.raw.sent);
        mp.start();
        mReference.child("Chats").push().setValue(hashMap);
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
                                         //   Toast.makeText(ChatAct.this, "failed", Toast.LENGTH_SHORT).show();
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
   //        phoneumber.setText(Shared.offerKnow.getSpinnerType());

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
        Shared.close=true;
        finish();
    }

    public void openOfferDetalis(View view) {
        Intent intent=new Intent(this,BuildDetial.class);
        intent.putExtra("enable","0");
        startActivity(intent);
    }

}
