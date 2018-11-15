package com.freshyummy.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
 * Created by afi on 17/03/18.
 */

public class FragmentProduct extends Fragment {
    static String KATEGORI;
    RecyclerView rView;
    Spinner sp_category;
    SearchView search_view;
    ImageView iv_title, back;
    RelativeLayout rl, rl2, rl3;
    TextView tv_cobalagi;
    String[] categoryArray;
//    int check = 0;
    SwipeRefreshLayout swipeRefreshLayout;

    public static FragmentProduct newInstance(String kategori) {
        KATEGORI = kategori;
        return new FragmentProduct();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        rView = v.findViewById(R.id.recycler_product);
        iv_title = v.findViewById(R.id.iv_title);
        back = v.findViewById(R.id.back);
        search_view = v.findViewById(R.id.search_view);
        sp_category = v.findViewById(R.id.sp_category);
        rl = v.findViewById(R.id.rl);
        rl2 = v.findViewById(R.id.rl2);
        rl3 = v.findViewById(R.id.rl3);
        tv_cobalagi = v.findViewById(R.id.tv_cobalagi);
        swipeRefreshLayout = v.findViewById(R.id.swipe);

        if (MainActivity.products_selection_temp.size() != 0){
            if (MainActivity.products_selection_temp.get(0).getKategori().equals(KATEGORI)){
                List<Product> listDataProduct = MainActivity.products_selection_temp;
                rView.setHasFixedSize(true);
                rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                ProductViewAdapter rcAdapter = new ProductViewAdapter(getActivity(), listDataProduct);
                rView.setAdapter(rcAdapter);
                rView.animate();
            }else if (KATEGORI.equals("SEARCH")){
                if (MainActivity.products_search_temp.size() != 0){
                    List<Product> listDataProduct = MainActivity.products_search_temp;
                    rView.setHasFixedSize(true);
                    rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    ProductViewAdapter rcAdapter = new ProductViewAdapter(getActivity(), listDataProduct);
                    rView.setAdapter(rcAdapter);
                    rView.animate();
                }else {
                    rl.setVisibility(View.GONE);
                    rl2.setVisibility(View.GONE);
                    rl3.setVisibility(View.VISIBLE);
                }

                requestFocus(search_view);
                iv_title.setVisibility(View.GONE);
                Utilities.hideKeyboard(getContext());
                search_view.setIconified(false);
            } else {
                rView.setVisibility(View.INVISIBLE);
                getProduct(KATEGORI);
            }
        }else if (KATEGORI.equals("SEARCH")){
            if (MainActivity.products_search_temp.size() != 0){
                List<Product> listDataProduct = MainActivity.products_search_temp;
                rView.setHasFixedSize(true);
                rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                ProductViewAdapter rcAdapter = new ProductViewAdapter(getActivity(), listDataProduct);
                rView.setAdapter(rcAdapter);
                rView.animate();
            }else {
                rl.setVisibility(View.GONE);
                rl2.setVisibility(View.GONE);
                rl3.setVisibility(View.VISIBLE);
            }

            requestFocus(search_view);
            iv_title.setVisibility(View.GONE);
            Utilities.hideKeyboard(getContext());
            search_view.setIconified(false);
        } else {
            rView.setVisibility(View.INVISIBLE);
            getProduct(KATEGORI);
        }

        if (KATEGORI.equals("SEARCH")){
            categoryArray = new String[MainActivity.categories.size()+1];
            for (int i = 0; i < MainActivity.categories.size()+1; i++) {
                if (i == 0) {
                    categoryArray[i] = "Pilih Kategori";
                } else {
                    categoryArray[i] = Utilities.capitalizeWord(MainActivity.categories.get(i-1).getNamakategori());
                }
            }
        }else {
            categoryArray = new String[MainActivity.categories.size()];
            int a=0;
            for (int i = 0; i < MainActivity.categories.size()+1; i++) {
                if (i == 0) {
                    categoryArray[i] = Utilities.capitalizeWord(KATEGORI);
                } else {
                    if (!MainActivity.categories.get(i-1).getNamakategori().equals(KATEGORI)) {
                        categoryArray[a] = Utilities.capitalizeWord(MainActivity.categories.get(i-1).getNamakategori());
                    }else {
                        if (i==0){
                            a=0;
                        }else {
                            a--;
                        }
                    }
                }
                a++;
            }
        }
        ArrayAdapter<String> sortbycategoryAdapter = new ArrayAdapter<>(getContext(),
                R.layout.simple_list_item, categoryArray);
        sp_category.setPrompt("Pilih Kategori");
        sp_category.setAdapter(sortbycategoryAdapter);

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!categoryArray[position].equals("Pilih Kategori")) {
//                        Log.e("kat", KATEGORI + "|" + categoryArray[position]+ "|" + last_category[position]);
                    if (categoryArray[position].equals(KATEGORI)) {
                        List<Product> listDataProduct = MainActivity.products_selection_temp;
                        rView.setHasFixedSize(true);
                        rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        ProductViewAdapter rcAdapter = new ProductViewAdapter(getActivity(), listDataProduct);
                        rView.setAdapter(rcAdapter);
                        rView.animate();
                    } else {
                        KATEGORI = categoryArray[position];
                        categoryArray = new String[MainActivity.categories.size()];
                        int a=0;
                        for (int i = 0; i < MainActivity.categories.size()+1; i++) {
                            if (i == 0) {
                                categoryArray[i] = Utilities.capitalizeWord(KATEGORI);
                            } else {
                                if (!MainActivity.categories.get(i-1).getNamakategori().equals(KATEGORI)) {
                                    categoryArray[a] = Utilities.capitalizeWord(MainActivity.categories.get(i-1).getNamakategori());
                                }else {
                                    if (i==0){
                                        a=0;
                                    }else {
                                        a--;
                                    }
                                }
                            }
                            a++;
                        }
                        ArrayAdapter<String> sortbycategoryAdapter = new ArrayAdapter<>(getContext(),
                                R.layout.simple_list_item, categoryArray);
                        sp_category.setPrompt("Pilih Kategori");
                        sp_category.setAdapter(sortbycategoryAdapter);
                        getProduct(KATEGORI);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProduct(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                iv_title.setVisibility(View.GONE);
                return false;
            }
        });

        search_view.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                iv_title.setVisibility(View.VISIBLE);
                if (KATEGORI.equals("SEARCH")){
                    if (MainActivity.products_search_temp.size() != 0) {
                        rl.setVisibility(View.GONE);
                        rl2.setVisibility(View.GONE);
                        rl3.setVisibility(View.GONE);
                        List<Product> listDataProduct = MainActivity.products_search_temp;
                        rView.setHasFixedSize(true);
                        rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        ProductViewAdapter rcAdapter = new ProductViewAdapter(getActivity(), listDataProduct);
                        rView.setAdapter(rcAdapter);
                        rView.animate();
                    }else {
                        rl.setVisibility(View.GONE);
                        rl2.setVisibility(View.GONE);
                        rl3.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (MainActivity.products_selection_temp.size() != 0){
                        rl.setVisibility(View.GONE);
                        rl2.setVisibility(View.GONE);
                        rl3.setVisibility(View.GONE);
                        List<Product> listDataProduct = MainActivity.products_selection_temp;
                        rView.setHasFixedSize(true);
                        rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        ProductViewAdapter rcAdapter = new ProductViewAdapter(getActivity(), listDataProduct);
                        rView.setAdapter(rcAdapter);
                        rView.animate();
                    }else {
                        rl.setVisibility(View.GONE);
                        rl2.setVisibility(View.GONE);
                        rl3.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });

        search_view.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_title.setVisibility(View.GONE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getProduct(KATEGORI);
            }
        });

        tv_cobalagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProduct(KATEGORI);
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

//    private List<Product> getAllItemList(){
//
//        List<Product> allItems = new ArrayList<Product>();
//        allItems.add(new Product("1", "Batu Ginjal", "Sayuran","sayuran dengan cita rasa yang mewah", "kg", "Rp1000.000", "https://drop.ndtv.com/albums/COOKS/pasta-vegetarian/pastaveg_640x480.jpg","1"));
//        allItems.add(new Product("1", "Batu Ginjal", "Sayuran","sayuran dengan cita rasa yang mewah", "kg", "Rp10.000", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQWvmHj-P4bpQU_hRkRHtgU0MzeOqurzuNgk5ERkX4W2UEvwJycw","1"));
//        allItems.add(new Product("1", "Batu Ginjal", "Sayuran","sayuran dengan cita rasa yang mewah", "kg", "Rp100.000", "https://www.wri.org/sites/default/files/styles/related_topics/public/Food_Aisle.jpg?itok=JPwPTWYJ","1"));
//        allItems.add(new Product("1", "Batu Ginjal", "Sayuran","sayuran dengan cita rasa yang mewah", "kg", "Rp100.000", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQeFlxJuSxFAZ9KdqlRz34ZlqmrvU-O-XvBCr1BsxLlmKSuDCogA","1"));
//        allItems.add(new Product("1", "Batu Ginjal", "Sayuran","sayuran dengan cita rasa yang mewah", "kg", "Rp10.000", "https://i.ytimg.com/vi/sWisuBtbypE/hqdefault.jpg","1"));
//        allItems.add(new Product("1", "Batu Ginjal", "Sayuran","sayuran dengan cita rasa yang mewah", "kg", "Rp100.000", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSr9PoRelJ9nfnzq3LFB9uQMAryLLWTB45eb5bx5E7eU2VIYMgj","1"));
//
//        return allItems;
//    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void getProduct(String kategori) {
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
        Call<GetValue<Product>> call = api.getproductcategory(random, kategori);
        call.enqueue(new Callback<GetValue<Product>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Product>> call, @NonNull Response<GetValue<Product>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Product> listDataProduct = response.body().getData();
                        MainActivity.products_temp = listDataProduct;
                        String lastnamaproduk="";
                        MainActivity.products_selection_temp = new ArrayList<>();
                        for (int a=0; a<listDataProduct.size(); a++){
                            if (!lastnamaproduk.equals(listDataProduct.get(a).getNamaproduk())){
                                MainActivity.products_selection_temp.add(new Product(listDataProduct.get(a).getIddetailproduk(),
                                        listDataProduct.get(a).getNamaproduk(), listDataProduct.get(a).getKategori(), listDataProduct.get(a).getDeskripsi(),
                                        listDataProduct.get(a).getSatuan(), listDataProduct.get(a).getHarga(), listDataProduct.get(a).getGambar(),
                                        listDataProduct.get(a).getDiscount(), listDataProduct.get(a).getStatusketersediaan()));
                            }
                            lastnamaproduk = listDataProduct.get(a).getNamaproduk();
                        }
                        rl.setVisibility(View.GONE);
                        rl2.setVisibility(View.GONE);
                        rl3.setVisibility(View.GONE);

                        rView.setVisibility(View.VISIBLE);
                        rView.setHasFixedSize(true);
                        rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        ProductViewAdapter rcAdapter = new ProductViewAdapter(getActivity(), MainActivity.products_selection_temp);
                        rView.setAdapter(rcAdapter);
                        rView.animate();
                    } else {
                        pDialog.dismiss();
                        rView.setVisibility(View.INVISIBLE);
                        rl.setVisibility(View.VISIBLE);
                        rl2.setVisibility(View.GONE);
                        rl3.setVisibility(View.GONE);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    rView.setVisibility(View.INVISIBLE);
                    rl.setVisibility(View.VISIBLE);
                    rl2.setVisibility(View.GONE);
                    rl3.setVisibility(View.GONE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Product>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                rView.setVisibility(View.INVISIBLE);
                rl.setVisibility(View.VISIBLE);
                rl2.setVisibility(View.GONE);
                rl3.setVisibility(View.GONE);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    private void searchProduct(String searchkey) {
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
        Call<GetValue<Product>> call = api.searchproduct(random, searchkey);
        call.enqueue(new Callback<GetValue<Product>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Product>> call, @NonNull Response<GetValue<Product>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Product> listDataProduct = new ArrayList<>();
                        MainActivity.products_temp = response.body().getData();
                        String lastnamaproduk="";
                        if (response.body().getData().size() != 0) {
                            for (int a = 0; a < response.body().getData().size(); a++) {
                                if (!lastnamaproduk.equals(response.body().getData().get(a).getNamaproduk())) {

                                    if (MainActivity.products_search_temp.size() == 0){
                                        MainActivity.products_search_temp.add(new Product(response.body().getData().get(a).getIddetailproduk(),
                                                response.body().getData().get(a).getNamaproduk(), response.body().getData().get(a).getKategori(),
                                                response.body().getData().get(a).getDeskripsi(),
                                                response.body().getData().get(a).getSatuan(), response.body().getData().get(a).getHarga(),
                                                response.body().getData().get(a).getGambar(),
                                                response.body().getData().get(a).getDiscount(),
                                                response.body().getData().get(a).getStatusketersediaan()));
                                    }
                                    else {
                                        boolean flag=false;
                                        for (int i = 0; i < MainActivity.products_search_temp.size(); i++) {
                                            if (response.body().getData().get(a).getNamaproduk().equals(MainActivity.products_search_temp.get(i).getNamaproduk())) {
                                                flag = true;
                                            }
                                        }
                                        if (!flag) {
                                            MainActivity.products_search_temp.add(new Product(response.body().getData().get(a).getIddetailproduk(),
                                                    response.body().getData().get(a).getNamaproduk(), response.body().getData().get(a).getKategori(),
                                                    response.body().getData().get(a).getDeskripsi(),
                                                    response.body().getData().get(a).getSatuan(), response.body().getData().get(a).getHarga(),
                                                    response.body().getData().get(a).getGambar(),
                                                    response.body().getData().get(a).getDiscount(),
                                                    response.body().getData().get(a).getStatusketersediaan()));
                                        }
                                    }

                                    listDataProduct.add(new Product(response.body().getData().get(a).getIddetailproduk(),
                                            response.body().getData().get(a).getNamaproduk(), response.body().getData().get(a).getKategori(),
                                            response.body().getData().get(a).getDeskripsi(),
                                            response.body().getData().get(a).getSatuan(), response.body().getData().get(a).getHarga(),
                                            response.body().getData().get(a).getGambar(),
                                            response.body().getData().get(a).getDiscount(),
                                            response.body().getData().get(a).getStatusketersediaan()));
                                }
                                lastnamaproduk = response.body().getData().get(a).getNamaproduk();
                            }

                            rl.setVisibility(View.GONE);
                            rl2.setVisibility(View.GONE);
                            rl3.setVisibility(View.GONE);

                            rView.setVisibility(View.VISIBLE);
                            rView.setHasFixedSize(true);
                            rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            ProductViewAdapter rcAdapter = new ProductViewAdapter(getActivity(), listDataProduct);
                            rView.setAdapter(rcAdapter);
                            rView.animate();

                            categoryArray = new String[MainActivity.categories.size()+1];
                            for (int i = 0; i < MainActivity.categories.size()+1; i++) {
                                if (i == 0) {
                                    categoryArray[i] = "Pilih Kategori";
                                } else {
                                    categoryArray[i] = Utilities.capitalizeWord(MainActivity.categories.get(i-1).getNamakategori());
                                }
                            }

                            KATEGORI = "SEARCH";
                            ArrayAdapter<String> sortbycategoryAdapter = new ArrayAdapter<>(getContext(),
                                    R.layout.simple_list_item, categoryArray);
                            sp_category.setPrompt("Pilih Kategori");
                            sp_category.setAdapter(sortbycategoryAdapter);

                        }else {
                            rView.setVisibility(View.INVISIBLE);
                            rl.setVisibility(View.GONE);
                            rl2.setVisibility(View.VISIBLE);
                            rl3.setVisibility(View.GONE);
                        }
                    } else {
                        pDialog.dismiss();
                        rView.setVisibility(View.INVISIBLE);
                        rl.setVisibility(View.GONE);
                        rl2.setVisibility(View.VISIBLE);
                        rl3.setVisibility(View.GONE);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    pDialog.dismiss();
                    rView.setVisibility(View.INVISIBLE);
                    rl.setVisibility(View.GONE);
                    rl2.setVisibility(View.VISIBLE);
                    rl3.setVisibility(View.GONE);
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
                rView.setVisibility(View.INVISIBLE);
                rl.setVisibility(View.VISIBLE);
                rl2.setVisibility(View.GONE);
                rl3.setVisibility(View.GONE);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }
}
