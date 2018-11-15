package com.freshyummy.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by afi on 15/03/18.
 */

public class FragmentHome extends Fragment{
    private SliderLayout mSlider;
    RecyclerView rView;
    RecyclerView rvViewDiscount;
    RecyclerView rvViewNewest;
    RelativeLayout search_view;
    LinearLayout cover;
    RelativeLayout rl;
    TextView tv_cobalagi;
    SwipeRefreshLayout swipeRefreshLayout;

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mSlider = v.findViewById(R.id.slider);
        rView = v.findViewById(R.id.recycler_view_category);
        rvViewDiscount = v.findViewById(R.id.recycler_view_discount);
        rvViewNewest = v.findViewById(R.id.recycler_view_newest);
        search_view = v.findViewById(R.id.search_view);
        cover = v.findViewById(R.id.cover);
        rl = v.findViewById(R.id.rl);
        tv_cobalagi  = v.findViewById(R.id.tv_cobalagi);
        swipeRefreshLayout = v.findViewById(R.id.swipe);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        final DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int viewPagerWidth = Math.round(outMetrics.widthPixels);
        int more = viewPagerWidth/6;
        int viewPagerHeight = viewPagerWidth/4+more;
        mSlider.setLayoutParams(new LinearLayout.LayoutParams(viewPagerWidth, viewPagerHeight));

        if (MainActivity.ads.size() != 0 && MainActivity.categories.size() != 0 && MainActivity.products_newest.size() != 0 && MainActivity.products_discount.size() != 0) {
            List<Ad> listDataAd = MainActivity.ads;
            HashMap<String,String> url_maps = new HashMap<String, String>();

            for (int a = 0; a < listDataAd.size(); a++) {
                url_maps.put(listDataAd.get(a).getJuduliklan(), Utilities.getURLImageIklan()+listDataAd.get(a).getGambar());
            }

            for(String name : url_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(getContext());
                textSliderView
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);

                mSlider.addSlider(textSliderView);
            }

            mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mSlider.setDuration(4000);

            List<Category> listDataCategory = MainActivity.categories;
            rView.setHasFixedSize(true);
            rView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            CategoryViewAdapter rcAdapter = new CategoryViewAdapter(getActivity(), listDataCategory);
            rView.setAdapter(rcAdapter);

            rvViewDiscount.setNestedScrollingEnabled(false);
            rvViewDiscount.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
            rvViewDiscount.setItemAnimator(new DefaultItemAnimator());
            ProductPromoViewAdapter rcAdapter_discount = new ProductPromoViewAdapter(getActivity(), MainActivity.products_selection_discount, 2);
            rvViewDiscount.setAdapter(rcAdapter_discount);

            rvViewNewest.setNestedScrollingEnabled(false);
            rvViewNewest.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
            rvViewNewest.setItemAnimator(new DefaultItemAnimator());
            ProductPromoViewAdapter rcAdapter_newest = new ProductPromoViewAdapter(getActivity(), MainActivity.products_selection_newest, 3);
            rvViewNewest.setAdapter(rcAdapter_newest);

            cover.setVisibility(View.GONE);
        }else {
            getAd();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getAd();
            }
        });

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.replaceFragment(FragmentProduct.newInstance("SEARCH"), 4);
            }
        });

        tv_cobalagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAd();
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        mSlider.stopAutoCycle();
        super.onStop();
    }

//    private List<Category> getAllItemList(){
//
//        List<Category> allItems = new ArrayList<Category>();
//        allItems.add(new Category("United States", "https://drop.ndtv.com/albums/COOKS/pasta-vegetarian/pastaveg_640x480.jpg"));
//        allItems.add(new Category("Canada", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQWvmHj-P4bpQU_hRkRHtgU0MzeOqurzuNgk5ERkX4W2UEvwJycw"));
//        allItems.add(new Category("United Kingdom", "https://www.wri.org/sites/default/files/styles/related_topics/public/Food_Aisle.jpg?itok=JPwPTWYJ"));
//        allItems.add(new Category("Germany", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQeFlxJuSxFAZ9KdqlRz34ZlqmrvU-O-XvBCr1BsxLlmKSuDCogA"));
//        allItems.add(new Category("Sweden", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQgk5fJJXAjnNrvRFgyixG13srq-UpiVEghcLXgzlnYnG3KEhYi"));
//        allItems.add(new Category("United Kingdom", "https://i.ytimg.com/vi/sWisuBtbypE/hqdefault.jpg"));
//        allItems.add(new Category("Germany", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSr9PoRelJ9nfnzq3LFB9uQMAryLLWTB45eb5bx5E7eU2VIYMgj"));
//
//        return allItems;
//    }

    private void getAllCategoryProducts(final ProgressDialog pDialog) {
        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        APIServices api = retrofit.create(APIServices.class);
        Call<GetValue<Category>> call = api.getallproductcategory(random);
        call.enqueue(new Callback<GetValue<Category>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Category>> call, @NonNull Response<GetValue<Category>> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Category> listDataProduct = response.body().getData();
                        MainActivity.categories = listDataProduct;
                        rView.setHasFixedSize(true);
                        rView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                        CategoryViewAdapter rcAdapter = new CategoryViewAdapter(getActivity(), listDataProduct);
                        rView.setAdapter(rcAdapter);

                        getProductDiscount(pDialog);
                    } else {
                        pDialog.dismiss();
                        cover.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    cover.setVisibility(View.GONE);
                    rl.setVisibility(View.VISIBLE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Category>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                cover.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    private void getProductDiscount(final ProgressDialog pDialog) {
        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        APIServices api = retrofit.create(APIServices.class);
        Call<GetValue<Product>> call = api.getallproductdiscount(random);
        call.enqueue(new Callback<GetValue<Product>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Product>> call, @NonNull Response<GetValue<Product>> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Product> listDataProduct = response.body().getData();
                        String lastnamaproduk="";
                        MainActivity.products_discount = listDataProduct;
                        MainActivity.products_selection_discount = new ArrayList<>();
                        for (int a=0; a<listDataProduct.size(); a++){
                            if (!lastnamaproduk.equals(listDataProduct.get(a).getNamaproduk())){
                                MainActivity.products_selection_discount.add(new Product(listDataProduct.get(a).getIddetailproduk(),
                                        listDataProduct.get(a).getNamaproduk(), listDataProduct.get(a).getKategori(), listDataProduct.get(a).getDeskripsi(),
                                        listDataProduct.get(a).getSatuan(), listDataProduct.get(a).getHarga(), listDataProduct.get(a).getGambar(),
                                        listDataProduct.get(a).getDiscount(), listDataProduct.get(a).getStatusketersediaan()));
                            }
                            lastnamaproduk = listDataProduct.get(a).getNamaproduk();
                        }

                        rvViewDiscount.setNestedScrollingEnabled(false);
                        rvViewDiscount.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                        rvViewDiscount.setItemAnimator(new DefaultItemAnimator());
                        ProductPromoViewAdapter rcAdapter = new ProductPromoViewAdapter(getActivity(), MainActivity.products_selection_discount, 2);
                        rvViewDiscount.setAdapter(rcAdapter);

                        getNewestProduct(pDialog);

                    } else {
                        pDialog.dismiss();
                        cover.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    cover.setVisibility(View.GONE);
                    rl.setVisibility(View.VISIBLE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Product>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                cover.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    private void getNewestProduct(final ProgressDialog pDialog) {
        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        APIServices api = retrofit.create(APIServices.class);
        Call<GetValue<Product>> call = api.getnewestproduct(random);
        call.enqueue(new Callback<GetValue<Product>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Product>> call, @NonNull Response<GetValue<Product>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Product> listDataProduct = response.body().getData();
                        String lastnamaproduk="";
                        MainActivity.products_newest = listDataProduct;
                        MainActivity.products_selection_newest = new ArrayList<>();
                        for (int a=0; a<listDataProduct.size(); a++){
                            if (!lastnamaproduk.equals(listDataProduct.get(a).getNamaproduk())){
                                MainActivity.products_selection_newest.add(new Product(listDataProduct.get(a).getIddetailproduk(),
                                        listDataProduct.get(a).getNamaproduk(), listDataProduct.get(a).getKategori(), listDataProduct.get(a).getDeskripsi(),
                                        listDataProduct.get(a).getSatuan(), listDataProduct.get(a).getHarga(), listDataProduct.get(a).getGambar(),
                                        listDataProduct.get(a).getDiscount(), listDataProduct.get(a).getStatusketersediaan()));
                            }
                            lastnamaproduk = listDataProduct.get(a).getNamaproduk();
                        }
                        rvViewNewest.setNestedScrollingEnabled(false);
                        rvViewNewest.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                        rvViewNewest.setItemAnimator(new DefaultItemAnimator());
                        ProductPromoViewAdapter rcAdapter = new ProductPromoViewAdapter(getActivity(), MainActivity.products_selection_newest, 3);
                        rvViewNewest.setAdapter(rcAdapter);

                        rl.setVisibility(View.GONE);
                        cover.setVisibility(View.GONE);
                    } else {
                        pDialog.dismiss();
                        cover.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    cover.setVisibility(View.GONE);
                    rl.setVisibility(View.VISIBLE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Product>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                cover.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    private void getAd() {
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
        Call<GetValue<Ad>> call = api.getactivead(random);
        call.enqueue(new Callback<GetValue<Ad>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Ad>> call, @NonNull Response<GetValue<Ad>> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Ad> listDataAd = response.body().getData();
                        MainActivity.ads = listDataAd;
                        HashMap<String,String> url_maps = new HashMap<String, String>();

                        for (int a = 0; a < listDataAd.size(); a++) {
                            url_maps.put(listDataAd.get(a).getJuduliklan(), Utilities.getURLImageIklan()+listDataAd.get(a).getGambar());
                        }

                        for(String name : url_maps.keySet()){
                            TextSliderView textSliderView = new TextSliderView(getContext());
                            textSliderView
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);

                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);

                            mSlider.addSlider(textSliderView);
                        }

                        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        mSlider.setDuration(4000);

                        getAllCategoryProducts(pDialog);

                    } else {
                        pDialog.dismiss();
                        cover.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    cover.setVisibility(View.GONE);
                    rl.setVisibility(View.VISIBLE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Ad>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                cover.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

}
