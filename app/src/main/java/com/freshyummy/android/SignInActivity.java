package com.freshyummy.android;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afi on 24/03/18.
 */

public class SignInActivity extends AppCompatActivity {
    EditText etPhoneEmail;
    public static List<User> users;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_signin);
        etPhoneEmail = findViewById(R.id.editText);
        Button signinFB = findViewById(R.id.button2);
        Button btnMasuk = findViewById(R.id.button);
        TextView tvCallUs = findViewById(R.id.textView2);
        final LoginButton login_button = findViewById(R.id.login_button);
        LinearLayout llDaftar = findViewById(R.id.ll_daftar);

        llDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class),  ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this).toBundle());
//                }else {
                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
//                }
                finish();
            }
        });

        tvCallUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "081278909143";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneEmail = etPhoneEmail.getText().toString();
                Utilities.hideKeyboard(SignInActivity.this);
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                if (TextUtils.isEmpty(phoneEmail)) {
                    etPhoneEmail.setError("Silahkan masukan email atau no. HP");
                }else {
                    if (etPhoneEmail.getText().toString().substring(0,1).equals("0")){
                        if (phoneEmail.substring(0,1).equals("0")){
                            phoneEmail = "+62"+phoneEmail.substring(1,phoneEmail.length());
                        }
                        signin(phoneEmail, "");
//                        Log.e("phone number", phone);
//                        Intent intent = new Intent(SignInActivity.this, VerifyPhoneActivity.class);
//                        intent.putExtra("EXTRA_PHONE", phone);
//                        startActivity(intent);
//                        finish();
                    }else if (etPhoneEmail.getText().toString().substring(0,1).equals("+")){
                        signin(phoneEmail, "");
//                        Log.e("phone number", phone);
//                        Intent intent = new Intent(SignInActivity.this, VerifyPhoneActivity.class);
//                        intent.putExtra("EXTRA_PHONE", phone);
//                        startActivity(intent);
//                        finish();
                    }else {
                        signin("", phoneEmail);
                    }
                }
            }
        });

        signinFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_button.setReadPermissions(Arrays.asList("email", "public_profile"));
                login_button.callOnClick();
            }
        });

//        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//
//            }
//        };
//
//        ProfileTracker profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//
//            }
//        };

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                AccessToken accessToken = loginResult.getAccessToken();
//                Profile profile = Profile.getCurrentProfile();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("aa", ""+response.toString());
                                String id = "", nama = "", email = "";
                                try {
                                    id = object.getString("id");
                                    nama = object.getString("name");
                                    email = object.getString("email");
//                                    checkfb(object.getString("id"), object.getString("name"), object.getString("email"));
//                                    Toast.makeText(getApplicationContext(), "Hi, " + object.getString("name"), Toast.LENGTH_LONG).show();
                                } catch(JSONException ex) {
                                    ex.printStackTrace();
                                }
                                checkfb(id, nama, email);
                                Log.e("aa", id+nama+email);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
//                Toast.makeText(getApplication(), "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
//                Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void signin(String phone, String email) {
        final ProgressDialog pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        APIServices api = retrofit.create(APIServices.class);
        Call<GetValue<User>> call = api.signin(random, email, phone, Utilities.getToken());
        call.enqueue(new Callback<GetValue<User>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<User>> call, @NonNull Response<GetValue<User>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        users = response.body().getData();
                        Intent intent = new Intent(SignInActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("EXTRA_PHONE", users.get(0).getTelepon());
                        intent.putExtra("ACTION", "SIGNIN");
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this).toBundle());
//                        }else {
                            startActivity(intent);
//                        }
                        finish();
                    }else if (success == 2) {
                        Snackbar.make(findViewById(android.R.id.content), "Alamat email atau nomor HP tidak terdaftar",
                                Snackbar.LENGTH_LONG).show();
                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    System.out.println("Response:" + response.message());
                    Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<User>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    public void checkfb(final String idfacebook, final String nama, final String email){
        final ProgressDialog pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        String random = Utilities.getRandom(5);

//        Log.e("data sent", random + users.getIdpelanggan()+ users.getNama()+ alamatpengantaran+ String.valueOf(currentLatLng.latitude)+
//                String.valueOf(currentLatLng.longitude)+ users.getTelepon()+ addressnote+ productnote+ jenisBayar+
//                tanggalpengantaran+ ongkir+ poinkeluar+ String.valueOf(poinmasuk));

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final APIServices apiService = retrofit.create(APIServices.class);
        Call<GetValue<User>> call = apiService.checkfacebook(random, idfacebook, email, Utilities.getToken());
        call.enqueue(new Callback<GetValue<User>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<User>> call, @NonNull Response<GetValue<User>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        Utilities.setLogin(SignInActivity.this);
                        for (User user : response.body().getData()) {
                            Utilities.setUser(SignInActivity.this, user);
                        }
                        Snackbar.make(findViewById(android.R.id.content), "Login succes",
                                Snackbar.LENGTH_LONG).show();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    } else if (success == 2) {
//                        Log.e("aa", response.body().getMessage());
//                        users = response.body().getData();
                        Intent intent = new Intent(SignInActivity.this, SignUpFBActivity.class);
                        intent.putExtra("EXTRA_IDFB", idfacebook);
                        intent.putExtra("EXTRA_EMAIL", email);
                        intent.putExtra("EXTRA_NAME", nama);
                        intent.putExtra("EXTRA_PHONE", "");
                        intent.putExtra("ACTION", "IN");
                        startActivity(intent);
                        finish();
                    }else if (success == 3) {
//                        Log.e("aa", response.body().getMessage());
                        users = response.body().getData();
                        Intent intent = new Intent(SignInActivity.this, SignUpFBActivity.class);
                        intent.putExtra("EXTRA_IDFB", idfacebook);
                        intent.putExtra("EXTRA_EMAIL", email);
                        intent.putExtra("EXTRA_NAME", nama);
                        intent.putExtra("EXTRA_PHONE", users.get(0).getTelepon());
                        intent.putExtra("ACTION", "AVAILABLE");
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                        Log.e("status", response.body().getMessage());
                    }
                } else {
                    Log.e("status", response.body().getMessage());
                    Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<User>> call, Throwable t) {
                pDialog.dismiss();
                Log.e("failur", t.getMessage());
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
