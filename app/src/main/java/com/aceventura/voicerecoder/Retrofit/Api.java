package com.aceventura.voicerecoder.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Retrofit retrofit = null;
    private static Api clientobject;

    public static synchronized Api getInstance()
    {
        if(clientobject==null)
            clientobject=new Api();
        return clientobject;
    }

    public ApiInterface getapi()
    {
        return retrofit.create(ApiInterface.class);
    }

    public static ApiInterface getInfo() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("parameter", "value").build();
                return chain.proceed(request);
            }
        });



        //  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            //   OkHttpClient okhttpclient = NullOnEmptyConverterFactory.getUnsafeOkHttpClient();
            OkHttpClient okhttpclient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();


//            val okHttpClient = OkHttpClient.Builder()
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .connectTimeout(30, TimeUnit.SECONDS)
//                    .addInterceptor(interceptor)
//                    .build()
            retrofit = new Retrofit.Builder()
                    .baseUrl(WebApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okhttpclient).build();

        }

        ApiInterface api = retrofit.create(ApiInterface.class);

        return api;

    }
}
