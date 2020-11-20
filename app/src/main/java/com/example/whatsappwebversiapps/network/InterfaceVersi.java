package com.example.whatsappwebversiapps.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceVersi {
    @GET("kontak/coba")
    Call<ResponseBody> getVersi(@Query("id") String id);
}
