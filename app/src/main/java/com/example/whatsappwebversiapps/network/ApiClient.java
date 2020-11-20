package com.example.whatsappwebversiapps.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://www.asia-one.co.id/testingapk/index.php/api/";
    public static InterfaceVersi getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(InterfaceVersi.class);
    }
}
