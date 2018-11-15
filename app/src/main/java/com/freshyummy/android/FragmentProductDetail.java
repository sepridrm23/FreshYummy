package com.freshyummy.android;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afi on 19/03/18.
 */

public class FragmentProductDetail extends Fragment {
    static String IMAGE, CATEGORY, DESCRIPTION, NAME, PRICE, DISCOUNT;
    SliderLayout image;
    RecyclerView rvOther;
    static List<Product> PRODUCT;
    ImageView back, photo_out;

    public static FragmentProductDetail newInstance(String image, String name, String price, String category, String description, String discount, List<Product> product) {
        IMAGE = image;
        CATEGORY = category;
        DESCRIPTION = description;
        NAME = name;
        PRICE = price;
        DISCOUNT = discount;
        PRODUCT = product;
        return new FragmentProductDetail();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_product_detail, container, false);
        image = v.findViewById(R.id.image);
        rvOther = v.findViewById(R.id.recycler_view_sejenis);
        back = v.findViewById(R.id.back);
        photo_out = v.findViewById(R.id.photo_out);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int viewPagerWidth = Math.round(outMetrics.widthPixels);
//        int more = viewPagerWidth/8;
//        int viewPagerHeight = viewPagerWidth/2+more;
//        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(viewPagerWidth, viewPagerWidth));
        image.setLayoutParams(new RelativeLayout.LayoutParams(viewPagerWidth, viewPagerWidth));

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put(NAME, Utilities.getURLImageProduk()+IMAGE);
        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",name);

            image.addSlider(textSliderView);
        }
        image.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        image.setDuration(4000);

        TextView name = v.findViewById(R.id.name);
        TextView price = v.findViewById(R.id.price);
        TextView pricediscount = v.findViewById(R.id.price_discount);
        TextView discount = v.findViewById(R.id.discount);
        TextView description = v.findViewById(R.id.tv_description);
        TextView category = v.findViewById(R.id.tv_category);

        if (PRODUCT.get(0).getStatusketersediaan().equals("0")){
            photo_out.setVisibility(View.VISIBLE);
        }else {
            photo_out.setVisibility(View.GONE);
        }

        category.setText(CATEGORY);
        description.setText(DESCRIPTION);
        name.setText(NAME);
        price.setText(Utilities.getCurrency(PRICE));
        if (DISCOUNT.equals("0")){
            pricediscount.setVisibility(View.GONE);
            discount.setVisibility(View.GONE);
        }else {
            pricediscount.setVisibility(View.VISIBLE);
            discount.setVisibility(View.VISIBLE);
            float xharga = 1 - (Float.parseFloat(DISCOUNT)/100);
            float hargadiscount = Float.parseFloat(PRICE) / xharga;
            pricediscount.setText(Utilities.getCurrency(String.valueOf(String.format("%.0f", hargadiscount))));
            discount.setText("diskon "+DISCOUNT+"%");
        }
        Utilities.strikeThroughText(pricediscount);

        if (MainActivity.products_selection_temp.size() != 0){
            if (MainActivity.products_selection_temp.get(0).getKategori().equals(CATEGORY)){
                List<Product> listDataProduct = new ArrayList<>();
                for (int a=0; a<MainActivity.products_selection_temp.size(); a++){
                    if (!MainActivity.products_selection_temp.get(a).getNamaproduk().equals(NAME)) {
                        listDataProduct.add(new Product(MainActivity.products_selection_temp.get(a).getIddetailproduk(),
                                MainActivity.products_selection_temp.get(a).getNamaproduk(),
                                MainActivity.products_selection_temp.get(a).getKategori(),
                                MainActivity.products_selection_temp.get(a).getDeskripsi(),
                                MainActivity.products_selection_temp.get(a).getSatuan(),
                                MainActivity.products_selection_temp.get(a).getHarga(),
                                MainActivity.products_selection_temp.get(a).getGambar(),
                                MainActivity.products_selection_temp.get(a).getDiscount(),
                                MainActivity.products_selection_temp.get(a).getStatusketersediaan()));
                    }
                }
//                rvOther.setHasFixedSize(true);
//                rvOther.setLayoutManager(new GridLayoutManager(getActivity(), 4));
//                ProductPromoViewAdapter rcAdapter = new ProductPromoViewAdapter(getActivity(), listDataProduct);
//                rvOther.setAdapter(rcAdapter);
//                rvOther.animate();
                rvOther.setNestedScrollingEnabled(false);
                rvOther.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                rvOther.setItemAnimator(new DefaultItemAnimator());
                ProductPromoViewAdapter rcAdapter = new ProductPromoViewAdapter(getActivity(), listDataProduct, 1);
                rvOther.setAdapter(rcAdapter);
            }else {
                getProductOther();
            }
        }else {
            getProductOther();
        }

        LinearLayout cart = v.findViewById(R.id.ll_add_cart);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_cart);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                RecyclerView rView = dialog.findViewById(R.id.recycler_view_product);
                ImageButton ibClose = dialog.findViewById(R.id.ib_close);
                ImageView ivProduct = dialog.findViewById(R.id.imageView2);
                TextView tvProduct = dialog.findViewById(R.id.tv_name_product);
                LinearLayout llAddCart = dialog.findViewById(R.id.ll_add_cart);
                CardView cvDiskon = dialog.findViewById(R.id.card_discount);
                TextView tvDiskon = dialog.findViewById(R.id.discount);

                Glide.with(getContext()).load(Utilities.getURLImageProduk()+IMAGE).into(ivProduct);
                tvProduct.setText(NAME);

                if (PRODUCT.get(0).getDiscount().equals("0")){
                    cvDiskon.setVisibility(View.GONE);
                }else{
                    cvDiskon.setVisibility(View.VISIBLE);
                    tvDiskon.setText("diskon "+PRODUCT.get(0).getDiscount()+"%");
                }

                rView.setLayoutManager(new LinearLayoutManager(getContext()));
                rView.setHasFixedSize(true);
                ProductAddViewAdapter rcAdapter = new ProductAddViewAdapter(getContext(), PRODUCT);
                rView.setAdapter(rcAdapter);

                ibClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        MainActivity.temp_carts.clear();
                    }
                });

                llAddCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (MainActivity.temp_carts.size() == 0){
                            Snackbar.make(dialog.findViewById(android.R.id.content), "Silahkan tambah jumlah beli",
                                    Snackbar.LENGTH_SHORT).show();
                        }else {
                            dialog.dismiss();
                            for (int a = 0; a < MainActivity.temp_carts.size(); a++) {
                                if (!MainActivity.temp_carts.get(a).getJumlahbeli().equals("0")) {
                                    DataHelper dbHelper = new DataHelper(getContext());
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    Cursor cursor = db.rawQuery("SELECT * FROM cart where iddetailproduk = '" + MainActivity.temp_carts.get(a).getIddetailproduk() + "'", null);
                                    if (cursor.getCount() == 0) {
                                        dbHelper.onInsert(db, MainActivity.temp_carts.get(a).getIddetailproduk(), MainActivity.temp_carts.get(a).getJumlahbeli());
                                    } else {
                                        dbHelper.onUpdate(db, MainActivity.temp_carts.get(a).getIddetailproduk(), MainActivity.temp_carts.get(a).getJumlahbeli());
                                    }
                                }
                            }
                            MainActivity.temp_carts.clear();
//                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Produk ditambah ke keranjang",
//                                    Snackbar.LENGTH_SHORT).show();
//                            new Timer().schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        public void run() {
                                            MainActivity.addBadgeCart(getContext());
//                                        }
//                                    });
//                                }
//                            }, 2300);
                        }
                    }
                });

                dialog.setCancelable(true);
//                Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations=R.style.com_facebook_auth_dialog;
                dialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        image.stopAutoCycle();
        super.onStop();
    }

    private void getProductOther() {
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
        Call<GetValue<Product>> call = api.getproductcategoryother(random, CATEGORY, NAME);
        call.enqueue(new Callback<GetValue<Product>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Product>> call, @NonNull Response<GetValue<Product>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Product> listDataProduct = new ArrayList<>();
                        MainActivity.products_other = response.body().getData();
                        String lastnamaproduk = "";
                        for (int a=0; a<response.body().getData().size(); a++){
                            if (!lastnamaproduk.equals(response.body().getData().get(a).getNamaproduk())){
                                listDataProduct.add(new Product(response.body().getData().get(a).getIddetailproduk(),
                                        response.body().getData().get(a).getNamaproduk(), response.body().getData().get(a).getKategori(), response.body().getData().get(a).getDeskripsi(),
                                        response.body().getData().get(a).getSatuan(), response.body().getData().get(a).getHarga(), response.body().getData().get(a).getGambar(),
                                        response.body().getData().get(a).getDiscount(), response.body().getData().get(a).getStatusketersediaan()));
                            }
                            lastnamaproduk = response.body().getData().get(a).getNamaproduk();
                        }
//                        rvOther.setHasFixedSize(true);
//                        rvOther.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//                        ProductPromoViewAdapter rcAdapter = new ProductPromoViewAdapter(getActivity(), listDataProduct);
//                        rvOther.setAdapter(rcAdapter);
//                        rvOther.animate();
                        rvOther.setNestedScrollingEnabled(false);
                        rvOther.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                        rvOther.setItemAnimator(new DefaultItemAnimator());
                        ProductPromoViewAdapter rcAdapter = new ProductPromoViewAdapter(getActivity(), listDataProduct, 4);
                        rvOther.setAdapter(rcAdapter);
                    } else {
                        pDialog.dismiss();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Product>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

}
