package com.freshyummy.android;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afi on 15/03/18.
 */

public class FragmentAccount extends Fragment {
    User users;
    LinearLayout ll_account_data, ll_poin, ll_fb, ll_tentang, ll_kritik, ll_nilai, ll_privacy;
    TextView tv_verify_email, tv_account_name, tv_account_phone, tv_account_poin, tv_account_change, tv_account_email, in_out;
    CallbackManager callbackManager;
    Button signinFB;

    public static FragmentAccount newInstance() {
        return new FragmentAccount();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        setHasOptionsMenu(true);
        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        in_out = v.findViewById(R.id.tv_in_out);
        ll_account_data = v.findViewById(R.id.ll_account_data);
        ll_poin = v.findViewById(R.id.ll_poin);
        ll_fb = v.findViewById(R.id.ll_fb);
        ll_tentang = v.findViewById(R.id.ll_tentang);
        ll_nilai = v.findViewById(R.id.ll_nilai);
        ll_kritik = v.findViewById(R.id.ll_kritik);
        ll_privacy = v.findViewById(R.id.ll_privacy);
        tv_verify_email = v.findViewById(R.id.tv_verify);
        tv_account_name = v.findViewById(R.id.tv_account_name);
        tv_account_email = v.findViewById(R.id.tv_account_email);
        tv_account_phone = v.findViewById(R.id.tv_account_phone);
        tv_account_poin = v.findViewById(R.id.tv_account_poin);
        tv_account_change = v.findViewById(R.id.tv_account_change);
        signinFB = v.findViewById(R.id.button2);
        final LoginButton login_button = v.findViewById(R.id.login_button);
        login_button.setFragment(this);

        in_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (in_out.getText().equals("MASUK")){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        startActivity(new Intent(getActivity(), SignInActivity.class),  ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                    }else {
                        startActivity(new Intent(getActivity(), SignInActivity.class));
//                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true)
                        .setTitle("Konfirmasi")
                        .setMessage("Keluar dari akun ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Profile profile = Profile.getCurrentProfile();
                                if (profile != null) {
                                    LoginManager.getInstance().logOut();
                                }
                                MainActivity.belanjaans.clear();
                                MainActivity.hideBadgeBelanja();
                                Utilities.signOutUser(getContext());
                                Intent mIntent = new Intent(getContext(), SignInActivity.class);
                                startActivity(mIntent);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                }
            }
        });

        ll_poin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isLogin(getContext())) {
//                    getActivity().startActivity(new Intent(getActivity(), PoinDetailActivity.class));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        startActivity(new Intent(getActivity(), PoinDetailActivity.class),  ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                    }else {
                        startActivity(new Intent(getActivity(), PoinDetailActivity.class));
//                    }
                }else {
//                    getActivity().startActivity(new Intent(getActivity(), SignInActivity.class));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        startActivity(new Intent(getActivity(), SignInActivity.class),  ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                    }else {
                        startActivity(new Intent(getActivity(), SignInActivity.class));
//                    }
                }
            }
        });

        tv_account_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), ChangeAccountActivity.class));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(new Intent(getActivity(), ChangeAccountActivity.class),  ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                }else {
                    startActivity(new Intent(getActivity(), ChangeAccountActivity.class));
//                }
            }
        });

        ll_tentang.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                assert (getActivity()) != null;
                dialog.setCancelable(true);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_app_info, null);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialogView.findViewById(R.id.iv_app_info_facebook).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = getString(R.string.uri_facebook);
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(i);
                    }
                });

                dialogView.findViewById(R.id.iv_app_info_instagram).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = getString(R.string.uri_instagram);
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(i);
                    }
                });

                dialogView.findViewById(R.id.iv_app_info_twitter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = getString(R.string.uri_twitter);
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(i);
                    }
                });

                dialog.setView(dialogView);
                dialog.create();
                dialog.show();
            }
        });

        ll_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getContext(), PrivacyPolicyActivity.class);
                startActivity(mIntent);
            }
        });

        ll_nilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.uri_play_store);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        ll_kritik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.uri_feedback);
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(i);
            }
        });

        tv_verify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getContext(), VerifyEmailActivity.class);
                startActivity(mIntent);
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
                                String id = "";
                                try {
                                    id = object.getString("id");
//                                    checkfb(object.getString("id"), object.getString("name"), object.getString("email"));
//                                    Toast.makeText(getApplicationContext(), "Hi, " + object.getString("name"), Toast.LENGTH_LONG).show();
                                } catch(JSONException ex) {
                                    ex.printStackTrace();
                                }
                                updateidfacebook(id);
//                                Log.e("aa", id+nama+email);
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

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        users = Utilities.getUser(getContext());

        if (Utilities.isLogin(getContext())){
            getpoin();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                signinFB.setEnabled(false);
                signinFB.setText("Terhubung sebagai "+profile.getName());
            }else {
                signinFB.setEnabled(true);
                signinFB.setText("Hubungkan dengan Facebook");
            }

            ll_account_data.setVisibility(View.VISIBLE);
            ll_poin.setVisibility(View.VISIBLE);
            ll_fb.setVisibility(View.VISIBLE);
            tv_account_change.setVisibility(View.VISIBLE);
            tv_account_email.setText(users.getEmail());
            tv_account_name.setText(users.getNama());
            tv_account_poin.setText(users.getPoin()+" Poin");
            tv_account_phone.setText(users.getTelepon());
            if (users.getStatusverifikasiemail().equals("0")) {
                tv_verify_email.setVisibility(View.VISIBLE);
            }else {
                tv_verify_email.setVisibility(View.GONE);
            }
            in_out.setText("KELUAR");
        }else {
            tv_account_email.setText("cs.freshyummy@gmail.com");
            tv_account_name.setText("Fresh Yummy");
            tv_account_phone.setText("081278909143");
            ll_account_data.setVisibility(View.VISIBLE);
            ll_poin.setVisibility(View.VISIBLE);
            ll_fb.setVisibility(View.GONE);
            tv_verify_email.setVisibility(View.GONE);
            tv_account_poin.setText("");
            tv_account_change.setVisibility(View.GONE);
            in_out.setText("MASUK");
        }

    }

    public void getpoin(){
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
        Call<SetValue> call = apiService.getpoin(random, users.getIdpelanggan());
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
//                        Log.e("status", response.body().getMessage());
                        if (!users.getPoin().equals(response.body().getMessage())){
                            tv_account_poin.setText(response.body().getMessage()+" Poin");
                            List<User> data_user = new ArrayList<>();
                            data_user.add(new User(users.getIdpelanggan(), users.getNama(), users.getTelepon(), users.getEmail(), users.getTanggaldaftar(),
                                    users.getStatusverifikasiemail(), response.body().getMessage(), users.getKodereferensi(), users.getReferensipelanggan()));
                            for (User user : data_user) {
                                Utilities.setUser(getContext(), user);
                            }
                        }
                    }else {
                        Log.e("status", response.body().getMessage());
                    }
                } else {
                    Log.e("status", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
                Log.e("failur", t.getMessage());
            }
        });
    }

    public void updateidfacebook(String idfacebook){
        final ProgressDialog pDialog = new ProgressDialog(getContext());
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
        Call<SetValue> call = apiService.connectfacebook(random, idfacebook, users.getIdpelanggan());
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        Profile profile = Profile.getCurrentProfile();
                        if (profile != null) {
                            signinFB.setEnabled(false);
                            signinFB.setText("Terhubung sebagai "+profile.getName());
                        }
                    }else {
//                        Log.e("status", response.body().getMessage());
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal menghubungkan ke Facebook. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
//                    Log.e("status", response.body().getMessage());
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal menghubungkan ke Facebook. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
                pDialog.dismiss();
//                Log.e("failur", t.getMessage());
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
