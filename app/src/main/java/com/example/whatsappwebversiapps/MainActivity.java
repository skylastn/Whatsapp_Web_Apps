package com.example.whatsappwebversiapps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsappwebversiapps.network.ApiClient;
import com.example.whatsappwebversiapps.network.InterfaceVersi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    protected WebView webView;

    private InterfaceVersi mApiInterface;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);

        mApiInterface = ApiClient.getAPIService();
        getVersiWA();
    }

    private void getVersiWA(){
        mApiInterface.getVersi("1")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                String nama = jsonRESULTS.getString("versi");
//                                Toast.makeText(MainActivity.this, nama, Toast.LENGTH_LONG).show();
                                webView.loadUrl(getString(R.string.whatsapp_web_url));
                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.getSettings().setBuiltInZoomControls(true);
                                webView.getSettings().setDomStorageEnabled(true);
                                webView.getSettings().setDisplayZoomControls(false);
                                webView.getSettings().setUserAgentString(nama);
                                webView.setWebViewClient(new NavWebViewClient());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    private class NavWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return handleUri(uri);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            return handleUri(uri);
        }

        private boolean handleUri(final Uri uri) {
            final String host = uri.getHost();
            if (host.contains(getString(R.string.whatsapp_host))) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            ActivityManager activityManager = ((ActivityManager) getSystemService(ACTIVITY_SERVICE));
            if (activityManager != null) activityManager.clearApplicationUserData();
        }
    }
}