package com.freshyummy.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afi on 26/04/18.
 */

public class PoinDetailActivity extends AppCompatActivity {
    User user;
    public static List<PoinDetail> pointDetails = new ArrayList<>();
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poin_detail);
        back = findViewById(R.id.back);
        TextView tv_point_detail = findViewById(R.id.tv_point_detail_current);

        user = Utilities.getUser(this);
        tv_point_detail.setText(user.getPoin()+" poin");

        getdetailpoin();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentAllPoin(), "Semua Histori");
        adapter.addFragment(new FragmentInPoin(), "Penghasilan");
        adapter.addFragment(new FragmentOutPoin(), "Pembelanjaan");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void getdetailpoin() {
        final ProgressDialog pDialog = new ProgressDialog(PoinDetailActivity.this);
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
        Call<GetValue<PoinDetail>> call = api.getpoindetail(random, user.getIdpelanggan());
        call.enqueue(new Callback<GetValue<PoinDetail>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<PoinDetail>> call, @NonNull Response<GetValue<PoinDetail>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        pointDetails = response.body().getData();
                        ViewPager viewPager = findViewById(R.id.container);
                        setupViewPager(viewPager);

                        TabLayout tabLayout = findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(viewPager);
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lag",
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
            public void onFailure(@NonNull Call<GetValue<PoinDetail>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }
}
