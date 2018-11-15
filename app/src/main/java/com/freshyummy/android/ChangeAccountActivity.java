package com.freshyummy.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afi on 24/04/18.
 */

public class ChangeAccountActivity extends AppCompatActivity {
    EditText et_nama, et_email, et_no_telp;
    ImageView ib_close_phone, ib_close_email, ib_close_name, back;
    Button btn_save;
    User user;
    public static List<User> users = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);
        et_nama = findViewById(R.id.et_nama);
        et_email = findViewById(R.id.et_email);
        et_no_telp = findViewById(R.id.et_no_telp);
        ib_close_email = findViewById(R.id.ib_close_email);
        ib_close_name = findViewById(R.id.ib_close_name);
        ib_close_phone = findViewById(R.id.ib_close_phone);
        btn_save = findViewById(R.id.btn_save);
        back = findViewById(R.id.back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        user = Utilities.getUser(this);
        btn_save.setEnabled(false);

        et_nama.setText(user.getNama());
        et_email.setText(user.getEmail());
        et_no_telp.setText(user.getTelepon());

        et_nama.post(new Runnable() {
            @Override
            public void run() {
                et_nama.setSelection(et_nama.getText().toString().length());
            }
        });

        ib_close_phone.setVisibility(View.GONE);
        ib_close_name.setVisibility(View.GONE);
        ib_close_email.setVisibility(View.GONE);

        et_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ib_close_name.setVisibility(View.VISIBLE);
                ib_close_email.setVisibility(View.GONE);
                ib_close_phone.setVisibility(View.GONE);
                btn_save.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strName = et_nama.getText().toString().trim();
                if (strName.isEmpty()){
                    et_nama.setError("Nama harus diisi");
                }
            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ib_close_name.setVisibility(View.GONE);
                ib_close_email.setVisibility(View.VISIBLE);
                ib_close_phone.setVisibility(View.GONE);
                btn_save.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strEmail = et_email.getText().toString().trim();
                if (strEmail.isEmpty()){
                    et_email.setError("Alamat email harus diisi");
                }else if (!Utilities.isValidEmail(strEmail)) {
                    et_email.setError("Alamat email tidak valid");
                }else {
                    et_email.setError(null);
                }
            }
        });

        et_no_telp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ib_close_name.setVisibility(View.GONE);
                ib_close_email.setVisibility(View.GONE);
                ib_close_phone.setVisibility(View.VISIBLE);
                btn_save.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 3) {
                    if (charSequence.toString().equals("+620")) {
                        et_no_telp.setText("+62");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 3){
                    et_no_telp.setError("Nomor telepon harus diisi");
                    et_no_telp.setText("+62");
                    et_no_telp.post(new Runnable() {
                        @Override
                        public void run() {
                            et_no_telp.setSelection(et_no_telp.getText().toString().length());
                        }
                    });
                }
            }
        });

        ib_close_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_nama.setText("");
            }
        });

        ib_close_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_email.setText("");
            }
        });

        ib_close_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_no_telp.setError("Nomor telepon harus diisi");
                et_no_telp.setText("+62");
                et_no_telp.post(new Runnable() {
                    @Override
                    public void run() {
                        et_no_telp.setSelection(et_no_telp.getText().toString().length());
                    }
                });
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_nama.getText().toString().isEmpty()){
                    et_nama.setError("Nama harus diisi");
                }
                else if (et_email.getText().toString().isEmpty()){
                    et_email.setError("Alamat email harus diisi");
                }
                else if (et_no_telp.getText().toString().equals("+62")){
                    et_no_telp.setError("Nomor telepon harus diisi");
                }else {
                    if (!et_no_telp.getText().toString().equals(user.getTelepon())){
                        if (!et_email.getText().toString().equals(user.getEmail())){
                            users.add(new User(user.getIdpelanggan(), et_nama.getText().toString(), et_no_telp.getText().toString(),
                                    et_email.getText().toString(), user.getTanggaldaftar(), user.getStatusverifikasiemail(), user.getPoin(),
                                    user.getKodereferensi(), user.getReferensipelanggan()));
                        }else {
                            users.add(new User(user.getIdpelanggan(), et_nama.getText().toString(), et_no_telp.getText().toString(),
                                    et_email.getText().toString(), user.getTanggaldaftar(), "0", user.getPoin(),
                                    user.getKodereferensi(), user.getReferensipelanggan()));
                        }
                        Intent intent = new Intent(ChangeAccountActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("EXTRA_PHONE", et_no_telp.getText().toString());
                        intent.putExtra("ACTION", "CHANGE");
                        startActivity(intent);
                    }else if (!et_email.getText().toString().equals(user.getEmail())){
                        users.add(new User(user.getIdpelanggan(), et_nama.getText().toString(), et_no_telp.getText().toString(),
                                et_email.getText().toString(), user.getTanggaldaftar(), "0", user.getPoin(),
                                user.getKodereferensi(), user.getReferensipelanggan()));
                        updateakun(1);
                    }else if (!et_nama.getText().toString().equals(user.getNama())){
                        users.add(new User(user.getIdpelanggan(), et_nama.getText().toString(), et_no_telp.getText().toString(),
                                et_email.getText().toString(), user.getTanggaldaftar(), user.getStatusverifikasiemail(), user.getPoin(),
                                user.getKodereferensi(), user.getReferensipelanggan()));
                        updateakun(2);
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void updateakun(final int action){
        final ProgressDialog pDialog = new ProgressDialog(ChangeAccountActivity.this);
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

        final APIServices apiService = retrofit.create(APIServices.class);
        Call<SetValue> call = apiService.updateakun(random, user.getIdpelanggan(), et_nama.getText().toString(), et_email.getText().toString(),
                et_no_telp.getText().toString());
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        for (User user : users) {
                            Utilities.setUser(ChangeAccountActivity.this, user);
                        }
                        if (action == 1) {
                            Snackbar.make(findViewById(android.R.id.content), "Akun berhasil diubah. Silahkan verifikasi email Anda",
                                    Snackbar.LENGTH_LONG).show();
                        }else {
                            Snackbar.make(findViewById(android.R.id.content), "Akun berhasil diubah",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }else if(success == 2){
                        Snackbar.make(findViewById(android.R.id.content), "Email atau nomor telepon sudah digunakan akun lain",
                                Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(findViewById(android.R.id.content), "Gagal mengubah akun. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Gagal mengubah akun. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
                pDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
