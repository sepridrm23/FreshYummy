package com.freshyummy.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class VerifyPhoneActivity extends AppCompatActivity {

//    private boolean mVerificationInProgress = false;
//    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
//    private String mVerificationId;
//    private PhoneAuthProvider.ForceResendingToken mResendToken;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//    private FirebaseAuth mAuth;
//    TextView tvTime;
//    Button btnKirim;
//    CountDownTimer countDownTimer;
//    boolean flag;
//    EditText etCode;

    String phone, action;

    public static int APP_REQUEST_CODE = 99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneLogin();
        setContentView(R.layout.activity_phone_auth);
//        TextView tvPhone = (TextView)findViewById(R.id.tv_telepon);
//        tvTime = (TextView)findViewById(R.id.tv_time);
//        etCode = (EditText)findViewById(R.id.et_kode);
//        btnKirim = (Button)findViewById(R.id.btn_kirim);
//
//        if (savedInstanceState != null) {
//            onRestoreInstanceState(savedInstanceState);
//        }
//
//        mAuth = FirebaseAuth.getInstance();
        phone = getIntent().getStringExtra("EXTRA_PHONE");
        action = getIntent().getStringExtra("ACTION");
//        tvPhone.setText(phone);
//
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                mVerificationInProgress = false;
//                btnKirim.setText("Kirim Kode");
//                btnKirim.setEnabled(true);
//                btnKirim.setBackgroundColor(getResources().getColor(R.color.colorButton));
//                btnKirim.setTextColor(Color.WHITE);
//                countDownTimer.cancel();
//                tvTime.setVisibility(View.INVISIBLE);
////                tvTime.setText("Success");
//                etCode.setText(credential.getSmsCode().toString());
//                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                mVerificationInProgress = false;
////                tvTime.setText("Failed");
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    tvTime.setVisibility(View.INVISIBLE);
//                    countDownTimer.cancel();
//                    Snackbar.make(findViewById(android.R.id.content), "Nomor HP tidak valid",
//                            Snackbar.LENGTH_LONG).show();
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    tvTime.setVisibility(View.INVISIBLE);
//                    countDownTimer.cancel();
//                    Snackbar.make(findViewById(android.R.id.content), "Sistem sedang sibuk. Silahkan coba lagi beberapa saat lagi.",
//                            Snackbar.LENGTH_LONG).show();
//                }
//
//            }
//
//            @Override
//            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
////                btnKirim.setText("Kirim Ulang");
////                Snackbar.make(findViewById(android.R.id.content), verificationId,
////                        Snackbar.LENGTH_LONG).show();
//                mVerificationId = verificationId;
//                mResendToken = token;
//            }
//        };
//
//        btnKirim.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(btnKirim.getText().toString().equals("Kirim Ulang")){
//                    btnKirim.setEnabled(false);
//                    btnKirim.setText("Kirim Kode");
//                    btnKirim.setBackgroundColor(Color.DKGRAY);
//                    tvTime.setVisibility(View.VISIBLE);
//                    btnKirim.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    resendVerificationCode(phone, mResendToken);
//                    countDownTimer = new CountDownTimer(60000, 1000) {
//
//                        public void onTick(long millisUntilFinished) {
//                            tvTime.setText("Kirim ulang dalam : " + millisUntilFinished / 1000);
//                        }
//
//                        public void onFinish() {
//                            tvTime.setVisibility(View.INVISIBLE);
//                            btnKirim.setEnabled(true);
//                            btnKirim.setBackgroundColor(getResources().getColor(R.color.colorButton));
//                            btnKirim.setTextColor(Color.WHITE);
//                            btnKirim.setText("Kirim Ulang");
//                        }
//
//                    }.start();
//                }else {
//                    tvTime.setVisibility(View.INVISIBLE);
//                    countDownTimer.cancel();
//                    String code = etCode.getText().toString();
//                    if (TextUtils.isEmpty(code)) {
//                        etCode.setError("Silahkan masukan kode dari SMS");
//                        return;
//                    }else {
//                        verifyPhoneNumberWithCode(mVerificationId, code);
//                    }
//                }
//            }
//        });
//
//        etCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (i >= 3) {
//                    btnKirim.setText("Kirim Kode");
//                    btnKirim.setEnabled(true);
//                    btnKirim.setBackgroundColor(getResources().getColor(R.color.colorButton));
//                    btnKirim.setTextColor(Color.WHITE);
//                }else{
//                    btnKirim.setText("Kirim Kode");
//                    btnKirim.setEnabled(false);
//                    btnKirim.setBackgroundColor(Color.DKGRAY);
//                    btnKirim.setTextColor(getResources().getColor(R.color.colorPrimary));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    public void phoneLogin() {
        final Intent intent = new Intent(VerifyPhoneActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage="";
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(
                        this,
                        toastMessage,
                        Toast.LENGTH_SHORT)
                        .show();
//                showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
//                toastMessage = "Login Cancelled";
                finish();
            } else {
//                if (loginResult.getAccessToken() != null) {
//                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
//                } else {
//                    toastMessage = String.format(
//                            "Success:%s...",
//                            loginResult.getAuthorizationCode().substring(0,10));
//                }

                if (action.equals("SIGNIN")) {
                    Utilities.setLogin(VerifyPhoneActivity.this);
                    for (User user : SignInActivity.users) {
//                        Log.d("data user", "[getUser] " + user.getIdpelanggan());
                        Utilities.setUser(VerifyPhoneActivity.this, user);
                    }
                    Snackbar.make(findViewById(android.R.id.content), "Login succes",
                            Snackbar.LENGTH_LONG).show();
                    finish();
//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            finish();
//                        }
//                    }, 2000);
                } else if (action.equals("FB")){
                    SetUserFB();

//                        Utilities.setLogin(VerifyPhoneActivity.this);
//                        for (User user : SignInActivity.users) {
////                        Log.d("data user", "[getUser] " + user.getIdpelanggan());
//                            Utilities.setUser(VerifyPhoneActivity.this, user);
//                        }
//                        Snackbar.make(findViewById(android.R.id.content), "Login succes",
//                                Snackbar.LENGTH_LONG).show();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        }, 2000);
                } else if (action.equals("SIGNUP")){
                    SetUser();
                } else {
                    updateakun();
                }
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        btnKirim.setEnabled(false);
//        btnKirim.setBackgroundColor(Color.DKGRAY);
//        btnKirim.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//        phoneLogin();
//
////        if (!flag) {
////            flag = true;
////            startPhoneNumberVerification(phone);
////        }
////        countDownTimer = new CountDownTimer(60000, 1000) {
////
////            public void onTick(long millisUntilFinished) {
////                tvTime.setText("Kirim ulang dalam : " + millisUntilFinished / 1000);
////            }
////
////            public void onFinish() {
////                tvTime.setVisibility(View.INVISIBLE);
////                btnKirim.setEnabled(true);
////                btnKirim.setBackgroundColor(getResources().getColor(R.color.colorButton));
////                btnKirim.setTextColor(Color.WHITE);
////                btnKirim.setText("Kirim Ulang");
////            }
////
////        }.start();
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
////        countDownTimer.cancel();
//    }

    @Override
    public void onBackPressed() {
//        countDownTimer.cancel();
        if (action.equals("SIGNIN")) {
            startActivity(new Intent(VerifyPhoneActivity.this, SignInActivity.class));
            finish();
        }else if (action.equals("SIGNUP")) {
            startActivity(new Intent(VerifyPhoneActivity.this, SignUpActivity.class));
            finish();
        }
        else if (action.equals("FB")) {
            Intent intent = new Intent(VerifyPhoneActivity.this, SignUpFBActivity.class);
            intent.putExtra("EXTRA_IDFB", SignUpFBActivity.idfacebook);
            intent.putExtra("EXTRA_EMAIL", SignUpFBActivity.email);
            intent.putExtra("EXTRA_NAME", SignUpFBActivity.nama);
            intent.putExtra("EXTRA_PHONE", SignUpFBActivity.telepon);
            intent.putExtra("ACTION", SignUpFBActivity.action);
            startActivity(intent);
            finish();
        }
        else {
//            onBackPressed();
            finish();
        }
    }


    //    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (mVerificationInProgress) {
//            startPhoneNumberVerification(phone);
//        }
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
//    }

//    private void startPhoneNumberVerification(String phoneNumber) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacks
//
//        mVerificationInProgress = true;
//    }

//    private void verifyPhoneNumberWithCode(String verificationId, String code) {
//        try {
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//            signInWithPhoneAuthCredential(credential);
//        }catch (Exception e){
//            Snackbar.make(findViewById(android.R.id.content), "Kode tidak valid",
//                    Snackbar.LENGTH_LONG).show();
//        }
//    }
//
//    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks,         // OnVerificationStateChangedCallbacks
//                token);             // ForceResendingToken from callbacks
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
////                    FirebaseUser user = task.getResult().getUser();
////                    Snackbar.make(findViewById(android.R.id.content), user.getPhoneNumber(),
////                            Snackbar.LENGTH_LONG).show();
//                    if (action.equals("SIGNIN")) {
//                        Utilities.setLogin(VerifyPhoneActivity.this);
//                        for (User user : SignInActivity.users) {
////                        Log.d("data user", "[getUser] " + user.getIdpelanggan());
//                            Utilities.setUser(VerifyPhoneActivity.this, user);
//                        }
//                        Snackbar.make(findViewById(android.R.id.content), "Login succes",
//                                Snackbar.LENGTH_LONG).show();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        }, 2000);
//                    } else if (action.equals("FB")){
//                        SetUserFB();
//
////                        Utilities.setLogin(VerifyPhoneActivity.this);
////                        for (User user : SignInActivity.users) {
//////                        Log.d("data user", "[getUser] " + user.getIdpelanggan());
////                            Utilities.setUser(VerifyPhoneActivity.this, user);
////                        }
////                        Snackbar.make(findViewById(android.R.id.content), "Login succes",
////                                Snackbar.LENGTH_LONG).show();
////                        new Timer().schedule(new TimerTask() {
////                            @Override
////                            public void run() {
////                                finish();
////                            }
////                        }, 2000);
//                    } else if (action.equals("SIGNUP")){
//                        SetUser();
//                    } else {
//                        updateakun();
//                    }
//                } else {
//                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                        Snackbar.make(findViewById(android.R.id.content), "Nomor HP tidak valid",
//                                Snackbar.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//    }

    public void updateakun(){
        final ProgressDialog pDialog = new ProgressDialog(VerifyPhoneActivity.this);
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
        Call<SetValue> call = apiService.updateakun(random, ChangeAccountActivity.users.get(0).getIdpelanggan(),
                ChangeAccountActivity.users.get(0).getNama(), ChangeAccountActivity.users.get(0).getEmail(),
                ChangeAccountActivity.users.get(0).getTelepon());
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        User user_ = Utilities.getUser(VerifyPhoneActivity.this);
                        if (user_.getEmail().equals(ChangeAccountActivity.users.get(0).getEmail())){
                            for (User user : ChangeAccountActivity.users) {
                                Utilities.setUser(VerifyPhoneActivity.this, user);
                            }
                            Snackbar.make(findViewById(android.R.id.content), "Akun berhasil diubah",
                                    Snackbar.LENGTH_LONG).show();
                            finish();
//                            new Timer().schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    finish();
//                                }
//                            }, 2000);
                        }else {
                            for (User user : ChangeAccountActivity.users) {
                                Utilities.setUser(VerifyPhoneActivity.this, user);
                            }
                            Snackbar.make(findViewById(android.R.id.content), "Akun berhasil diubah. Silahkan verifikasi email Anda",
                                    Snackbar.LENGTH_LONG).show();
                            finish();
//                            new Timer().schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    finish();
//                                }
//                            }, 2000);
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

    public void SetUser() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final APIServices apiService = retrofit.create(APIServices.class);
        Call<SetValue> call = apiService.signup(random, SignInActivity.users.get(0).getEmail(), "+62"+SignInActivity.users.get(0).getTelepon(), SignInActivity.users.get(0).getNama(), Utilities.getToken());
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        List<User> users = new ArrayList<>();
                        users.add(new User(response.body().getMessage(), SignInActivity.users.get(0).getNama(), "+62"+SignInActivity.users.get(0).getTelepon(), SignInActivity.users.get(0).getEmail(), dateFormat.format(date), "0", "0", "", ""));
                        SignInActivity.users = users;

                        Utilities.setLogin(VerifyPhoneActivity.this);
                        for (User user : users) {
//                        Log.d("data user", "[getUser] " + user.getIdpelanggan());
                            Utilities.setUser(VerifyPhoneActivity.this, user);
                        }

                        Snackbar.make(findViewById(android.R.id.content), "Akun berhasil dibuat",
                                Snackbar.LENGTH_LONG).show();
                        finish();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
////                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                                finish();
//                            }
//                        }, 2500);
                    } else if (success == 2) {
                        Snackbar.make(findViewById(android.R.id.content), "Alamat email atau nomor HP sudah terdaftar",
                                Snackbar.LENGTH_LONG).show();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                                finish();
//                            }
//                        }, 4000);
                    }else {
                        Snackbar.make(findViewById(android.R.id.content), "Gagal membuat akun. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke server",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
                dialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke server",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void SetUserFB() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final APIServices apiService = retrofit.create(APIServices.class);
        Call<SetValue> call = apiService.signupfb(random, SignUpFBActivity.email, "+62"+SignUpFBActivity.telepon, SignUpFBActivity.nama, SignUpFBActivity.idfacebook, Utilities.getToken());
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
//                        Log.e("idpelanggan", response.body().getMessage());
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        SignInActivity.users = new ArrayList<>();
                        SignInActivity.users.add(new User(response.body().getMessage(), SignUpFBActivity.nama, "+62"+SignUpFBActivity.telepon, SignUpFBActivity.email, dateFormat.format(date), "0", "0", "", ""));

                        Utilities.setLogin(VerifyPhoneActivity.this);
                        for (User user : SignInActivity.users) {
//                        Log.d("data user", "[getUser] " + user.getIdpelanggan());
                            Utilities.setUser(VerifyPhoneActivity.this, user);
                        }
                        Snackbar.make(findViewById(android.R.id.content), "Login succes",
                                Snackbar.LENGTH_LONG).show();
                        finish();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        }, 2000);

                    } else if (success == 2) {
                        Snackbar.make(findViewById(android.R.id.content), "Alamat email atau nomor HP sudah terdaftar",
                                Snackbar.LENGTH_LONG).show();
                        finish();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                                finish();
//                            }
//                        }, 4000);
                    }else {
                        Snackbar.make(findViewById(android.R.id.content), "Gagal membuat akun. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke server",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
                dialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke server",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
