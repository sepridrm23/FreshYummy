package com.freshyummy.android;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.TedPermissionResult;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import picker.ugurtekbas.com.Picker.Picker;
import picker.ugurtekbas.com.Picker.TimeChangedListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

/**
 * Created by afi on 21/03/18.
 */

public class FragmentCheckout extends Fragment {
    RecyclerView rvProduct;
    EditText et_address_note, et_product_note, et_kode_promo;
    TextView tv_delivery_address, tv_poin, tv_get_poin, tv_subtotal, tv_ongkir, tv_total_bayar, tv_total_bayar_discount,
            tv_tanggal, tv_jam, tv_promo_title, tv_promo_nominal, tv_change_address;
    List<Cart> carts = new ArrayList<>();
    LatLng currentLatLng;
    FusedLocationProviderClient mFusedLocationClient;
    RecyclerView rvBank;
    RadioButton rbCOD, rbTransfer;
    String jenisBayar = "1", date_now;
    User users;
    boolean flag_promo;
    RelativeLayout rl_promo;
    LinearLayout ll_buy;
    ImageView back;
    String alamatpengantaran="", ongkir, poinkeluar="0", tanggal="", jam="", idpromo="", nama="", notelpon="";
    int subtotal=0, total_bayar=0, poinmasuk=0, counter=0, temp_total_bayar=0, nominal_promo=0, PLACE_PICKER_REQUEST_POSITION=99;

    public static FragmentCheckout newInstance() {
        return new FragmentCheckout();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        rvProduct = v.findViewById(R.id.rv_order_detail_product);
        tv_delivery_address = v.findViewById(R.id.tv_delivery_address_full_address);
        LinearLayout llDate = v.findViewById(R.id.ll_purchase_confirmation_order_date);
        LinearLayout llTime = v.findViewById(R.id.ll_purchase_confirmation_order_time);
        final Switch sw_poin = v.findViewById(R.id.sw_poin);
        rvBank = v.findViewById(R.id.rv_transfer_detail);
        rbTransfer = v.findViewById(R.id.rb_purchase_confirmation_payment_type_transfer);
        rbCOD = v.findViewById(R.id.rb_purchase_confirmation_payment_type_cod);
        tv_poin = v.findViewById(R.id.tv_poin);
        tv_get_poin = v.findViewById(R.id.tv_poin_get);
        tv_ongkir = v.findViewById(R.id.tv_order_detail_payment_amount_delivery);
        tv_subtotal = v.findViewById(R.id.tv_order_detail_payment_amount_subtotal);
        tv_total_bayar = v.findViewById(R.id.tv_order_detail_payment_amount);
        tv_total_bayar_discount = v.findViewById(R.id.tv_order_detail_payment_amount_discount);
        et_address_note = v.findViewById(R.id.et_address_note);
        et_product_note = v.findViewById(R.id.et_product_note);
        ll_buy = v.findViewById(R.id.ll_add_cart);
        tv_tanggal = v.findViewById(R.id.tv_purchase_confirmation_order_date);
        tv_jam = v.findViewById(R.id.tv_purchase_confirmation_order_time);
        et_kode_promo = v.findViewById(R.id.et_kode_promo);
        tv_promo_nominal = v.findViewById(R.id.tv_promo_nominal);
        tv_promo_title = v.findViewById(R.id.tv_promo_title);
        rl_promo = v.findViewById(R.id.rl_promo);
        tv_change_address = v.findViewById(R.id.tv_change_address);
        back = v.findViewById(R.id.back);

        rl_promo.setVisibility(View.GONE);

        date_now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        for (int a=0; a<MainActivity.carts.size(); a++){
            if (MainActivity.carts.get(a).getStatusketersediaan().equals("1")) {
                carts.add(new Cart(MainActivity.carts.get(a).getIdkeranjang(), MainActivity.carts.get(a).getIddetailproduk(), MainActivity.carts.get(a).getNamaproduk(), MainActivity.carts.get(a).getGambar(), MainActivity.carts.get(a).getSatuan(), MainActivity.carts.get(a).getHarga(), MainActivity.carts.get(a).getJumlahbeli(), MainActivity.carts.get(a).getDiscount(), MainActivity.carts.get(a).getStatusketersediaan()));
                subtotal = subtotal + (Integer.parseInt(MainActivity.carts.get(a).getHarga()) * Integer.parseInt(MainActivity.carts.get(a).getJumlahbeli()));
            }
        }

        users = Utilities.getUser(getActivity());
        tv_subtotal.setText(Utilities.getCurrency(String.valueOf(subtotal)));
        tv_poin.setText(users.getPoin());
        sw_poin.setText("-"+users.getPoin());

        rvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProduct.setHasFixedSize(true);
        CheckoutViewAdapter rcAdapter = new CheckoutViewAdapter(getActivity(), carts);
        rvProduct.setAdapter(rcAdapter);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            getLastPosition();
        }

        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker();
            }
        });

        rbCOD.setChecked(true);
        rbTransfer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rvBank.setVisibility(View.VISIBLE);
                    jenisBayar = "2";
                } else {
                    rvBank.setVisibility(View.GONE);
                    jenisBayar = "1";
                }
            }
        });

        sw_poin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!tv_poin.getText().equals("0")) {
                        poinkeluar = tv_poin.getText().toString();
                        int new_total_bayar=0;
                        if (temp_total_bayar != total_bayar) {
                            new_total_bayar = temp_total_bayar - Integer.parseInt(poinkeluar);
                        }else {
                            new_total_bayar = total_bayar - Integer.parseInt(poinkeluar);
                        }
                        tv_total_bayar_discount.setText(Utilities.getCurrency(String.valueOf(total_bayar)));
                        Utilities.strikeThroughText(tv_total_bayar_discount);
                        if (new_total_bayar < 0){
                            tv_total_bayar.setText("  "+Utilities.getCurrency("0"));
                        }else {
                            tv_total_bayar.setText("  "+Utilities.getCurrency(String.valueOf(new_total_bayar)));
                        }
                        temp_total_bayar = new_total_bayar;
                        poinmasuk = 0;
                        for (int a = 0; a < new_total_bayar; a = a + 10000) {
                            poinmasuk = poinmasuk + 100;
                        }
                        tv_get_poin.setText(String.valueOf(poinmasuk));
                    }else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Silahkan berbelanja untuk mendapatkan poin tambahan",
                                Snackbar.LENGTH_LONG).show();
                        sw_poin.setChecked(false);
                    }
                } else {
                    poinkeluar = tv_poin.getText().toString();
                    temp_total_bayar = temp_total_bayar + Integer.valueOf(poinkeluar);
                    if (temp_total_bayar!=total_bayar){
                        tv_total_bayar_discount.setText(Utilities.getCurrency(String.valueOf(total_bayar)));
                        Utilities.strikeThroughText(tv_total_bayar_discount);
                        if (temp_total_bayar < 0){
                            tv_total_bayar.setText("  "+Utilities.getCurrency("0"));
                        }else {
                            tv_total_bayar.setText("  "+Utilities.getCurrency(String.valueOf(temp_total_bayar)));
                        }
                        poinkeluar = "0";
                        poinmasuk = 0;
                        for (int a=0; a<temp_total_bayar; a=a+10000){
                            poinmasuk = poinmasuk + 100;
                        }
                    }else {
                        tv_total_bayar_discount.setText("");
                        tv_total_bayar.setText("  "+Utilities.getCurrency(String.valueOf(total_bayar)));
                        poinkeluar = "0";
                        poinmasuk = 0;
                        for (int a=0; a<total_bayar; a=a+10000){
                            poinmasuk = poinmasuk + 100;
                        }
                    }
                    tv_get_poin.setText(String.valueOf(poinmasuk));
                }
            }
        });

        ll_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address_note="", product_note = "", tanggalpengiriman="";
                if (!et_product_note.getText().toString().isEmpty()){
                    product_note = et_product_note.getText().toString();
                }
                if (!et_address_note.getText().toString().isEmpty()){
                    address_note = et_address_note.getText().toString();
                }

                if (tanggal.equals("")){
                    datePicker();
                }else if (jam.equals("")){
                    timePicker();
                }else if (alamatpengantaran.equals("") || notelpon.equals("") || nama.equals("")){
                    changeAddress();
                } else{
                    tanggalpengiriman = tanggal + " " + jam;
                    SetUserTransaction(address_note, product_note, tanggalpengiriman);
                }
            }
        });

        et_kode_promo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 < i1) {
                    if (flag_promo) {
                        flag_promo = false;
                        temp_total_bayar = temp_total_bayar + nominal_promo;
                        nominal_promo = 0;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString();
                if(!s.equals(s.toUpperCase())) {
                    s=s.toUpperCase();
                    et_kode_promo.setText(s);
                }
                et_kode_promo.setSelection(s.length());
                if (!s.equals("")) {
                    for (int a = 0; a < MainActivity.promos.size(); a++) {
                        if (s.equals(MainActivity.promos.get(a).getNamapromo())) {
//                            Log.e("nama", s+" "+MainActivity.promos.get(a).getNamapromo());
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            Date date1 = null, date2 = null;
                            try {
                                date1 = format.parse(date_now);
                                date2 = format.parse(MainActivity.promos.get(a).getTanggalberakhir());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (date1.after(date2)){
                                if (!flag_promo) {
                                    idpromo = "";
                                    rl_promo.setVisibility(View.GONE);
                                    et_kode_promo.setError("Kode promo telah berakhir");
                                    if (temp_total_bayar != total_bayar) {
                                        tv_total_bayar_discount.setText(Utilities.getCurrency(String.valueOf(total_bayar)));
                                        Utilities.strikeThroughText(tv_total_bayar_discount);
                                        if (temp_total_bayar < 0) {
                                            tv_total_bayar.setText("  " + Utilities.getCurrency("0"));
                                        } else {
                                            tv_total_bayar.setText("  " + Utilities.getCurrency(String.valueOf(temp_total_bayar)));
                                        }
                                        poinmasuk = 0;
                                        for (int x = 0; x < temp_total_bayar; x = x + 10000) {
                                            poinmasuk = poinmasuk + 100;
                                        }
                                    } else {
                                        tv_total_bayar_discount.setText("");
                                        tv_total_bayar.setText("  " + Utilities.getCurrency(String.valueOf(total_bayar)));
                                        poinmasuk = 0;
                                        for (int x = 0; x < total_bayar; x = x + 10000) {
                                            poinmasuk = poinmasuk + 100;
                                        }
                                    }
                                    tv_get_poin.setText(String.valueOf(poinmasuk));
                                }
                            }else {
                                if (MainActivity.promos.get(a).getUsed().equals("0")) {
                                    flag_promo = true;
                                    et_kode_promo.setError(null);
                                    rl_promo.setVisibility(View.VISIBLE);
                                    idpromo = MainActivity.promos.get(a).getIdpromo();
                                    tv_promo_title.setText("Gunakan Promo - " + s);

                                    if (MainActivity.promos.get(a).getJenispromo().equals("1")) {
//                                    Log.e("boo", "ongkir");
                                        int new_total_bayar = 0;
                                        if (MainActivity.promos.get(a).getNominal().equals("~")) {
//                                        Log.e("boo", "ongir ~");
                                            tv_promo_nominal.setText("-" + Utilities.getCurrency(ongkir));
                                            if (nominal_promo == 0) {
                                                if (temp_total_bayar != total_bayar) {
                                                    new_total_bayar = temp_total_bayar - Integer.parseInt(ongkir);
                                                } else {
                                                    new_total_bayar = total_bayar - Integer.parseInt(ongkir);
                                                }
                                            } else {
                                                new_total_bayar = temp_total_bayar;
                                            }
                                            nominal_promo = Integer.parseInt(ongkir);
                                        } else {
//                                        Log.e("boo", "ongkir other");
                                            tv_promo_nominal.setText("-" + Utilities.getCurrency(MainActivity.promos.get(a).getNominal()));
                                            if (nominal_promo == 0) {
                                                new_total_bayar = Integer.parseInt(ongkir) - Integer.parseInt(MainActivity.promos.get(a).getNominal());

                                                if (new_total_bayar < 0) {
                                                    new_total_bayar = 0;
                                                    nominal_promo = Integer.parseInt(ongkir);
                                                } else {
                                                    nominal_promo = Integer.parseInt(MainActivity.promos.get(a).getNominal());
                                                }

                                                if (temp_total_bayar != total_bayar) {
                                                    new_total_bayar = (temp_total_bayar - Integer.parseInt(ongkir)) + new_total_bayar;
                                                } else {
                                                    new_total_bayar = (total_bayar - Integer.parseInt(ongkir)) + new_total_bayar;
                                                }
                                            } else {
                                                new_total_bayar = temp_total_bayar;
                                            }
                                        }
                                        tv_total_bayar_discount.setText(Utilities.getCurrency(String.valueOf(total_bayar)));
                                        Utilities.strikeThroughText(tv_total_bayar_discount);
                                        if (new_total_bayar < 0) {
                                            tv_total_bayar.setText("  " + Utilities.getCurrency("0"));
                                        } else {
                                            tv_total_bayar.setText("  " + Utilities.getCurrency(String.valueOf(new_total_bayar)));
                                        }
                                        temp_total_bayar = new_total_bayar;
                                        poinmasuk = 0;
                                        for (int x = 0; x < new_total_bayar; x = x + 10000) {
                                            poinmasuk = poinmasuk + 100;
                                        }
                                        tv_get_poin.setText(String.valueOf(poinmasuk));
                                    } else {
//                                    Log.e("boo", "produk");
                                        tv_promo_nominal.setText("-" + Utilities.getCurrency(MainActivity.promos.get(a).getNominal()));
                                        int new_total_bayar = 0;

                                        if (nominal_promo == 0) {
                                            if (temp_total_bayar != total_bayar) {
//                                            Log.e("boo", "!=");
                                                new_total_bayar = temp_total_bayar - Integer.parseInt(MainActivity.promos.get(a).getNominal());
                                            } else {
//                                            Log.e("boo", "=");
                                                new_total_bayar = total_bayar - Integer.parseInt(MainActivity.promos.get(a).getNominal());
                                            }
                                        } else {
                                            new_total_bayar = temp_total_bayar;
                                        }
                                        nominal_promo = Integer.parseInt(MainActivity.promos.get(a).getNominal());

                                        tv_total_bayar_discount.setText(Utilities.getCurrency(String.valueOf(total_bayar)));
                                        Utilities.strikeThroughText(tv_total_bayar_discount);
                                        if (new_total_bayar < 0) {
                                            tv_total_bayar.setText("  " + Utilities.getCurrency("0"));
                                        } else {
                                            tv_total_bayar.setText("  " + Utilities.getCurrency(String.valueOf(new_total_bayar)));
                                        }
                                        temp_total_bayar = new_total_bayar;
                                        poinmasuk = 0;
                                        for (int x = 0; x < new_total_bayar; x = x + 10000) {
                                            poinmasuk = poinmasuk + 100;
                                        }
                                        tv_get_poin.setText(String.valueOf(poinmasuk));
                                    }
                                } else {
                                    if (!flag_promo) {
                                        idpromo = "";
                                        rl_promo.setVisibility(View.GONE);
                                        et_kode_promo.setError("Anda sudah menggunakan kode promo ini");
                                        if (temp_total_bayar != total_bayar) {
                                            tv_total_bayar_discount.setText(Utilities.getCurrency(String.valueOf(total_bayar)));
                                            Utilities.strikeThroughText(tv_total_bayar_discount);
                                            if (temp_total_bayar < 0) {
                                                tv_total_bayar.setText("  " + Utilities.getCurrency("0"));
                                            } else {
                                                tv_total_bayar.setText("  " + Utilities.getCurrency(String.valueOf(temp_total_bayar)));
                                            }
                                            poinmasuk = 0;
                                            for (int x = 0; x < temp_total_bayar; x = x + 10000) {
                                                poinmasuk = poinmasuk + 100;
                                            }
                                        } else {
                                            tv_total_bayar_discount.setText("");
                                            tv_total_bayar.setText("  " + Utilities.getCurrency(String.valueOf(total_bayar)));
                                            poinmasuk = 0;
                                            for (int x = 0; x < total_bayar; x = x + 10000) {
                                                poinmasuk = poinmasuk + 100;
                                            }
                                        }
                                        tv_get_poin.setText(String.valueOf(poinmasuk));
                                    }
                                }
                            }
                            a = MainActivity.promos.size();
                        } else {
                            if (!flag_promo) {
                                idpromo = "";
                                rl_promo.setVisibility(View.GONE);
                                et_kode_promo.setError("Kode promo tidak tersedia");
                                if (temp_total_bayar!=total_bayar){
//                                    Log.e("boo", "!=");
                                    tv_total_bayar_discount.setText(Utilities.getCurrency(String.valueOf(total_bayar)));
                                    Utilities.strikeThroughText(tv_total_bayar_discount);
                                    if (temp_total_bayar < 0){
                                        tv_total_bayar.setText("  "+Utilities.getCurrency("0"));
                                    }else {
                                        tv_total_bayar.setText("  "+Utilities.getCurrency(String.valueOf(temp_total_bayar)));
                                    }
                                    poinmasuk = 0;
                                    for (int x = 0; x < temp_total_bayar; x = x + 10000) {
                                        poinmasuk = poinmasuk + 100;
                                    }
                                }else {
//                                    Log.e("boo", "=");
                                    tv_total_bayar_discount.setText("");
                                    tv_total_bayar.setText("  "+Utilities.getCurrency(String.valueOf(total_bayar)));
                                    poinmasuk = 0;
                                    for (int x = 0; x < total_bayar; x = x + 10000) {
                                        poinmasuk = poinmasuk + 100;
                                    }
                                }
                                tv_get_poin.setText(String.valueOf(poinmasuk));
                            }
                        }
                    }
                }else {
                    idpromo = "";
                    et_kode_promo.setError(null);
                    if (temp_total_bayar!=total_bayar){
                        tv_total_bayar_discount.setText(Utilities.getCurrency(String.valueOf(total_bayar)));
                        Utilities.strikeThroughText(tv_total_bayar_discount);
                        if (temp_total_bayar < 0){
                            tv_total_bayar.setText("  "+Utilities.getCurrency("0"));
                        }else {
                            tv_total_bayar.setText("  "+Utilities.getCurrency(String.valueOf(temp_total_bayar)));
                        }
                        poinmasuk = 0;
                        for (int x = 0; x < temp_total_bayar; x = x + 10000) {
                            poinmasuk = poinmasuk + 100;
                        }
                    }else {
                        tv_total_bayar_discount.setText("");
                        tv_total_bayar.setText("  "+Utilities.getCurrency(String.valueOf(total_bayar)));
                        poinmasuk = 0;
                        for (int x = 0; x < total_bayar; x = x + 10000) {
                            poinmasuk = poinmasuk + 100;
                        }
                    }
                    tv_get_poin.setText(String.valueOf(poinmasuk));
                }
            }
        });

        getPromo();

        if (MainActivity.banks.size() != 0){
            rvBank.setLayoutManager(new LinearLayoutManager(getContext()));
            rvBank.setHasFixedSize(true);
            BankViewAdapter rcAdapterBank = new BankViewAdapter(getActivity(), MainActivity.banks);
            rvBank.setAdapter(rcAdapterBank);
        }else {
            getBank();
        }

        tv_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddress();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_PICKER_REQUEST_POSITION) {
                final Place place = PlacePicker.getPlace(data, getActivity());

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_confirm_address);

                final EditText et_nama = dialog.findViewById(R.id.et_nama);
                final EditText et_no_telp = dialog.findViewById(R.id.et_no_telp);
                final EditText et_alamat = dialog.findViewById(R.id.et_alamat);
                Button btnConfirm = dialog.findViewById(R.id.btn_save);

                et_nama.setText(users.getNama());
                et_no_telp.setText(users.getTelepon());
                et_alamat.setText(place.getAddress());

                ImageView ib_edit_name = dialog.findViewById(R.id.ib_close_name);
                ImageView ib_edit_phone = dialog.findViewById(R.id.ib_close_phone);
                ImageView ib_edit_address = dialog.findViewById(R.id.ib_close_email);

                ib_edit_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_alamat.setEnabled(true);
                    }
                });

                ib_edit_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_nama.setEnabled(true);
                    }
                });

                ib_edit_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_no_telp.setEnabled(true);
                    }
                });

                et_no_telp.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (i == 3) {
                            if (charSequence.toString().equals("+620")) {
                                et_no_telp.setText("+62");
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() < 3){
                            et_no_telp.setError("Nomor telepon harus diisi");
                            et_no_telp.setText("+62");
                            et_no_telp.post(new Runnable() {
                                @Override
                                public void run() {
                                    et_no_telp.setSelection(et_no_telp.getText().toString().length());
                                }
                            });
                        }
                    }
                });

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_nama.getText().toString().trim().isEmpty()){
                            et_nama.setError("Nama penerima harus diisi");
                        }else if (et_no_telp.getText().toString().trim().equals("+62")){
                            et_no_telp.setError("Nomor telepon penerima harus diisi");
                        }else if (et_alamat.getText().toString().trim().isEmpty()){
                            et_alamat.setError("Alamat penerima harus diisi");
                        }else {
                            dialog.dismiss();
                            nama = et_nama.getText().toString().trim();
                            notelpon = et_no_telp.getText().toString().trim();
                            alamatpengantaran = et_alamat.getText().toString().trim();
                            currentLatLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
//                        tv_delivery_address.setText(nama+"\n"+notelpon+"\n"+alamatpengantaran);
                            String alamat = nama + "\n" + notelpon + "\n" + alamatpengantaran;
                            getOngkir(String.valueOf(currentLatLng.latitude), String.valueOf(currentLatLng.longitude), alamat);
                        }
                    }
                });

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.setCancelable(false);
                dialog.show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.e("reqcode", ""+requestCode);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                    }
                    else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                    }
                }else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    getLastPosition();
                }else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                }
                return;
            }
        }
    }

    void changeAddress(){
        TedRx2Permission.with(getActivity())
            .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .request()
            .subscribe(new Observer<TedPermissionResult>() {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                }

                @Override
                public void onNext(@io.reactivex.annotations.NonNull TedPermissionResult tedPermissionResult) {
                    if (tedPermissionResult.isGranted()) {
                        final PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                        try {
                            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_POSITION);
                        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                                "Please enable permission from settings",
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction("SETTING", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    void getLastPosition(){
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("permission", "DENIED");
            return;
        } else {
            Log.e("permission", "GRANTED");
        }

        mFusedLocationClient.getLastLocation()
            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        getOngkir(String.valueOf(currentLatLng.latitude), String.valueOf(currentLatLng.longitude), "");
                    }else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Silahkan hidupkan GPS Anda",
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            });
    }

    private void getOngkir(String latitude, String longitude, final String alamat) {
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
        Call<SetValue> call = api.getongkir(random, latitude, longitude);
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        ongkir = response.body().getMessage();
                        tv_ongkir.setText(Utilities.getCurrency(ongkir));
                        total_bayar = subtotal + Integer.valueOf(ongkir);
                        tv_total_bayar.setText("  "+Utilities.getCurrency(String.valueOf(total_bayar)));
                        temp_total_bayar = total_bayar;
                        poinmasuk = 0;
                        for (int a=0; a<total_bayar; a=a+10000){
                            poinmasuk = poinmasuk + 100;
                        }
                        tv_get_poin.setText(String.valueOf(poinmasuk));

                        if (alamat.equals("")) {
                            try {
                                addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
                                alamatpengantaran = addresses.get(0).getAddressLine(0);
                                nama = users.getNama();
                                notelpon = users.getTelepon();
                                tv_delivery_address.setText(nama + "\n" + notelpon + "\n" + alamatpengantaran);
                            } catch (IOException e) {
                                e.printStackTrace();
                                tv_delivery_address.setText("Silahkan masukan alamat pengantaran...");
                            }
                        }else {
                            tv_delivery_address.setText(alamat);
                        }
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
            public void onFailure(@NonNull Call<SetValue> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    private void datePicker(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_date);
        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        TextView ok = dialog.findViewById(R.id.ok);
        TextView cancle = dialog.findViewById(R.id.cancle);
        TextView tv_title = dialog.findViewById(R.id.tv_title);

        tv_title.setText("Masukan Tanggal Pengantaran");

        final Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
//        int m = calendar.get(Calendar.MINUTE);
//        Log.e("jam", ""+h+" "+m);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);

//        datePicker.updateDate(year, month, day);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bulan = datePicker.getMonth()+1;
                tv_tanggal.setText(datePicker.getDayOfMonth() + "-" + bulan + "-" + datePicker.getYear());
                tanggal = datePicker.getYear() + "-" + bulan + "-" + datePicker.getDayOfMonth();
                dialog.dismiss();
            }
        });

        if (h > 17){
//            Snackbar.make(dialog.findViewById(android.R.id.content), "Pengantaran untuk lusa", Snackbar.LENGTH_LONG).show();
            calendar.add(Calendar.DATE, 2);
            datePicker.setMinDate(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, 1);
            datePicker.setMaxDate(calendar.getTimeInMillis());
        }else {
            calendar.add(Calendar.DATE, 1);
            datePicker.setMinDate(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, 1);
            datePicker.setMaxDate(calendar.getTimeInMillis());
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.show();

//        final Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
//                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                tv_tanggal.setText(i2 + "-" + (i1+1) + "-" + i);
//                tanggal = i + "-" + (i1+1) + "-" + i2;
//            }
//        }, year, month, day);
//
//        calendar.add(Calendar.DATE, 1);
//        datepickerdialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//        calendar.add(Calendar.DATE, 1);
//        datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//        datepickerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        datepickerdialog.setTitle(null);
//        datepickerdialog.setMessage(null);
//        datepickerdialog.show();
    }

    private void timePicker(){
        final int[] time = new int[2];
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_time);
        final Picker picker = (Picker)dialog.findViewById(R.id.amPicker);
        picker.setTime(9,0);
        time[0] = picker.getTime().getHours();
        time[1] = picker.getTime().getMinutes();
        TextView ok = (TextView)dialog.findViewById(R.id.ok);
        TextView cancle = (TextView)dialog.findViewById(R.id.cancle);
        picker.setTimeChangedListener(new TimeChangedListener() {
            @Override
            public void timeChanged(Date date) {
                time[0] = date.getHours();
                time[1] = date.getMinutes();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time[0] > 16 || time[0] < 8){
                    Snackbar.make(dialog.findViewById(android.R.id.content), "Silahkan memilih jam pengantaran antara pukul 08:00 sampai 17:00",
                            Snackbar.LENGTH_LONG).show();
                }else {
                    tv_jam.setText(time[0]+":"+time[1]);
                    jam = time[0]+":"+time[1]+":00";
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.show();

    }

    private void getBank() {
//        final ProgressDialog pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(false);
//        pDialog.show();

        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        APIServices api = retrofit.create(APIServices.class);
        Call<GetValue<Bank>> call = api.getbank(random);
        call.enqueue(new Callback<GetValue<Bank>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Bank>> call, @NonNull Response<GetValue<Bank>> response) {
//                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Bank> listDataProduct = response.body().getData();
                        MainActivity.banks = listDataProduct;
                        rvBank.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvBank.setHasFixedSize(true);
                        BankViewAdapter rcAdapter = new BankViewAdapter(getActivity(), listDataProduct);
                        rvBank.setAdapter(rcAdapter);
                    } else {
//                        pDialog.dismiss();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
//                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Bank>> call, @NonNull Throwable t) {
//                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    private void getPromo() {
//        final ProgressDialog pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(false);
//        pDialog.show();

        String random = Utilities.getRandom(5);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        APIServices api = retrofit.create(APIServices.class);
        Call<GetValue<Promo>> call = api.getpromo(random, users.getIdpelanggan());
        call.enqueue(new Callback<GetValue<Promo>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Promo>> call, @NonNull Response<GetValue<Promo>> response) {
//                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        MainActivity.promos = response.body().getData();
                    } else {
//                        pDialog.dismiss();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
//                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Promo>> call, @NonNull Throwable t) {
//                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    public void SetUserTransaction(String addressnote, String productnote, String tanggalpengantaran) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

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
        Call<SetValue> call = apiService.setusertransaction(random, users.getIdpelanggan(), nama, alamatpengantaran, String.valueOf(currentLatLng.latitude),
                String.valueOf(currentLatLng.longitude), notelpon, addressnote, productnote, jenisBayar,
                tanggalpengantaran, ongkir, poinkeluar, String.valueOf(poinmasuk), idpromo);
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
//                        Snackbar.make(getActivity().findViewById(android.R.id.content), response.body().getMessage(),
//                                Snackbar.LENGTH_LONG).show();
                        counter = 0;
                        for (int a=0; a<carts.size(); a++){
                            SetUserDetailTransaction(carts.size(), dialog, response.body().getMessage(), carts.get(a).getIddetailproduk(), carts.get(a).getJumlahbeli(), carts.get(a).getHarga());
                        }

//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//
//                            }
//                        }, 2000);
                    }else {
                        dialog.dismiss();
                        Log.e("status", response.body().getMessage());
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal membuat transakasi. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    dialog.dismiss();
                    Log.e("status", response.body().getMessage());
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke server",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
                Log.e("status", "transaksi");
                dialog.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke server",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void SetUserDetailTransaction(final int count, final ProgressDialog dialog, final String idtransaksi,
                                         String idproduk, String jumlahbeli, String hargabeli) {
//        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("Loading...");
//        dialog.setCancelable(false);
//        dialog.show();

        String random = Utilities.getRandom(5);

//        Log.e("data sent", random + idtransaksi+idproduk+jumlahbeli+hargabeli);

        OkHttpClient okHttpClient = Utilities.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLPhp())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final APIServices apiService = retrofit.create(APIServices.class);
        Call<SetValue> call = apiService.setusertransactiondetail(random, idtransaksi, idproduk, jumlahbeli, hargabeli);
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        counter++;
                        if (count == counter) {
                            dialog.dismiss();
//                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Transaksi berhasil",
//                                    Snackbar.LENGTH_SHORT).show();
//                            new Timer().schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                getActivity().runOnUiThread(new Runnable() {
//                                    public void run() {
                                        MainActivity.setCurrenBNV(1);
                                        DataHelper dbHelper = new DataHelper(getContext());
                                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                                        for (int a=0; a< carts.size(); a++){
                                            dbHelper.onDelete(db, carts.get(a).getIddetailproduk());
                                        }
                                        MainActivity.flag_belanjaan = true;
                                        MainActivity.addBadgeBelanja(getContext());

                                        MainActivity.setCurrenBNV(2);

                                        Intent intent = new Intent(getContext(), DetailBelanjaanActivity.class);
                                        intent.putExtra("IDTRANSAKSI", idtransaksi);
                                        getActivity().startActivity(intent);
//                                    }
//                                });
//                                }
//                            }, 2300);

//                            new Timer().schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        public void run() {
//
//                                        }
//                                    });
//                                }
//                            }, 2000);

//                            MainActivity.setCurrenBNV(2);
                        }
                    }else {
                        dialog.dismiss();
                        Log.e("status", response.body().getMessage());
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Gagal membuat transakasi. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    dialog.dismiss();
                    Log.e("status", response.body().getMessage());
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke server",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SetValue> call, Throwable t) {
                Log.e("status", "detail transaksi");
                dialog.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak terhubung ke server",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
