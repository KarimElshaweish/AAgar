package com.sourcey.materiallogindemo.Service;

import com.sourcey.materiallogindemo.Notification.MyResponse;
import com.sourcey.materiallogindemo.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


    @Headers
            (
            {
                        "Content-Type:application/json",
                        "Authorization: key=AAAARKmYlz0:APA91bF8kIv-N7hQkhVMe3fEePGoXAN2mH5WhBg5nZEgOmf2uFOvdG2z0iEI1EsaEjM5VzOKoRp4kvia4-DvLCdUfi5ZSQbH-PoADor1CyMZZGgQRGUayVpPFjKY_CW01alJEZC1lxmb"
            }
            )
    @POST("fcm/send")
    Call<MyResponse> sendNotificaiton(@Body Sender body);

}
