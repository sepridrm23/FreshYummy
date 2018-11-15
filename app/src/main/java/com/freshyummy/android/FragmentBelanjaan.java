package com.freshyummy.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afi on 17/03/18.
 */

public class FragmentBelanjaan extends Fragment {
    RelativeLayout rl, rl2;
    RecyclerView rvCart;
    User users;
    TextView tv_cobalagi;

    public static FragmentBelanjaan newInstance() {
        return new FragmentBelanjaan();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_belanjaan, container, false);
        rvCart = v.findViewById(R.id.recycler_belanjaan);
        rl = v.findViewById(R.id.rl);
        rl2 = v.findViewById(R.id.rl2);
        tv_cobalagi = v.findViewById(R.id.tv_cobalagi);

//        users = Utilities.getUser(getContext());

//        if (MainActivity.belanjaans.size() != 0 && !MainActivity.flag_belanjaan){
//            rl.setVisibility(View.GONE);
//            rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
//            rvCart.setHasFixedSize(true);
//            BelanjaanViewAdapter rcAdapter = new BelanjaanViewAdapter(getActivity(), MainActivity.belanjaans);
//            rvCart.setAdapter(rcAdapter);
//        }else {
//            if (Utilities.isLogin(getContext())) {
//                MainActivity.flag_belanjaan = false;
//                gettransaction(users.getIdpelanggan());
//            }else {
//                rl.setVisibility(View.VISIBLE);
//            }
//        }

        tv_cobalagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.flag_belanjaan = false;
                gettransaction(users.getIdpelanggan());
                MainActivity.addBadgeBelanja(getContext());
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        users = Utilities.getUser(getContext());
//        if (MainActivity.belanjaans.size() != 0 && !MainActivity.flag_belanjaan){
//            rl.setVisibility(View.GONE);
//            rl2.setVisibility(View.GONE);
//            rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
//            rvCart.setHasFixedSize(true);
//            BelanjaanViewAdapter rcAdapter = new BelanjaanViewAdapter(getActivity(), MainActivity.belanjaans);
//            rvCart.setAdapter(rcAdapter);
//        }else {
            if (Utilities.isLogin(getContext())) {
                MainActivity.flag_belanjaan = false;
                gettransaction(users.getIdpelanggan());
                MainActivity.addBadgeBelanja(getContext());
            }else {
                rl.setVisibility(View.VISIBLE);
            }
//        }
    }

    //    private List<Belanjaan> getAllItemList(){
//
//        List<Belanjaan> allItems = new ArrayList<Belanjaan>();
//        allItems.add(new Belanjaan("1", "1", "1", "Batu ginjal kronis", "2", "100000", "kg", "https://drop.ndtv.com/albums/COOKS/pasta-vegetarian/pastaveg_640x480.jpg", "1000", "0", "100000","4"));
//        allItems.add(new Belanjaan("2", "2", "2", "Paru paru basah kronis", "1", "10000", "kg", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQWvmHj-P4bpQU_hRkRHtgU0MzeOqurzuNgk5ERkX4W2UEvwJycw", "1000", "0", "1000000","4"));
//        allItems.add(new Belanjaan("3", "3", "3", "Batu ginjal kronis", "2", "190000", "kg", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQeFlxJuSxFAZ9KdqlRz34ZlqmrvU-O-XvBCr1BsxLlmKSuDCogA", "1000", "0", "100000","4"));
//        allItems.add(new Belanjaan("4", "4", "4", "Batu ginjal kronis", "2", "10000", "kg", "https://i.ytimg.com/vi/sWisuBtbypE/hqdefault.jpg", "10000", "0", "10000","4"));
//        return allItems;
//    }

    private void gettransaction(String id) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
        Call<GetValue<Belanjaan>> call = api.getusertransaction(random, id);
        call.enqueue(new Callback<GetValue<Belanjaan>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Belanjaan>> call, @NonNull Response<GetValue<Belanjaan>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Belanjaan> listData = response.body().getData();
                        MainActivity.belanjaans = listData;
                        if (listData.size() == 0){
                            rl.setVisibility(View.VISIBLE);
                        }else {
                            rl2.setVisibility(View.GONE);
                            rl.setVisibility(View.GONE);
                            rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
                            rvCart.setHasFixedSize(true);
                            BelanjaanViewAdapter rcAdapter = new BelanjaanViewAdapter(getActivity(), listData);
                            rvCart.setAdapter(rcAdapter);
                        }
                    } else {
                        Log.e("boo", response.body().getMessage());
                        rl2.setVisibility(View.VISIBLE);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    Log.e("boo", response.body().getMessage());
                    rl2.setVisibility(View.VISIBLE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Belanjaan>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                Log.e("Retrofit Error:", t.getMessage());
                rl2.setVisibility(View.VISIBLE);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

}
