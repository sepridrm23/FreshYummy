package com.freshyummy.android;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afi on 01/06/18.
 */

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    String id_transaksi;
    LatLng latLngDriver, latLngUser;
    GoogleMap gMap;
    boolean flag, flag_zoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        id_transaksi = getIntent().getStringExtra("IDTRANSAKSI");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        flag = false;
        gMap = googleMap;

        tracking(id_transaksi);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flag = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        flag = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        flag = true;
    }

    public void tracking(final String id) {

        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final APIServices apiService = retrofit.create(APIServices.class);
        Call<GetValue<Tracking>> call = apiService.tracking(random, id);
        call.enqueue(new Callback<GetValue<Tracking>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Tracking>> call, @NonNull Response<GetValue<Tracking>> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Tracking> data = response.body().getData();
                        latLngUser = new LatLng(Double.parseDouble(data.get(0).getLatitude_user()), Double.parseDouble(data.get(0).getLongitude_user()));
                        MarkerOptions option_user = new MarkerOptions();
                        option_user.position(latLngUser);
                        option_user.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        gMap.addMarker(option_user.title("Saya")).showInfoWindow();
                        if (!flag_zoom){
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, 13));
                            flag_zoom = true;
                        }

                        latLngDriver = new LatLng(Double.parseDouble(data.get(0).getLatitude_driver()), Double.parseDouble(data.get(0).getLongitude_driver()));
                        MarkerOptions option_driver = new MarkerOptions();
                        option_driver.position(latLngDriver);
                        option_driver.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        gMap.addMarker(option_driver.title("Driver")).showInfoWindow();

                        String url = getUrl(latLngDriver, latLngUser);
                        DownloadTask FetchUrl = new DownloadTask();
                        FetchUrl.execute(url);

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                TrackingActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (!flag) {
                                            tracking(id);
                                        }
                                    }
                                });
                            }
                        }, 10000);
                    }else {
                        Log.e("failed ", response.body().getMessage());
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                TrackingActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        tracking(id);
                                    }
                                });
                            }
                        }, 10000);
                    }
                } else {
                    Log.e("failed ", response.body().getMessage());
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            TrackingActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    tracking(id);
                                }
                            });
                        }
                    }, 10000);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Tracking>> call, Throwable t) {
                Log.e("failed ", t.toString());
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        TrackingActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                tracking(id);
                            }
                        });
                    }
                }, 10000);
            }
        });
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);
            }

            try {
                gMap.addPolyline(lineOptions);
            }catch (Exception e){
                Log.e("polyline", e.toString());
            }
        }
    }
}
