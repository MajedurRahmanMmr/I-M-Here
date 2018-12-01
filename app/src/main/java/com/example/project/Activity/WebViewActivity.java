package com.example.project.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.project.DB.TokenFitbit;
import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.realm.Realm;
import io.realm.annotations.PrimaryKey;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        final WebView webView = findViewById(R.id.webViewFitbit);
        webView.setWebViewClient(new WebViewClient());
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setEnableSmoothTransition(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        findViewById(R.id.webViewCacheClear).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.where(TokenFitbit.class).findAll().deleteAllFromRealm();
                            }
                        });

                        Toast.makeText(WebViewActivity.this, "Existing Connection Removed", Toast.LENGTH_SHORT).show();
                        webView.clearCache(true);
                        webView.clearFormData();
                        webView.clearSslPreferences();
                        webView.clearHistory();
                        finish();
                    }
                });


        wevViewSetup(webView);
    }

    public void wevViewSetup(WebView webView) {

/*

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Toast.makeText(WebViewActivity.this, "onPageStarted", Toast.LENGTH_SHORT).show();

                Log.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);


                Toast.makeText(WebViewActivity.this, "onPageCommitVisible", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);


                try {


                    if (url.contains("https://192.168.0.102:8080/index/html#access_token=")) {


                        String token = url.replace("https://192.168.0.102:8080/index/html#access_token=", "").split("&user_id")[0];


                        OkHttpClient client = new OkHttpClient();
                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "client_id%3D22942C%26grant_type%3Dauthorization_code%26redirect_uri%3Dhttp%253A%252F%252Fexample.com%252Ffitbit_auth%26code%3D1234567890%26code_verifier%3DdBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk=");
                        Request request = new Request.Builder()
                                .url("https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json")
                                .get()
                                .addHeader("authorization", "Bearer " + token)
                                .addHeader("cache-control", "no-cache")
                                .addHeader("postman-token", "5fae747b-29fd-07e5-d02c-26af0f8dacd0")
                                .build();


                        Response responseUser = client.newCall(request).execute();


                        JSONObject userOBJECT = null;
                        if (responseUser.body() != null) {
                            userOBJECT = new JSONObject(responseUser.body().string());


                            String data = userOBJECT.get("user").toString();

                            JSONObject userMain = new JSONObject(data);

                            final String name = (String) userMain.get("displayName");

                            Log.e("Response", name);


                            finish();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(WebViewActivity.this, "Connection Established with " + name, Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                Toast.makeText(WebViewActivity.this, "onLoadResource", Toast.LENGTH_SHORT).show();

                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(WebViewActivity.this, "shouldOverrideUrlLoading", Toast.LENGTH_SHORT).show();

                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

*/

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.locading).setVisibility(View.GONE);

                if (url.contains("https://192.168.0.102:8080/index/html#access_token=")) {


                    final String token = url.replace("https://192.168.0.102:8080/index/html#access_token=", "").split("&user_id")[0];


                    Log.e("Token", token);

                    final OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "client_id%3D22942C%26grant_type%3Dauthorization_code%26redirect_uri%3Dhttp%253A%252F%252Fexample.com%252Ffitbit_auth%26code%3D1234567890%26code_verifier%3DdBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk=");
                    final Request request = new Request.Builder()
                            .url("https://api.fitbit.com/1/user/-/profile.json")
                            .get()
                            .addHeader("authorization", "Bearer " + token)
                            .addHeader("cache-control", "no-cache")
                            .addHeader("postman-token", "7f880a77-7777-c4b1-c0ee-644a5c84bd80")
                            .build();


                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final Response response = client.newCall(request).execute();


                                if (response.body() != null) {


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {

                                                JSONObject jsonObject = new JSONObject(response.body().string());


                                                String data = null;

                                                data = jsonObject.get("user").toString();

                                                JSONObject userMain = new JSONObject(data);


                                                final String fullName = userMain.getString("fullName");
                                                Toast.makeText(WebViewActivity.this, "Connected With " + fullName, Toast.LENGTH_SHORT).show();


                                                Realm realm = Realm.getDefaultInstance();
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {


                                                        realm.where(TokenFitbit.class).findAll().deleteAllFromRealm();
                                                        TokenFitbit object = new TokenFitbit(token, 0 + "");

                                                        object.userName = fullName;
                                                        realm.copyToRealmOrUpdate(object);
                                                    }
                                                });
                                                finish();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }

            }
        });


        String url = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=22D575&redirect_uri=https%3A%2F%2F192.168.0.102%3A8080%2Findex%2Fhtml&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800";
        webView.loadUrl(url);
    }

    private String getTokenFitbit() {
        SharedPreferences prefs = getSharedPreferences("FitbitToken", Context.MODE_PRIVATE);
        String restoredText = prefs.getString("FitbitAccessToken", null);
        return restoredText;
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
