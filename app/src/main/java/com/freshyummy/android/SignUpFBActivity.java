package com.freshyummy.android;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * Created by afi on 08/05/18.
 */

public class SignUpFBActivity extends AppCompatActivity {
    public static String action, idfacebook, nama, email, telepon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_fb);
        final EditText etnama = findViewById(R.id.editText5);
        final EditText etphone = findViewById(R.id.editText4);
        final EditText etemail = findViewById(R.id.editText);
        TextView tvdeskripsi = findViewById(R.id.textView9);
        final Button btnaction = findViewById(R.id.button);

        action = getIntent().getStringExtra("ACTION");
        idfacebook = getIntent().getStringExtra("EXTRA_IDFB");
        nama = getIntent().getStringExtra("EXTRA_NAME");
        email = getIntent().getStringExtra("EXTRA_EMAIL");
        telepon = getIntent().getStringExtra("EXTRA_PHONE");

        if (action.equals("AVAILABLE")){
            tvdeskripsi.setText("Anda sudah mempunyai akun Fresh Yummy dengan email yang sama. Silahkan lanjutkan untuk menghubungkan dengan facebook.");
            btnaction.setText("Lanjutkan");
        }

        etphone.setText(telepon);
        etemail.setText(email);
//        etemail.setEnabled(false);
        etnama.setText(nama);

        btnaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnaction.getText().toString().equals("Lanjutkan")){
                    updateidfacebook();
                }else {
                    email = etemail.getText().toString().trim();
                    telepon = etphone.getText().toString().trim();
                    nama = etnama.getText().toString().trim();

                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(nama) || TextUtils.isEmpty(telepon)){
                        Snackbar.make(findViewById(android.R.id.content), "Harap lengkapi semua data yang dibutuhkan",
                                Snackbar.LENGTH_LONG).show();
                    }else {
                        if (!Utilities.isValidEmail(email)) {
                            requestFocus(etemail);
                        } else {
                            Utilities.hideKeyboard(SignUpFBActivity.this);
//                            SetUser();
                            Intent intent = new Intent(SignUpFBActivity.this, VerifyPhoneActivity.class);
                            intent.putExtra("EXTRA_PHONE", "+62"+telepon);
                            intent.putExtra("ACTION", "FB");
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignUpFBActivity.this).toBundle());
//                            }else {
                                startActivity(intent);
//                            }
                            finish();
                        }
                    }
                }
            }
        });

        etphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 0) {
                    if (charSequence.toString().equals("0")) {
                        etphone.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strEmail = etemail.getText().toString().trim();
                if (strEmail.isEmpty() || !Utilities.isValidEmail(strEmail)) {
                    etemail.setError("Alamat email tidak valid");
                    requestFocus(etemail);
                } else {
                    etemail.setError(null);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (action.equals("IN")) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                startActivity(new Intent(this, SignInActivity.class), ActivityOptions.makeSceneTransitionAnimation(SignUpFBActivity.this).toBundle());
//            }else {
                startActivity(new Intent(this, SignInActivity.class));
//            }
            finish();
        }else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                startActivity(new Intent(this, SignUpActivity.class), ActivityOptions.makeSceneTransitionAnimation(SignUpFBActivity.this).toBundle());
//            }else {
                startActivity(new Intent(this, SignUpActivity.class));
//            }
            finish();
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
//        Call<SetValue> call = apiService.signupfb(random, email, "+62"+telepon, nama, idfacebook, Utilities.getToken());
//        call.enqueue(new Callback<SetValue>() {
//            @Override
//            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
//                dialog.dismiss();
//                if (response.body() != null) {
//                    int success = response.body().getSuccess();
//                    if (success == 1) {
////                        Log.e("idpelanggan", response.body().getMessage());
//                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                        Date date = new Date();
//                        SignInActivity.users = new ArrayList<>();
//                        SignInActivity.users.add(new User(response.body().getMessage(), nama, telepon, email, dateFormat.format(date), "0", "0", "", ""));
//                        Intent intent = new Intent(SignUpFBActivity.this, VerifyPhoneActivity.class);
//                        intent.putExtra("EXTRA_PHONE", "+62"+telepon);
//                        intent.putExtra("ACTION", "FB");
//                        startActivity(intent);
//                        finish();
//                    } else if (success == 2) {
//                        Snackbar.make(findViewById(android.R.id.content), "Alamat email atau nomor HP sudah terdaftar",
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

    public void updateidfacebook(){
        final ProgressDialog pDialog = new ProgressDialog(SignUpFBActivity.this);
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
        Call<SetValue> call = apiService.updateidfacebook(random, idfacebook, email, Utilities.getToken());
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
//                        Log.e("status", response.body().getMessage());
                        Utilities.setLogin(SignUpFBActivity.this);
                        for (User user : SignInActivity.users) {
                            Utilities.setUser(SignUpFBActivity.this, user);
                        }
                        Snackbar.make(findViewById(android.R.id.content), "Login succes",
                                Snackbar.LENGTH_LONG).show();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }else {
                        Log.e("status", response.body().getMessage());
                        Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("status", response.body().getMessage());
                    Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
                pDialog.dismiss();
                Log.e("failur", t.getMessage());
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
