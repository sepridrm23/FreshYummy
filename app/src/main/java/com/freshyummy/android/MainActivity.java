package com.freshyummy.android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    static Badge cartBadge, belanjaBadge;
    static FragmentManager fragmentManager;
    Fragment fragment;
    static Fragment lastFragment;
    public static List<Cart> carts = new ArrayList<>();
    public static List<Cart> temp_carts = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<Product> products_temp = new ArrayList<>();
    public static List<Product> products_selection_temp = new ArrayList<>();
    public static List<Product> products_search_temp = new ArrayList<>();
    public static List<Product> products_discount = new ArrayList<>();
    public static List<Product> products_selection_discount = new ArrayList<>();
    public static List<Product> products_newest = new ArrayList<>();
    public static List<Product> products_selection_newest = new ArrayList<>();
    public static List<Product> products_other = new ArrayList<>();
    public static List<Ad> ads = new ArrayList<>();
    public static List<Bank> banks = new ArrayList<>();
    public static List<Belanjaan> belanjaans = new ArrayList<>();
    public static List<Promo> promos = new ArrayList<>();
    public static boolean flag_belanjaan = false;
    static int currentFragment = 0, nowFragment = 0;
    static User users;
    public static int badge_belanjaan=0;
    @SuppressLint("StaticFieldLeak")
    static BottomNavigationViewEx bnve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnve = findViewById(R.id.bnve);
        bnve.enableAnimation(true);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setIconSize(27, 27);
        bnve.setTextSize(11);

        users = Utilities.getUser(MainActivity.this);

        cartBadge = new QBadgeView(MainActivity.this)
                .setGravityOffset(25, 5, true)
                .bindTarget(bnve.getBottomNavigationItemView(1));

        addBadgeCart(this);

        belanjaBadge = new QBadgeView(MainActivity.this)
                .setGravityOffset(25, 5, true)
                .bindTarget(bnve.getBottomNavigationItemView(2));

        if (Utilities.isLogin(this)) {
            addBadgeBelanja(this);
        }

        fragmentManager = getSupportFragmentManager();
        fragment = FragmentHome.newInstance();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        currentFragment = 0;
                        nowFragment = 0;
                        fragment = FragmentHome.newInstance();
                        lastFragment = fragment;
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        break;
                    case R.id.nav_cart:
                        currentFragment = 1;
                        nowFragment = 1;
                        fragment = FragmentCart.newInstance();
                        lastFragment = fragment;
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        break;
                    case R.id.nav_belanjaan:
                        currentFragment = 2;
                        nowFragment = 2;
                        fragment = FragmentBelanjaan.newInstance();
                        lastFragment = fragment;
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        break;
                    case R.id.nav_akun:
                        currentFragment = 3;
                        nowFragment = 3;
                        fragment = FragmentAccount.newInstance();
                        lastFragment = fragment;
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        break;
                }

                return true;
            }
        });

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    public void onBackPressed() {
        if(nowFragment != currentFragment){
            if (nowFragment == 5) {
                fragmentManager.beginTransaction().replace(R.id.flContent, lastFragment).commit();
                nowFragment = 4;
            } else {
                bnve.setCurrentItem(currentFragment);
            }
        }else {
            super.onBackPressed();
        }
    }

//    public static void  hideBadgeCart(){
//        cartBadge.hide(true);
//    }

    public static void  hideBadgeBelanja(){
        belanjaBadge.hide(true);
        badge_belanjaan = 0;
    }

//    public static void  addBadgeBelanja(){
//        Random a = new Random();
//        int x = a.nextInt(10);
//        belanjaBadge.setBadgeNumber(x);
//    }

    public static void  addBadgeCart(Context context){
//        Random a = new Random();
//        int x = a.nextInt(10);
//        cartBadge.setBadgeNumber(x);
        DataHelper dbHelper = new DataHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cart",null);
        int jml = cursor.getCount();
        cartBadge.setBadgeNumber(jml);
    }

    public static void replaceFragment(Fragment fragment, int flag){
        if (flag == 5){
            nowFragment = flag;
        }else{
            nowFragment = flag;
            lastFragment = fragment;
        }
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public static void setCurrenBNV(int flag){
        bnve.setCurrentItem(flag);
    }

    public static void addBadgeBelanja(Context context) {
//        final ProgressDialog dialog = new ProgressDialog(context);
//        dialog.setMessage("Loading...");
//        dialog.setCancelable(false);
//        dialog.show();

        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final APIServices apiService = retrofit.create(APIServices.class);
        Call<SetValue> call = apiService.gettransactioncount(random, users.getIdpelanggan());
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
//                dialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        belanjaBadge.setBadgeNumber(Integer.parseInt(response.body().getMessage()));
                        badge_belanjaan = Integer.parseInt(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
//                dialog.dismiss();
                Log.e("onFailure", t.toString());
            }
        });
    }
}
