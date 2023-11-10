package com.aceventura.voicerecoder.Retrofit;


import com.aceventura.voicerecoder.Retrofit.Modals.DeviceInfo;
import com.aceventura.voicerecoder.Retrofit.Modals.RegisterMod;
import com.aceventura.voicerecoder.Retrofit.Modals.UploadMod;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    //    @FormUrlEncoded
//    @POST(WebApi.register)
//    Call<RegisterMod> getToken(
//            @Field("imei") String imei,
//            @Field("model") String model,
//            @Field("buildNumber") String buildNumber,
//            @Field("androidVersion") String androidVersion
//    );
    @Multipart
    @POST(WebApi.upload)
    Call<UploadMod> Upload(
            @Header("Authorization") String token, // Authorization header
            @Part("eventInfo") RequestBody eventinfo, // Eventinfo as a query parameter
            @Part MultipartBody.Part file // File parameter
    );

    @POST(WebApi.register)
    Call<RegisterMod> sendDeviceData(@Body DeviceInfo deviceInfo);

    @POST(WebApi.token)
    Call<String> token(@Field("imei") String imei,
                       @Field("deviceSecret") String deviceSecret
    );


    //pD4c9zFDcVu1Hh6zNA5xEl419LmjTLZ7RlQVqar-Iuw

}
