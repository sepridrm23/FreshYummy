package com.freshyummy.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
 * Created by afi on 22/03/18.
 */

public class DetailBelanjaanActivity extends AppCompatActivity {
    String idtransaksi, namabank="", norekening="", namarekening="", jumlahtransfer="", gambartransfer="", tanggaltransfer="";
    TextView tv_status, tv_status_description, tv_status_date_order, tv_status_date_send,
            tv_delivery_address, tv_subtotal, tv_ongkir, tv_poin_uses, tv_promo, tv_total_bayar,
            tv_metode_bayar, tv_poin_get, tv_promo_title, tv_poin_title, tv_idtransaksi, tv_cobalagi;
    EditText et_delivery_address_note, et_product_note;
    LinearLayout ll_poin_get, ll_promo, ll_poin_used, cover;
    RecyclerView rv_product;
    RelativeLayout rl_bayar, rl;
    int totalbayar = 0;
    public static boolean flag=true;
    Button btn_tanya, btn_batal, btn_tracking, btn_transfer;
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_belanjaan);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tv_status = findViewById(R.id.tv_status);
        tv_status_description = findViewById(R.id.tv_status_description);
        tv_status_date_order = findViewById(R.id.tv_status_date_order);
        tv_status_date_send = findViewById(R.id.tv_status_date_send);
        tv_delivery_address = findViewById(R.id.tv_delivery_address);
        rv_product = findViewById(R.id.recycler_view_product);
        et_delivery_address_note = findViewById(R.id.et_delivery_address_note);
        et_product_note = findViewById(R.id.et_product_note);
        tv_subtotal = findViewById(R.id.tv_subtotal);
        tv_ongkir = findViewById(R.id.tv_ongkir);
        tv_poin_uses = findViewById(R.id.tv_poin_used);
        tv_total_bayar = findViewById(R.id.tv_total_bayar);
        tv_poin_get = findViewById(R.id.tv_poin_get);
        tv_metode_bayar = findViewById(R.id.tv_metode_bayar);
        tv_promo = findViewById(R.id.tv_promo);
        ll_poin_get = findViewById(R.id.ll_poin_get);
        ll_poin_used = findViewById(R.id.ll_poin_used);
        ll_promo = findViewById(R.id.ll_promo);
        cover = findViewById(R.id.cover);
        tv_poin_title = findViewById(R.id.tv_poin_title);
        tv_promo_title = findViewById(R.id.tv_promo_title);
        btn_batal = findViewById(R.id.btn_batal);
        btn_tanya = findViewById(R.id.btn_tanya);
        btn_tracking = findViewById(R.id.btn_tracking);
        btn_transfer = findViewById(R.id.btn_transfer);
        rl_bayar = findViewById(R.id.rl_bayar);
        tv_idtransaksi = findViewById(R.id.tv_idtransaksi);
        back = findViewById(R.id.back);
        rl = findViewById(R.id.rl);
        tv_cobalagi = findViewById(R.id.tv_cobalagi);

        idtransaksi = getIntent().getStringExtra("IDTRANSAKSI");
        tv_idtransaksi.setText("FY-"+idtransaksi);
        flag=true;

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBelanjaanActivity.this);
                builder.setCancelable(true)
                .setTitle("Konfirmasi")
                .setMessage("Lanjutkan batalkan transaksi ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        canceltransaksi();
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
        });

        btn_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailBelanjaanActivity.this, TrackingActivity.class);
                intent.putExtra("IDTRANSAKSI", idtransaksi);
                startActivity(intent);
            }
        });

        btn_tanya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "081278909143";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        btn_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!norekening.equals("")){
                    Intent intent = new Intent(DetailBelanjaanActivity.this, TransferActivity.class);
                    intent.putExtra("TOTAL", String.valueOf(totalbayar));
                    intent.putExtra("IDTRANSAKSI", idtransaksi);
                    intent.putExtra("NAMABANK", namabank);
                    intent.putExtra("NOREKENING", norekening);
                    intent.putExtra("NAMAREKENING", namarekening);
                    intent.putExtra("JUMLAHTRANSFER", jumlahtransfer);
                    intent.putExtra("GAMBARTRANSFER", gambartransfer);
                    intent.putExtra("TANGGALTRANSFER", tanggaltransfer);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(DetailBelanjaanActivity.this, TransferActivity.class);
                    intent.putExtra("TOTAL", String.valueOf(totalbayar));
                    intent.putExtra("IDTRANSAKSI", idtransaksi);
                    intent.putExtra("NAMABANK", "");
                    intent.putExtra("NOREKENING", "");
                    intent.putExtra("NAMAREKENING", "");
                    intent.putExtra("JUMLAHTRANSFER", "");
                    intent.putExtra("GAMBARTRANSFER", "");
                    intent.putExtra("TANGGALTRANSFER", "");
                    startActivity(intent);
                }
            }
        });

        tv_cobalagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettransactiondetail();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            gettransactiondetail();
        }
    }

    private void gettransactiondetail() {
        final ProgressDialog pDialog = new ProgressDialog(DetailBelanjaanActivity.this);
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
        Call<GetValue<Belanjaan>> call = api.getusertransactiondetail(random, idtransaksi);
        call.enqueue(new Callback<GetValue<Belanjaan>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Belanjaan>> call, @NonNull Response<GetValue<Belanjaan>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Belanjaan> listData = response.body().getData();
                        List<Cart> listCart = new ArrayList<>();
                        cover.setVisibility(View.GONE);
                        rl.setVisibility(View.GONE);
                        flag = false;
                        totalbayar=0;

                        for (int a=0; a<listData.size(); a++){
                            totalbayar = totalbayar + (Integer.parseInt(listData.get(a).getHargabeli()) * Integer.parseInt(listData.get(a).getJumlahbeli()));
                            listCart.add(new Cart(listData.get(a).iddetailtransaksi, listData.get(a).iddetailtransaksi,
                                    listData.get(a).getNamaproduk(), listData.get(a).getGambar(), listData.get(a).getSatuan(),
                                    listData.get(a).getHargabeli(), listData.get(a).getJumlahbeli(), "0", "1"));
                        }
                        rv_product.setLayoutManager(new LinearLayoutManager(DetailBelanjaanActivity.this));
                        rv_product.setHasFixedSize(true);
                        CheckoutViewAdapter rcAdapter = new CheckoutViewAdapter(DetailBelanjaanActivity.this, listCart);
                        rv_product.setAdapter(rcAdapter);
                        if (!listData.get(0).getKeteranganbarang().isEmpty()) {
                            et_product_note.setText(listData.get(0).getKeteranganbarang());
                        }

                        tv_delivery_address.setText(listData.get(0).getPenerimapengantaran()+"\n"+listData.get(0).getTelepon()+"\n"+listData.get(0).getAlamatpengantaran());
                        if (!listData.get(0).getKeteranganalamat().isEmpty()) {
                            et_delivery_address_note.setText(listData.get(0).getKeteranganalamat());
                        }

                        tv_subtotal.setText(Utilities.getCurrency(String.valueOf(totalbayar)));
                        tv_ongkir.setText(Utilities.getCurrency(listData.get(0).getOngkir()));

                        if (!listData.get(0).getJenispromo().isEmpty()){
                            if (listData.get(0).getJenispromo().equals("1")) {
                                if (listData.get(0).getNominal().equals("~")) {
                                    tv_promo_title.setText("Gunakan Promo - " + listData.get(0).getNamapromo());
                                    tv_promo.setText("-" + Utilities.getCurrency(listData.get(0).getOngkir()));
                                }else {
                                    tv_promo_title.setText("Gunakan Promo - " + listData.get(0).getNamapromo());
                                    tv_promo.setText("-" + Utilities.getCurrency(listData.get(0).getNominal()));
                                    int lastongkir = Integer.valueOf(listData.get(0).getOngkir()) - Integer.valueOf(listData.get(0).getNominal());
                                    if (lastongkir < 0){
                                        lastongkir = 0;
                                    }
                                    totalbayar = totalbayar + lastongkir;
                                }
                            }else {
                                tv_promo_title.setText("Gunakan Promo - " + listData.get(0).getNamapromo());
                                tv_promo.setText("-" + Utilities.getCurrency(listData.get(0).getNominal()));
                                totalbayar = totalbayar + Integer.parseInt(listData.get(0).getOngkir());
                                totalbayar = totalbayar - Integer.parseInt(listData.get(0).getNominal());
                            }
                        }else {
                            totalbayar = totalbayar + Integer.parseInt(listData.get(0).getOngkir());
                            ll_promo.setVisibility(View.GONE);
                        }

                        if (!listData.get(0).getPoinkeluar().equals("0")){
                            tv_poin_title.setText("Tukarkan "+listData.get(0).getPoinkeluar()+" Poin Yummy");
                            tv_poin_uses.setText("-"+ Utilities.getCurrency(listData.get(0).getPoinkeluar()));
                            totalbayar = totalbayar - Integer.parseInt(listData.get(0).getPoinkeluar());
                        }else {
                            ll_poin_used.setVisibility(View.GONE);
                        }

                        if (listData.get(0).getJenisbayar().equals("1")){
                            tv_metode_bayar.setText("Tunai");
                        }else if (listData.get(0).getJenisbayar().equals("2")){
                            tv_metode_bayar.setText("Transfer");
                        }

                        tv_poin_get.setText(listData.get(0).getPoinmasuk()+" Poin");

                        if (totalbayar<0) {
                            totalbayar = 0;
                            tv_total_bayar.setText(Utilities.getCurrency("0"));
                        }else {
                            tv_total_bayar.setText(Utilities.getCurrency(String.valueOf(totalbayar)));
                        }

                        if (listData.get(0).getStatustransaksi().equals("0")){
                            tv_status.setText("Dipesan");
                            btn_tracking.setVisibility(View.GONE);
                            if (listData.get(0).getJenisbayar().equals("1")) {
                                tv_status_description.setText("Silahkan lakukan pembayaran setelah Anda menerima barang dengan baik");
                            }else {
                                if (!listData.get(0).getNorekening().equals("")){
                                    namabank = listData.get(0).getNamabank();
                                    norekening = listData.get(0).getNorekening();
                                    namarekening = listData.get(0).getNamarekening();
                                    jumlahtransfer = listData.get(0).getJumlahtransfer();
                                    gambartransfer = listData.get(0).getGambartransfer();
                                    tanggaltransfer = listData.get(0).getTanggaltransfer();
                                    tv_status_description.setText("Pesanan Anda dalam proses validasi pembayaran");
                                    btn_transfer.setText("Lihat Bukti Bayar");
                                    btn_batal.setVisibility(View.GONE);
                                }else {
                                    tv_status_description.setText("Silahkan lakukan pembayaran sebelum tanggal " + listData.get(0).getTanggalpengantaran());
                                }
                            }
                            tv_status_date_order.setText("Waktu Pemesan : "+listData.get(0).getTanggaltransaksi());
                            tv_status_date_send.setText("Waktu Pengantaran : "+listData.get(0).getTanggalpengantaran());
                            if (listData.get(0).getJenisbayar().equals("1")){
                                rl_bayar.setVisibility(View.GONE);
                            }else {
                                rl_bayar.setVisibility(View.VISIBLE);
                            }
                        }else if (listData.get(0).getStatustransaksi().equals("1")){
                            tv_status.setText("Disiapkan");
                            tv_status_description.setText("Pesanan Anda sedang disiapkan oleh Fresh Yummy");
                            tv_status_date_order.setText("Waktu Pemesan : "+listData.get(0).getTanggaltransaksi());
                            tv_status_date_send.setText("Waktu Pengantaran : "+listData.get(0).getTanggalpengantaran());
                            btn_batal.setVisibility(View.GONE);
                            rl_bayar.setVisibility(View.GONE);
                            btn_tracking.setVisibility(View.GONE);
                        }else if (listData.get(0).getStatustransaksi().equals("2")){
                            tv_status.setText("Dikirim");
                            tv_status_description.setText("Pesanan Anda dalam pengiriman oleh driver Fresh Yummy");
                            tv_status_date_order.setText("Waktu Pemesan : "+listData.get(0).getTanggaltransaksi());
                            tv_status_date_send.setText("Waktu Pengantaran : "+listData.get(0).getTanggalpengantaran());
                            btn_batal.setVisibility(View.GONE);
                            rl_bayar.setVisibility(View.GONE);
                        }else if (listData.get(0).getStatustransaksi().equals("3")){
                            tv_status.setText("Selesai");
                            tv_status_description.setText("Pesanan Anda telah diterima oleh "+listData.get(0).getNamapenerima());
                            tv_status_date_order.setText("Waktu Pengantaran : "+listData.get(0).getTanggalpengantaran());
                            tv_status_date_send.setText("Waktu Pesanan Selesai : "+listData.get(0).getTanggalpenerimaan());
                            btn_batal.setVisibility(View.GONE);
                            rl_bayar.setVisibility(View.GONE);
                            btn_tracking.setVisibility(View.GONE);
                        }else if (listData.get(0).getStatustransaksi().equals("4")){
                            tv_status.setText("Dibatalkan");
                            tv_status_description.setText("Pesanan Anda telah dibatalkan");
                            tv_status_date_order.setText("Waktu Pemesan : "+listData.get(0).getTanggalpengantaran());
                            tv_status_date_send.setText("Waktu Pembatalan : "+listData.get(0).getTanggalpenerimaan());
                            btn_batal.setVisibility(View.GONE);
                            rl_bayar.setVisibility(View.GONE);
                            btn_tracking.setVisibility(View.GONE);
                        }
                    } else {
                        rl.setVisibility(View.VISIBLE);
                        Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
                    rl.setVisibility(View.VISIBLE);
                    Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Belanjaan>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                rl.setVisibility(View.VISIBLE);
                System.out.println("Retrofit Error:" + t.getMessage());
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    public void canceltransaksi() {
        final ProgressDialog dialog = new ProgressDialog(DetailBelanjaanActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        String random = Utilities.getRandom(5);

//        Log.e("data sent", random + idtransaksi+idproduk+jumlahbeli+hargabeli);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final APIServices apiService = retrofit.create(APIServices.class);
        Call<SetValue> call = apiService.canceltransaksi(random, idtransaksi);
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        MainActivity.flag_belanjaan = true;
                        Snackbar.make(findViewById(android.R.id.content), "Transaksi berhasil dibatalkan",
                                Snackbar.LENGTH_LONG).show();
                        gettransactiondetail();
                    }else {
//                        dialog.dismiss();
                        Log.e("status", response.body().getMessage());
                        Snackbar.make(findViewById(android.R.id.content), "Gagal membatalkan transakasi. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
//                    dialog.dismiss();
                    Log.e("status", response.body().getMessage());
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
