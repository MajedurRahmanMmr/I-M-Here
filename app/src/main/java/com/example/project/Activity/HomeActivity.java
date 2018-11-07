package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.project.DB.FireBaseDataHelper;
import com.example.project.DB.TokenFitbit;
import com.example.project.Model.CustomLocationWithTime;
import com.example.project.Model.HeartModel.HeartBeat;
import com.example.project.R;
import com.firebase.ui.auth.AuthUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    try {
                        CustomLocationWithTime customLocation = new CustomLocationWithTime(location.getLatitude(), location.getLongitude(), location.getTime());
                        new FireBaseDataHelper(HomeActivity.this).updateUserCurentLocation(customLocation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            ;
        };

      /*  GPSTracker gpsTracker = new GPSTracker(this);
        Toast.makeText(this, gpsTracker.getLocation().getLatitude() +"", Toast.LENGTH_SHORT).show();*/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            CustomLocationWithTime customLocation = new CustomLocationWithTime(location.getLatitude(), location.getLongitude(), location.getTime());
                            new FireBaseDataHelper(HomeActivity.this).updateUserCurentLocation(customLocation);

                        }
                    }
                });


        InitLocationListener();
        new FireBaseDataHelper(this).setUserDataToDB();

        setActions();


      /*  Handler handler = new Handler();
        handler.postDelayed(runnable, 5000);
*/

        heartData();

    }

    private void heartData() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            OkHttpClient client = new OkHttpClient();
                            String Token = getTokenFitbit();
                            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                            RequestBody body = RequestBody.create(mediaType, "client_id%3D22942C%26grant_type%3Dauthorization_code%26redirect_uri%3Dhttp%253A%252F%252Fexample.com%252Ffitbit_auth%26code%3D1234567890%26code_verifier%3DdBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk=");
                            Request request = new Request.Builder()
                                    .url("https://api.fitbit.com/1/user/-/activities/heart/date/today/1d/1sec/time/00:00/00:01.json")
                                    .get()
                                    .addHeader("authorization", "Bearer " + Token)
                                    .addHeader("cache-control", "no-cache")
                                    .addHeader("postman-token", "1cdc2959-cb58-ea5a-7450-325550981cce")
                                    .build();

                            okhttp3.Response response = client.newCall(request).execute();

                            if (response.body() != null) {


                                Gson gson = new Gson();

                                HeartBeat heartBeat = gson.fromJson(response.body().string(), HeartBeat.class);
                                if (heartBeat != null) {

                                    Log.e("Heartbeat", heartBeat.toString());

                                    if (heartBeat.getActivitiesHeart() != null && heartBeat.getActivitiesHeart().size() > 0 && heartBeat.getActivitiesHeart().get(0).getHeartRateZones() != null && heartBeat.getActivitiesHeart().get(0).getHeartRateZones().size() > 0){



                                        final int max = heartBeat.getActivitiesHeart().get(0).getHeartRateZones().get(1).getMax();
                                    final int min = heartBeat.getActivitiesHeart().get(0).getHeartRateZones().get(1).getMin();
                                    final int time = heartBeat.getActivitiesHeart().get(0).getHeartRateZones().get(1).getMinutes();


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomeActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();

                                            String formate = "Min:" + min + "  Max:" + max + " Time:" + time + " Min";
                                            TextView textView = findViewById(R.id.snippetHeart);
                                            textView.setText(formate);

                                        }
                                    });
                                }

                                }


                            } else {

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

                thread.start();
            }
        }, 0, 5000);

    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            heartData();
        }
    };

    private String getTokenFitbit() {
        Realm realm = Realm.getDefaultInstance();
        TokenFitbit first = realm.where(TokenFitbit.class).findFirst();

        if (first != null) {
            return first.getToken();
        }
        return "";
    }

    private void InitLocationListener() {
        Context context = this;

    }

    private void setActions() {
        findViewById(R.id.callEmergencyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkPermission();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.radiusSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RadiusActivity.class));
            }
        });


        findViewById(R.id.trusteeContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TrusteeContactActivity.class));
            }
        });

        findViewById(R.id.health).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, HealthActivity.class));
            }
        });
        findViewById(R.id.comminity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CominityActivity.class));
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AuthUI.getInstance()
                        .signOut(HomeActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            }
                        });
            }
        });

        findViewById(R.id.currentPosition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CurrentPositionActivity.class));
            }
        });

        findViewById(R.id.trustedContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TrustedActivity.class));
            }
        });

        findViewById(R.id.fitbitSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WebViewActivity.class));
            }
        });
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        // LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();


        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                // Toast.makeText(HomeActivity.this, " Location REsponse", Toast.LENGTH_SHORT).show();

                if (locationSettingsResponse.getLocationSettingsStates().isLocationPresent()) {
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        Toast.makeText(HomeActivity.this, " " + location.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }

    private void checkPermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + 100));
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(HomeActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(HomeActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("")
                .setDeniedMessage("")
                .setGotoSettingButtonText("go to Settings")
                .setPermissions(android.Manifest.permission.CALL_PHONE)
                .check();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000 * 5);
        mLocationRequest.setFastestInterval(1000 * 5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
    }


}
