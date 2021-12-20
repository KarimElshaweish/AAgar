package com.sourcey.materiallogindemo.Notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sourcey.materiallogindemo.ChatAct;
import com.sourcey.materiallogindemo.MyOfferNeeded;
import com.sourcey.materiallogindemo.R;

public class MyFirebaseMessing extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented=remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(sented!=null&&firebaseUser!=null &&sented.equals(firebaseUser.getUid())){
            sendNotification(remoteMessage);
        }else{
            sentNormalNotification();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sentNormalNotification(){
        Intent intent=new Intent(this, MyOfferNeeded.class);
        Bundle bundle=new Bundle();
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defualtSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("تطبيق مطلوب")
                .setContentText("عرض جديد")
                .setSound(defualtSound)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);
        NotificationManager noti=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        String ChannelId="Your_Channel_id";
        @SuppressLint("WrongConstant") NotificationChannel notificationChannel=new NotificationChannel(ChannelId,"channel human readable",1);
        noti.createNotificationChannel(notificationChannel);
        builder.setChannelId(ChannelId);
        noti.notify(2,builder.build());

        MediaPlayer mp=MediaPlayer.create(this, R.raw.notification);
        mp.start();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(RemoteMessage remoteMessage){
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int j=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this,ChatAct.class);
        Bundle bundle=new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defualtSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(defualtSound)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);
        NotificationManager noti=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        String ChannelId="Your_Channel_id";
        @SuppressLint("WrongConstant") NotificationChannel notificationChannel=new NotificationChannel(ChannelId,"channel human readable",1);
        noti.createNotificationChannel(notificationChannel);
        builder.setChannelId(ChannelId);
        int i=0;
        if(j>0)
            i=j;
        noti.notify(i,builder.build());

        MediaPlayer mp=MediaPlayer.create(this, R.raw.notification);
        mp.start();

    }
}
