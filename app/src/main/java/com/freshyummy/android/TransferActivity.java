package com.freshyummy.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
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
 * Created by afi on 20/04/18.
 */

public class TransferActivity extends AppCompatActivity {
    TextView tv_transfer_confirmation_nominal;
    Spinner sp_transfer_confirmation_bank_name;
    RecyclerView rv_tranfer_confirmation_bank;
    ImageView iv_transfer_confirmation_photo, back;
    EditText et_transfer_confirmation_name, et_transfer_confirmation_rek_number, et_transfer_confirmation_amount, et_transfer_confirmation_timestamp;
    String idtransaksi="", totalbayar="", strgambar="", tanggal="", namabank="", norekening="", namarekening="", jumlahtransfer="", gambartransfer="", tanggaltransfer="";
    Button button_upload_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        tv_transfer_confirmation_nominal = findViewById(R.id.tv_transfer_confirmation_nominal);
        sp_transfer_confirmation_bank_name = findViewById(R.id.sp_transfer_confirmation_bank_name);
        rv_tranfer_confirmation_bank = findViewById(R.id.rv_tranfer_confirmation_bank);
        iv_transfer_confirmation_photo = findViewById(R.id.iv_transfer_confirmation_photo);
        et_transfer_confirmation_amount = findViewById(R.id.et_transfer_confirmation_amount);
        et_transfer_confirmation_name = findViewById(R.id.et_transfer_confirmation_name);
        et_transfer_confirmation_rek_number = findViewById(R.id.et_transfer_confirmation_rek_number);
        et_transfer_confirmation_timestamp = findViewById(R.id.et_transfer_confirmation_timestamp);
        button_upload_image = findViewById(R.id.button_upload_image);
        back = findViewById(R.id.back);

        idtransaksi = getIntent().getStringExtra("IDTRANSAKSI");
        totalbayar = getIntent().getStringExtra("TOTAL");
        namabank = getIntent().getStringExtra("NAMABANK");
        norekening = getIntent().getStringExtra("NOREKENING");
        namarekening = getIntent().getStringExtra("NAMAREKENING");
        jumlahtransfer = getIntent().getStringExtra("JUMLAHTRANSFER");
        gambartransfer = getIntent().getStringExtra("GAMBARTRANSFER");
        tanggaltransfer = getIntent().getStringExtra("TANGGALTRANSFER");

        tv_transfer_confirmation_nominal.setText(Utilities.getCurrency(totalbayar));
        et_transfer_confirmation_amount.setText(Utilities.getCurrency(totalbayar));
        et_transfer_confirmation_amount.setEnabled(false);
        if (!norekening.equals("")) {
            et_transfer_confirmation_timestamp.setText(tanggaltransfer.substring(0, 10));
            et_transfer_confirmation_name.setText(namarekening);
            et_transfer_confirmation_rek_number.setText(norekening);
            et_transfer_confirmation_amount.setText(jumlahtransfer);
            for (int i = 0; i < sp_transfer_confirmation_bank_name.getAdapter().getCount(); i++) {
                if (sp_transfer_confirmation_bank_name.getAdapter().getItem(i).toString().contains(namabank)) {
                    sp_transfer_confirmation_bank_name.setSelection(i);
                }
            }
            Glide.with(this).load(Utilities.getURLImageTransfer() + gambartransfer).into(iv_transfer_confirmation_photo);
            button_upload_image.setText("Ubah");
        }

        iv_transfer_confirmation_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAmbilGambar();
            }
        });

        if (MainActivity.banks.size() != 0){
            rv_tranfer_confirmation_bank.setLayoutManager(new LinearLayoutManager(TransferActivity.this));
            rv_tranfer_confirmation_bank.setHasFixedSize(true);
            BankViewAdapter rcAdapterBank = new BankViewAdapter(TransferActivity.this, MainActivity.banks);
            rv_tranfer_confirmation_bank.setAdapter(rcAdapterBank);
        }else {
            getBank();
        }

        et_transfer_confirmation_timestamp.setInputType(InputType.TYPE_NULL);
        et_transfer_confirmation_timestamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
        et_transfer_confirmation_timestamp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker();
                }
            }
        });

        button_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strgambar.equals("")){
                    dialogAmbilGambar();
                }else if (tanggal.equals("")){
                    datePicker();
                }else if (et_transfer_confirmation_name.getText().toString().isEmpty() || et_transfer_confirmation_rek_number.getText().toString().isEmpty() ||
                        et_transfer_confirmation_amount.getText().toString().isEmpty() || et_transfer_confirmation_timestamp.getText().toString().isEmpty()){
                    Snackbar.make(findViewById(android.R.id.content), "Mohon penuhi data yang dibutuhkan",
                            Snackbar.LENGTH_LONG).show();
                }else {
                    transferconfirmation(idtransaksi, sp_transfer_confirmation_bank_name.getSelectedItem().toString(),
                            et_transfer_confirmation_name.getText().toString().trim(), et_transfer_confirmation_rek_number.getText().toString().trim(),
                            et_transfer_confirmation_amount.getText().toString().trim(), tanggal);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void datePicker() {
        final Dialog dialog = new Dialog(TransferActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_date);
        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        TextView ok = dialog.findViewById(R.id.ok);
        TextView cancle = dialog.findViewById(R.id.cancle);

        TextView tv_title = dialog.findViewById(R.id.tv_title);

        tv_title.setText("Masukan Tanggal Transfer");

//        final Calendar calendar = Calendar.getInstance();
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
                int bulan = datePicker.getMonth() + 1;
                et_transfer_confirmation_timestamp.setText(datePicker.getDayOfMonth() + "-" + bulan + "-" + datePicker.getYear());
                tanggal = datePicker.getYear() + "-" + bulan + "-" + datePicker.getDayOfMonth();
                dialog.dismiss();
            }
        });

//        calendar.add(Calendar.DATE, 1);
//        datePicker.setMinDate(calendar.getTimeInMillis());
//        calendar.add(Calendar.DATE, 1);
//        datePicker.setMaxDate(calendar.getTimeInMillis());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void transferconfirmation(String idtransaksi, String namabank, String namarekening, String norekening,
                                     String jumlahtransfer, String tanggal) {
        final ProgressDialog dialog = new ProgressDialog(TransferActivity.this);
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
        Call<SetValue> call = apiService.transferconfirmation(random, idtransaksi, strgambar, namabank, namarekening, norekening, tanggal, jumlahtransfer);
        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(@NonNull Call<SetValue> call, @NonNull Response<SetValue> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        Snackbar.make(findViewById(android.R.id.content), "Berhasil mengirim bukti bayar. Terimakasih",
                                Snackbar.LENGTH_LONG).show();
                        DetailBelanjaanActivity.flag = true;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        onBackPressed();
                                    }
                                });
                            }
                        }, 2300);

                    }else {
                        Log.e("status", response.body().getMessage());
                        Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim bukti bayar. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
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


    private void getBank() {
        final ProgressDialog pDialog = new ProgressDialog(TransferActivity.this);
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
        Call<GetValue<Bank>> call = api.getbank(random);
        call.enqueue(new Callback<GetValue<Bank>>() {
            @Override
            public void onResponse(@NonNull Call<GetValue<Bank>> call, @NonNull Response<GetValue<Bank>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        List<Bank> listDataProduct = response.body().getData();
                        MainActivity.banks = listDataProduct;
                        rv_tranfer_confirmation_bank.setLayoutManager(new LinearLayoutManager(TransferActivity.this));
                        rv_tranfer_confirmation_bank.setHasFixedSize(true);
                        BankViewAdapter rcAdapter = new BankViewAdapter(TransferActivity.this, listDataProduct);
                        rv_tranfer_confirmation_bank.setAdapter(rcAdapter);
                    } else {
//                        pDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                                Snackbar.LENGTH_LONG).show();
//                        Utilities.showAsToast(getContext(), "Gagal mengambil data. Silahkan coba lagi");
                    }
                } else {
//                    pDialog.dismiss();
                    System.out.println("Response:" + response.message());
                    Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data. Silahkan coba lagi",
                            Snackbar.LENGTH_LONG).show();
//                    Utilities.showAsToast(getContext(), "Tidak ada data terbaru");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetValue<Bank>> call, @NonNull Throwable t) {
                pDialog.dismiss();
                System.out.println("Retrofit Error:" + t.getMessage());
                Snackbar.make(findViewById(android.R.id.content), "Tidak terhubung ke Internet",
                        Snackbar.LENGTH_LONG).show();
//                Utilities.showAsToast(getActivity(), "Tidak terhubung ke Internet");
            }
        });
    }

    private void dialogAmbilGambar() {
        final CharSequence[] options = { "Camera", "Gallery" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TransferActivity.this);
        builder.setTitle("Ambil bukti transfer dari ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera"))
                {
                    if (ContextCompat.checkSelfPermission(TransferActivity.this,
                            android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(TransferActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(TransferActivity.this,
                                new String[]{android.Manifest.permission.CAMERA},
                                1);
                    }else {
                        if(Build.VERSION.SDK_INT>=24){
                            try{
                                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                m.invoke(null);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                    }
                }
                else if (options[item].equals("Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
            }
        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        Log.e("reqcode", ""+requestCode);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(TransferActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(TransferActivity.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }else {
                        if(Build.VERSION.SDK_INT>=24){
                            try{
                                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                m.invoke(null);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                    }
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(Build.VERSION.SDK_INT>=24){
                        try{
                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                            m.invoke(null);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap imageBitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    imageBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    int w = imageBitmap.getWidth();
//                    int h = imageBitmap.getHeight();
//                    w=w/2;h=h/2;
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 480, 480, true);
                    strgambar= Utilities.getArrayByteFromBitmap(imageBitmap);
                    iv_transfer_confirmation_photo.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri imageUri = data.getData();
                InputStream imageStream = null;
                Bitmap imageBitmap;
                try{
                    imageStream = getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    int w = imageBitmap.getWidth();
//                    int h = imageBitmap.getHeight();
//                    w=w/2;h=h/2;
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 480, 480, true);
                    strgambar= Utilities.getArrayByteFromBitmap(imageBitmap);
                    iv_transfer_confirmation_photo.setImageBitmap(imageBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                finally {
                    if (imageStream!=null){
                        try{
                            imageStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
