package com.sourcey.materiallogindemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.Notification.Client;
import com.sourcey.materiallogindemo.Notification.Data;
import com.sourcey.materiallogindemo.Notification.MyResponse;
import com.sourcey.materiallogindemo.Notification.Sender;
import com.sourcey.materiallogindemo.Notification.Token;
import com.sourcey.materiallogindemo.Service.APIService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAct extends AppCompatActivity {

    TextView phoneumber;
    RecyclerView recyclerView;

    ImageView sentImage,img;
    EditText sentText;
    LinearLayout bottom;
    TextView price,city,street,desc;
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

    ValueEventListener sentListener;
    @Override
    protected void onPause() {
        Shared.AllMesage=false;
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    TextView order,result,offerOrderName, offerType, offerplace;
    TextView offerPrice;
    User user;
    CardView cv1,cv2;
    private void  getUserData(String usreID){
        if(avatar==null)
            avatar=findViewById(R.id.avatar);
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
                        Glide.with(ChatAct.this).load(user.getProfilePic()).placeholder(R.drawable.avatar_logo)
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_block:
                btt.setVisibility(View.GONE);
                blockingTxt.setVisibility(View.GONE);
                // ubblockingTxt.setVisibility(View.VISIBLE);
                block(FirebaseAuth.getInstance().getCurrentUser().getUid(), Shared.sent_id, "block");
                return true;
            case R.id.action_call:
                int permissionCheck = ContextCompat.checkSelfPermission(ChatAct.this, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ChatAct.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            Integer.parseInt("123"));
                } else {
                    String number=phoneumber.getText().toString();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+number));
                    startActivity(callIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    LinearLayout orderLayout;
    Toolbar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        try{

            titleBar=findViewById(R.id.titleBar);
            setSupportActionBar(titleBar);
         //   TextView offerOwner=findViewById(R.id.offerOwner);
//            if(Shared.user!=null&&Shared.user.getType().equals(Shared.Array[1])){
//                LinearLayout client_name_section=findViewById(R.id.client_name_section);
//                client_name_section.setVisibility(View.GONE);
//                orderLayout.setVisibility(View.GONE);
//            }else{
//                result.setVisibility(View.GONE);
//            }
       //     bottom = findViewById(R.id.bottom);
            // ubblockingTxt=findViewById(R.id.ubblocking);

//            if(Shared.MyOffer!=null&&Shared.MyOffer.getType()!=null)
//                result.setText(Shared.MyOffer.getType()+" "+Shared.MyOffer.getBuildingTyp()+" "+Shared.MyOffer.getPrice()+" "+"ريال");
//            offerplace=findViewById(R.id.offerplace);



            phoneumber=findViewById(R.id.phoneumber);
//            phoneumber.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {

//            });
            getUserData(Shared.sent_id);
            sentImage=findViewById(R.id.sentImage);
            sentText=findViewById(R.id.text_sent);
            FirebaseDatabase.getInstance().getReference().child("user").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dt:dataSnapshot.getChildren()){
                        User user=dt.getValue(User.class);
                        if(user.getUserID().equals(Shared.sent_id)){
                            phoneumber.setText(user.getName());

//                        TextView offerOrderName=findViewById(R.id.offerOrderName);
//                        offerOrderName.setText(user.getName());
                            phoneNumber=user.getPhoneNumber();
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            recyclerView=findViewById(R.id.rv);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            if(Shared.AllMesage){
                //   blockingTxt.setVisibility(View.GONE);
                //  cv1.setVisibility(View.GONE);
                //     cv2.setVisibility(View.GONE);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
    public void Finish(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shared.allUsersChat=false;
    }
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    private void block(String sender, final String receiver, String message){
        bottom.setVisibility(View.GONE);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm",Locale.US);
        String nowTime=sdf.format(new Date());
        DatabaseReference mReference=database.getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        hashMap.put("isblock",true);
        hashMap.put("time",nowTime);
        hashMap.put("offerNeed",Shared.offerNeed);

        mReference.child("Chats").push().setValue(hashMap);



        final String  msg=message;
        mReference=FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                    sendNotificaion(receiver,user.getName(),msg);
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
        mReference=FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                    sendNotificaion(receiver,user.getName(),msg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    APIService apiService;
    private  void sendNotificaion(String reciver, final String name, final String msg){
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(reciver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Token token = dt.getValue(Token.class);
                    Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid()
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




}
