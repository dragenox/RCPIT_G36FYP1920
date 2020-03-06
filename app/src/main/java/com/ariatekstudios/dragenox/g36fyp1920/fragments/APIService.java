package com.ariatekstudios.dragenox.g36fyp1920.fragments;

import com.ariatekstudios.dragenox.g36fyp1920.notifications.MyResponse;
import com.ariatekstudios.dragenox.g36fyp1920.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA-TstZZE:APA91bGWq5qd49ngOD6xPH3M6YxzQSPkcU_pdCLzXxV9VySKA9IU9MiK_Av5NadzW4x8HKKEBQz9kozFgaOmJLjNwqHYDbv2mDtj8aa_vDxEr_XnR2A2Pqjh_S8k9TLoCx3ujtKombqy"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
