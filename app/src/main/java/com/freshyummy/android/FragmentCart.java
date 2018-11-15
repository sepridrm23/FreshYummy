package com.freshyummy.android;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
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
 * Created by afi on 15/03/18.
 */

public class FragmentCart extends Fragment {
    RecyclerView rvCart;
    ProgressDialog pDialog;
    List<Cart> allItemsCart = MainActivity.carts;
    List<Cart> tempCart = new ArrayList<>();
    TextView tv_cart_sub_total, tv_cobalagi;
    RelativeLayout rl, rl2;
    int cart_sub = 0, nowcounter=0;

    public static FragmentCart newInstance() {
        return new FragmentCart();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        rvCart = v.findViewById(R.id.recycler_cart);
        Button btnCheckout = v.findViewById(R.id.btn_cart_check_out);
        rl = v.findViewById(R.id.rl);
        rl2 = v.findViewById(R.id.rl2);
        tv_cart_sub_total = v.findViewById(R.id.tv_cart_sub_total);
        tv_cobalagi = v.findViewById(R.id.tv_cobalagi);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        MainActivity.addBadgeCart(getContext());

        getCartSQLite();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart_sub == 0){
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Silahkan tambahkan barang ke keranjang",
                            Snackbar.LENGTH_LONG).show();
                }else {
                    if (!Utilities.isLogin(getContext())){
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            startActivity(new Intent(getActivity(), SignInActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                        }else {
                            startActivity(new Intent(getActivity(), SignInActivity.class));
//                        }
                    }else {
                        MainActivity.replaceFragment(FragmentCheckout.newInstance(), 6);
                    }
                }
            }
        });

        tv_cobalagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCartSQLite();
            }
        });

        return v;
    }

    private void getCartSQLite(){
        DataHelper dbHelper = new DataHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cart",null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0){
            rl.setVisibility(View.VISIBLE);
            tv_cart_sub_total.setText(Utilities.getCurrency(String.valueOf(cart_sub)));
            MainActivity.carts.clear();
        }else {
            if (allItemsCart.size() == 0) {
                allItemsCart = new ArrayList<>();
                tempCart = new ArrayList<>();
                for (int cc = 0; cc < cursor.getCount(); cc++) {
                    cursor.moveToPosition(cc);
                    getProduct(cursor.getCount(), cursor.getString(0), cursor.getString(1), cursor.getString(2));
                }
            }else {
                if (allItemsCart.size() != cursor.getCount()){
                    allItemsCart = new ArrayList<>();
                    tempCart = new ArrayList<>();
                    if (cursor.getCount() > MainActivity.carts.size()) {
                        for (int cc = 0; cc < cursor.getCount(); cc++) {
                            cursor.moveToPosition(cc);
                            getProduct(cursor.getCount(), cursor.getString(0), cursor.getString(1), cursor.getString(2));
                        }
                    }else {
                        for (int cc = 0; cc < MainActivity.carts.size(); cc++) {
                            for (int a = 0; a < cursor.getCount(); a++){
                                cursor.moveToPosition(a);
                                if (cursor.getString(1).equals(MainActivity.carts.get(cc).getIddetailproduk())) {
                                    if (MainActivity.carts.get(cc).getStatusketersediaan().equals("1")) {
                                        allItemsCart.add(new Cart(MainActivity.carts.get(cc).getIdkeranjang(), MainActivity.carts.get(cc).getIddetailproduk(),
                                                MainActivity.carts.get(cc).getNamaproduk(), MainActivity.carts.get(cc).getGambar(),
                                                MainActivity.carts.get(cc).getSatuan(), MainActivity.carts.get(cc).getHarga(),
                                                cursor.getString(2), MainActivity.carts.get(cc).getDiscount(),
                                                MainActivity.carts.get(cc).getStatusketersediaan()));
                                    } else {
                                        tempCart.add(new Cart(MainActivity.carts.get(cc).getIdkeranjang(), MainActivity.carts.get(cc).getIddetailproduk(),
                                                MainActivity.carts.get(cc).getNamaproduk(), MainActivity.carts.get(cc).getGambar(),
                                                MainActivity.carts.get(cc).getSatuan(), MainActivity.carts.get(cc).getHarga(),
                                                cursor.getString(2), MainActivity.carts.get(cc).getDiscount(),
                                                MainActivity.carts.get(cc).getStatusketersediaan()));
                                    }
                                }
                            }
                        }

                        for (int a = 0; a < tempCart.size(); a++){
                            allItemsCart.add(new Cart(tempCart.get(a).getIdkeranjang(), tempCart.get(a).getIddetailproduk(), tempCart.get(a).getNamaproduk(), tempCart.get(a).getGambar(), tempCart.get(a).getSatuan(), tempCart.get(a).getHarga(), tempCart.get(a).getJumlahbeli(), tempCart.get(a).getDiscount(), tempCart.get(a).getStatusketersediaan()));
                        }

                        MainActivity.carts = allItemsCart;

                        cart_sub = 0;
                        for (int x = 0; x < allItemsCart.size() ; x++) {
                            if (allItemsCart.get(x).getStatusketersediaan().equals("1")) {
                                cart_sub = cart_sub + (Integer.parseInt(allItemsCart.get(x).getHarga()) * Integer.parseInt(allItemsCart.get(x).getJumlahbeli()));
                            }
                        }

                        rl.setVisibility(View.GONE);
                        rl2.setVisibility(View.GONE);
                        tv_cart_sub_total.setText(Utilities.getCurrency(String.valueOf(cart_sub)));

                        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvCart.setHasFixedSize(true);
                        CartViewAdapter rcAdapter = new CartViewAdapter(getActivity(), allItemsCart);
                        rvCart.setAdapter(rcAdapter);
                    }
                }else {
                    allItemsCart = new ArrayList<>();
                    tempCart = new ArrayList<>();
                    for (int cc = 0; cc < MainActivity.carts.size(); cc++) {
                        for (int a = 0; a < cursor.getCount(); a++){
                            cursor.moveToPosition(a);
                            if (cursor.getString(1).equals(MainActivity.carts.get(cc).getIddetailproduk())) {
                                if (MainActivity.carts.get(cc).getStatusketersediaan().equals("1")) {
                                    allItemsCart.add(new Cart(MainActivity.carts.get(cc).getIdkeranjang(), MainActivity.carts.get(cc).getIddetailproduk(),
                                            MainActivity.carts.get(cc).getNamaproduk(), MainActivity.carts.get(cc).getGambar(),
                                            MainActivity.carts.get(cc).getSatuan(), MainActivity.carts.get(cc).getHarga(),
                                            cursor.getString(2), MainActivity.carts.get(cc).getDiscount(),
                                            MainActivity.carts.get(cc).getStatusketersediaan()));
                                }else {
                                    tempCart.add(new Cart(MainActivity.carts.get(cc).getIdkeranjang(), MainActivity.carts.get(cc).getIddetailproduk(),
                                            MainActivity.carts.get(cc).getNamaproduk(), MainActivity.carts.get(cc).getGambar(),
                                            MainActivity.carts.get(cc).getSatuan(), MainActivity.carts.get(cc).getHarga(),
                                            cursor.getString(2), MainActivity.carts.get(cc).getDiscount(),
                                            MainActivity.carts.get(cc).getStatusketersediaan()));
                                }
                            }
                        }
                    }

                    for (int a = 0; a < tempCart.size(); a++){
                        allItemsCart.add(new Cart(tempCart.get(a).getIdkeranjang(), tempCart.get(a).getIddetailproduk(), tempCart.get(a).getNamaproduk(), tempCart.get(a).getGambar(), tempCart.get(a).getSatuan(), tempCart.get(a).getHarga(), tempCart.get(a).getJumlahbeli(), tempCart.get(a).getDiscount(), tempCart.get(a).getStatusketersediaan()));
                    }

                    MainActivity.carts = allItemsCart;

                    cart_sub = 0;
                    for (int x = 0; x < allItemsCart.size() ; x++) {
                        if (allItemsCart.get(x).getStatusketersediaan().equals("1")) {
                            cart_sub = cart_sub + (Integer.parseInt(allItemsCart.get(x).getHarga()) * Integer.parseInt(allItemsCart.get(x).getJumlahbeli()));
                        }
                    }

                    rl.setVisibility(View.GONE);
                    rl2.setVisibility(View.GONE);
                    tv_cart_sub_total.setText(Utilities.getCurrency(String.valueOf(cart_sub)));

                    rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvCart.setHasFixedSize(true);
                    CartViewAdapter rcAdapter = new CartViewAdapter(getActivity(), allItemsCart);
                    rvCart.setAdapter(rcAdapter);
                }
            }
        }
    }

    private void getProduct(final int endcounter, final String idkeranjang, final String iddetailproduk, final String jumlahbeli) {
        if (nowcounter == 0) {
            nowcounter++;
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        APIServices api = retrofit.create(APIServices.class);
        Call<GetValue<Cart>> call = api.getusercart(random, iddetailproduk);
        call.enqueue(new Callback<GetValue<Cart>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Cart>> call, @NonNull Response<GetValue<Cart>> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Cart> listDataProduct = response.body().getData();
                        if (listDataProduct.get(0).getStatusketersediaan().equals("1")) {
                            allItemsCart.add(new Cart(idkeranjang, iddetailproduk, listDataProduct.get(0).getNamaproduk(), listDataProduct.get(0).getGambar(), listDataProduct.get(0).getSatuan(), listDataProduct.get(0).getHarga(), jumlahbeli, listDataProduct.get(0).getDiscount(), listDataProduct.get(0).getStatusketersediaan()));
                        }else {
                            tempCart.add(new Cart(idkeranjang, iddetailproduk, listDataProduct.get(0).getNamaproduk(), listDataProduct.get(0).getGambar(), listDataProduct.get(0).getSatuan(), listDataProduct.get(0).getHarga(), jumlahbeli, listDataProduct.get(0).getDiscount(), listDataProduct.get(0).getStatusketersediaan()));
                        }

                        if (nowcounter == endcounter) {
                            for (int a = 0; a < tempCart.size(); a++){
                                allItemsCart.add(new Cart(tempCart.get(a).getIdkeranjang(), tempCart.get(a).getIddetailproduk(), tempCart.get(a).getNamaproduk(), tempCart.get(a).getGambar(), tempCart.get(a).getSatuan(), tempCart.get(a).getHarga(), tempCart.get(a).getJumlahbeli(), tempCart.get(a).getDiscount(), tempCart.get(a).getStatusketersediaan()));
                            }

                            cart_sub = 0;
                            for(int x = 0; x < allItemsCart.size(); x++){
                                if (allItemsCart.get(x).getStatusketersediaan().equals("1")) {
                                    cart_sub = cart_sub + (Integer.parseInt(allItemsCart.get(x).getHarga()) * Integer.parseInt(allItemsCart.get(x).getJumlahbeli()));
                                }
                            }

                            rl.setVisibility(View.GONE);
                            rl2.setVisibility(View.GONE);
                            tv_cart_sub_total.setText(Utilities.getCurrency(String.valueOf(cart_sub)));

                            MainActivity.carts = allItemsCart;
                            rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
                            //rvCart.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                            rvCart.setHasFixedSize(true);
                            CartViewAdapter rcAdapter = new CartViewAdapter(getActivity(), allItemsCart);
                            rvCart.setAdapter(rcAdapter);

                            pDialog.dismiss();
                        }

                        nowcounter++;
                    } else {
                        pDialog.dismiss();
                        rl2.setVisibility(View.VISIBLE);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    rl2.setVisibility(View.VISIBLE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Cart>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                rl2.setVisibility(View.VISIBLE);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

}
