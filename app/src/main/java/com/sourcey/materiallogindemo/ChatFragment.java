package com.sourcey.materiallogindemo;


import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sourcey.materiallogindemo.Adapter.MessageAdapter;
import com.sourcey.materiallogindemo.model.Chat;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.Notification.Client;
import com.sourcey.materiallogindemo.Notification.Data;
import com.sourcey.materiallogindemo.Notification.MyResponse;
import com.sourcey.materiallogindemo.Notification.Sender;
import com.sourcey.materiallogindemo.Notification.Token;
import com.sourcey.materiallogindemo.Service.APIService;
import com.sourcey.materiallogindemo.databinding.FragmentChatBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ChatFragment extends Fragment implements View.OnClickListener {



    boolean notify=false;
    APIService apiService;
    MessageAdapter messageAdapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    ValueEventListener sentListener;
    String []IDS ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentChatBinding fragmentChatBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentChatBinding= DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.fragment_chat,container,false);
        fragmentChatBinding.sentImage.setOnClickListener(this);
        fragmentChatBinding.cv1.setOnClickListener(this);
        fragmentChatBinding.rv.setHasFixedSize(true);

        if(Shared.offerNeed!=null) {
//            fragmentChatBinding.offerType.setText(Shared.offerNeed.getType());
//            fragmentChatBinding.offerplace.setText(Shared.offerNeed.getCity());
//            fragmentChatBinding.offerPrice.setText(Shared.offerNeed.getPrice());
           // fragmentChatBinding.offerOrderName.setText(Shared.offerNeed.getUserName());
          //  fragmentChatBinding.orderneedText.setText(Shared.offerNeed.getBuildingTyp());
         //   fragmentChatBinding.order.setText(Shared.offerNeed.getType() + "  " + Shared.offerNeed.getBuildingTyp() + " " + Shared.offerNeed.getPrice() + " ريال ");
        }if(Shared.offerKnow!=null) {
          //  fragmentChatBinding.bottom.setVisibility(View.GONE);
         //   fragmentChatBinding.orderPriceText.setText(Shared.offerKnow.getPrice());
           // fragmentChatBinding.city.setText(Shared.offerKnow.getCity());
           // fragmentChatBinding.street.setText(Shared.offerKnow.getStreet());
            //fragmentChatBinding.desc.setText(Shared.offerKnow.getDescription());
           // Glide.with(getContext()).load(Shared.offerKnow.getImageList().get(0)).into(fragmentChatBinding.img);

        }else{
           // fragmentChatBinding.view.setVisibility(View.GONE);
        }
        getOffer();
        fragmentChatBinding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        new readMessageTask().execute();
        if(Shared.fristTime) {

            Shared.fristTime=false;
            String msg="";
            Shared.offerKnow.getPrice();
            notify = true;
            if (!msg.equals(""))
                sentMeeage(FirebaseAuth.getInstance().getCurrentUser().getUid(), Shared.sent_id, msg);
            fragmentChatBinding.textSent.setText("");
        }
        else if(Shared.chatOfferId!=null){
            if(Shared.chatOfferId!=null)
                IDS=(Shared.chatOfferId.split("\\*"));
            final String finalID = IDS[0];

        }
        return fragmentChatBinding.getRoot();

    }
    String imageURL;
    List<Chat> mChat;
    private void readMessage(final String myID, final String userID, final String imageURL){
        try {
            this.imageURL = imageURL;
            mChat = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean check = dataSnapshot.child("Chats").exists();
                    if (check)
                        database.getReference("Chats").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null)
//                                    && dataSnapshot.hasChild(Shared.offerKnow.getOfferID()))
                                {
                                    DatabaseReference mReference = database.getReference("Chats");
//                                        .child(Shared.offerKnow.getOfferID());
                                    mReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mChat.clear();
                                            for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                                Chat chat = dt.getValue(Chat.class);
                                                if (chat != null)
                                                    if (chat.getSender().equals(myID) && chat.getReciver().equals(userID) ||
                                                            chat.getSender().equals(userID) && chat.getReciver().equals(myID)) {
                                                        if (chat.isIsblock()) {
                                                            fragmentChatBinding.btt.setVisibility(View.GONE);
                                                            //fragmentChatBinding.blockingTxt.setVisibility(View.GONE);
                                                            //ubblockingTxt.setVisibility(View.VISIBLE);
                                                        }
                                                        if (chat != null && chat.getId() != null)
                                                            if (chat.getId().equals(Shared.chatOfferId)
                                                                    || (Shared.offerKnow != null &&
                                                                    chat.getId().equals(Shared.offerKnow.getOfferID())
                                                            )) {
                                                                if (Shared.allUsersChat)
                                                                    Shared.offerNeed = chat.getOfferNeed();
                                                                if (chat.getOfferNeed().getOfferID().equals(Shared.offerNeed.getOfferID())) {
                                                                    mChat.add(chat);
                                                          //          fragmentChatBinding.orderTypeText.setText(Shared.offerNeed == null ? "" : Shared.offerNeed.getType());
                                                                 //   fragmentChatBinding.offerplace.setText(Shared.offerNeed == null ? "" : Shared.offerNeed.getCity());
                                                            //        fragmentChatBinding.orderPriceText.setText(Shared.offerNeed == null ? "" : Shared.offerNeed.getPrice());
                                                                 //   fragmentChatBinding.offerOrderName.setText(Shared.offerNeed == null ? "" : Shared.offerNeed.getUserName());
                                                            //        if (Shared.offerNeed != null)
                                                            //            fragmentChatBinding.order.setText(Shared.offerNeed + "-" + Shared.offerNeed.getBuildingTyp() + "-" + Shared.offerNeed.getPrice() + " ريال ");
                                                                }
                                                            }
                                                    }

                                            }
                                            try {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        messageAdapter = new MessageAdapter(getActivity(), mChat, imageURL);
                                                        fragmentChatBinding.rv.setAdapter(messageAdapter);
                                                        fragmentChatBinding.rv.scrollToPosition(mChat.size() - 1);
                                                    }
                                                });
                                            }catch (Exception ex){
                                                Log.e("erro",ex.getMessage());
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
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }
    class readMessageTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            readMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),Shared.sent_id,"defualt");
        }

        @Override
        protected Void doInBackground(Void... strings) {
            System.out.println("Done");
            seenMessage(FirebaseAuth.getInstance().getCurrentUser().getUid());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    private void seenMessage(final String userid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        sentListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    Chat chat=dt.getValue(Chat.class);
                    if(chat!=null) {
                        //   String[] offid = chat.getId().split("\\*");
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

    public void sendMessageResponse(){
        String msg =fragmentChatBinding.textSent.getText().toString();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        try {
            notify = true;
            if (!msg.equals(""))
                new firebaseSendingTheMessageTask().execute(msg);
            fragmentChatBinding.textSent.setText("");

        }catch(Exception e){
            System.out.println("hide  the keyboard error: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sentImage:
                sendMessageResponse();
                break;
            case R.id.cv1:
               // openDetails();
                Navigation.findNavController(fragmentChatBinding.getRoot()).navigate(R.id.navigate_chat_to_offer);
                break;
        }
    }
    class firebaseSendingTheMessageTask extends AsyncTask<String,Void,Void> {


        @Override
        protected Void doInBackground(String... msg) {
            sentMeeage(FirebaseAuth.getInstance().getCurrentUser().getUid(), Shared.sent_id, msg[0]);
            return null;
        }
    }

    private void sentMeeage(String sender, final String receiver, String message){
        DatabaseReference mReference=database.getReference();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
        String nowTime=sdf.format(new Date());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciver",receiver);
        hashMap.put("message",message);
        hashMap.put("id",Shared.offerKnow!=null?Shared.offerKnow.getOfferID():Shared.chatOfferId);
        hashMap.put("isseen",false);
        hashMap.put("isblock",false);
        hashMap.put("time",nowTime);
        hashMap.put("offerNeed",Shared.offerNeed);

        mReference.child("Chats").push().setValue(hashMap);
        final String  msg=message;
        mReference= FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    String offID;
    private void getOffer(){
        offID=Shared.offerKnow!=null?Shared.offerKnow.getOfferID():Shared.chatOfferId;
        String uID=(offID.split("\\*"))[0];
        String oID=(offID.split("\\*"))[1];
        FirebaseDatabase.getInstance().getReference("OfferResult")
                .child(uID)
                .child(oID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                com.sourcey.materiallogindemo.model.OfferResult offerResult=dataSnapshot.getValue(com.sourcey.materiallogindemo.model.OfferResult.class);
                if(offerResult!=null) {
                    String  resultText=offerResult.getType()+" "+offerResult.getBuildingType()+
                            " "+offerResult.getPrice()+" "+"ريال";
                   // fragmentChatBinding.result.setText(resultText);
                    Shared.offerKnow = offerResult;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}