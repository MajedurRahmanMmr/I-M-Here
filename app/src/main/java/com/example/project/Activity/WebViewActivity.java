package com.example.project.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;
import com.example.project.DB.TokenFitbit;
import com.example.project.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.realm.Realm;
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

        WebView webView = findViewById(R.id.webViewFitbit);
        webView.setWebViewClient(new WebViewClient());
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (url.contains("https://192.168.0.102:8080/index/html?code=")) {
                    // https://192.168.0.102:8080/index/html?code=6e047f84a0946920b63f08b7249ee9d8728500fc
                    String token = url.replace(getString(R.string.redirectUrl), "");
                    token = token.replace("#_=_", "");


                    final OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "grant_type=authorization_code&code=" + token + "&redirect_uri=https%3A%2F%2F192.168.0.102%3A8080%2Findex%2Fhtml");
                    final Request request = new Request.Builder()
                            .url("https://api.fitbit.com/oauth2/token")
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("authorization", "Basic MjJENTc1OmJjN2Y0NmY0YTJjZjNhYTk2YTdhMmJhODMwOTk4YjQ0")
                            .addHeader("cache-control", "no-cache")
                            .addHeader("postman-token", "7cd623c7-90fb-5bad-4a3b-ac3e0b0337fb")
                            .build();


                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {


                                Response response = client.newCall(request).execute();
                                if (response.body() != null) {


                                    JSONObject dbJsonObj = new JSONObject(response.body().string());

                                    final String Tocken = (String) dbJsonObj.get("access_token");
                                    final String user_id = (String) dbJsonObj.get("user_id");
                                    SharedPreferences sharedpreferences = getSharedPreferences("FitbitToken", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(getString(R.string.fitbit_token_key), Tocken);
                                    editor.putString(getString(R.string.fitbit_user_id_key), user_id);
                                    editor.commit();


                                    Realm realm = Realm.getDefaultInstance();


                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.where(TokenFitbit.class).findAll().deleteAllFromRealm();

                                            realm.copyToRealmOrUpdate(new TokenFitbit(Tocken, user_id));

                                        }
                                    });



                                    OkHttpClient client = new OkHttpClient();

                                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                                    RequestBody body = RequestBody.create(mediaType, "client_id%3D22942C%26grant_type%3Dauthorization_code%26redirect_uri%3Dhttp%253A%252F%252Fexample.com%252Ffitbit_auth%26code%3D1234567890%26code_verifier%3DdBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk=");
                                    Request request = new Request.Builder()
                                            .url("https://api.fitbit.com/1/user/-/profile.json")
                                            .get()
                                            .addHeader("authorization", "Bearer " + Tocken)
                                            .addHeader("cache-control", "no-cache")
                                            .addHeader("postman-token", "fda8868f-d9e5-47dd-0481-2b0c41fa5601")
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

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    thread.start();

                }

            }
        });


        webView.loadUrl("https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=22D575&redirect_uri=https%3A%2F%2F192.168.0.102%3A8080%2Findex%2Fhtml&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800");
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
