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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afi on 24/03/18.
 */

public class SignUpActivity extends AppCompatActivity{
    EditText etEmail, etNama, etTelp;
    CallbackManager callbackManager;
    TextView tvCallUs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_signup);
        LinearLayout llMasuk = findViewById(R.id.ll_masuk);
        etEmail = findViewById(R.id.editText);
        etNama = findViewById(R.id.editText5);
        etTelp = findViewById(R.id.editText4);
        tvCallUs = findViewById(R.id.textView2);
        Button btnDaftar = findViewById(R.id.button);
        Button signinFB = findViewById(R.id.button2);
        final LoginButton login_button = findViewById(R.id.login_button);

        llMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class),  ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this).toBundle());
//                }else {
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
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

        etTelp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0) {
                    if (charSequence.toString().equals("0")) {
                        etTelp.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etEmail.addTextChangedListener(new EmailTextWatcher());

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etEmail.getText().toString().trim()) || TextUtils.isEmpty(etNama.getText().toString().trim()) || TextUtils.isEmpty(etTelp.getText().toString().trim())){
                    Snackbar.make(findViewById(android.R.id.content), "Harap lengkapi semua data yang dibutuhkan",
                            Snackbar.LENGTH_LONG).show();
                }else {
                    if (!Utilities.isValidEmail(etEmail.getText().toString())) {
                        requestFocus(etEmail);
                    } else {
                        Utilities.hideKeyboard(SignUpActivity.this);
//                        SetUser();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        SignInActivity.users = new ArrayList<>();
                        SignInActivity.users.add(new User("", etNama.getText().toString().trim(), etTelp.getText().toString().trim(), etEmail.getText().toString().trim(), dateFormat.format(date), "0", "0", "", ""));

                        Intent intent = new Intent(SignUpActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("EXTRA_PHONE", "+62"+SignInActivity.users.get(0).getTelepon());
                        intent.putExtra("ACTION", "SIGNUP");
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this).toBundle());
//                        }else {
                            startActivity(intent);
//                        }
                        finish();
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

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                AccessToken accessToken = loginResult.getAccessToken();
//                Profile profile = Profile.getCurrentProfile();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.e("aa", ""+response.toString());
//                                try {
//                                    checkfb(object.getString("id"), object.getString("name"), object.getString("email"));
////                                    Toast.makeText(getApplicationContext(), "Hi, " + object.getString("name"), Toast.LENGTH_LONG).show();
//                                } catch(JSONException ex) {
//                                    ex.printStackTrace();
//                                }

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

    private class EmailTextWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String strEmail = etEmail.getText().toString().trim();
            if (strEmail.isEmpty() || !Utilities.isValidEmail(strEmail)) {
                etEmail.setError("Alamat email tidak valid");
                requestFocus(etEmail);
            } else {
                etEmail.setError(null);
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

//    public void SetUser() {
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Loading...");
//        dialog.setCancelable(false);
//        dialog.show();
//
//        String random = Utilities.getRandom(5);
//
//        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Utilities.getBaseURLPhp())
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//
//        final APIServices apiService = retrofit.create(APIServices.class);
//        Call<SetValue> call = apiService.signup(random, etEmail.getText().toString(), "+62"+etTelp.getText().toString().trim(), etNama.getText().toString().trim(), Utilities.getToken());
//        call.enqueue(new Callback<SetValue>() {
//            @Override
//            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
//                dialog.dismiss();
//                if (response.body() != null) {
//                    int success = response.body().getSuccess();
//                    if (success == 1) {
//                        Snackbar.make(findViewById(android.R.id.content), "Akun berhasil dibuat. Silahkan login",
//                                Snackbar.LENGTH_LONG).show();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                                finish();
//                            }
//                        }, 2500);
//                    } else if (success == 2) {
//                        Snackbar.make(findViewById(android.R.id.content), "Alamat email atau nomor HP sudah terdaftar. Silahkan login",
//                                Snackbar.LENGTH_LONG).show();
////                        new Timer().schedule(new TimerTask() {
////                            @Override
////                            public void run() {
////                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
////                                finish();
////                            }
////                        }, 4000);
//                    }else {
//                        Snackbar.make(findViewById(android.R.id.content), "Gagal membuat akun. Silahkan coba lagi",
//                                Snackbar.LENGTH_LONG).show();
//                    }
//                } else {
//                    Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke server",
//                            Snackbar.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
//                dialog.dismiss();
//                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke server",
//                        Snackbar.LENGTH_LONG).show();
//            }
//        });
//    }

    public void checkfb(final String idfacebook, final String nama, final String email){
        final ProgressDialog pDialog = new ProgressDialog(SignUpActivity.this);
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
                        Utilities.setLogin(SignUpActivity.this);
                        for (User user : response.body().getData()) {
                            Utilities.setUser(SignUpActivity.this, user);
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
                        SignInActivity.users = response.body().getData();
                        Intent intent = new Intent(SignUpActivity.this, SignUpFBActivity.class);
                        intent.putExtra("EXTRA_IDFB", idfacebook);
                        intent.putExtra("EXTRA_EMAIL", email);
                        intent.putExtra("EXTRA_NAME", nama);
                        intent.putExtra("EXTRA_PHONE", "");
                        intent.putExtra("ACTION", "OUT");
                        startActivity(intent);
                        finish();
                    }else if (success == 3) {
//                        Log.e("aa", response.body().getMessage());
                        SignInActivity.users = response.body().getData();
                        Intent intent = new Intent(SignUpActivity.this, SignUpFBActivity.class);
                        intent.putExtra("EXTRA_IDFB", idfacebook);
                        intent.putExtra("EXTRA_EMAIL", email);
                        intent.putExtra("EXTRA_NAME", nama);
                        intent.putExtra("EXTRA_PHONE", SignInActivity.users.get(0).getTelepon());
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
