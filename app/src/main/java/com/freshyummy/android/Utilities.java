package com.freshyummy.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.security.cert.CertificateException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;

/**
 * Created by afi on 19/03/18.
 */

public class Utilities {
    private static String image_url = "http://freshyummy.co.id/";
    private static String php_url = "https://freshyummy.co.id/";

    public static String getURLImageKategori() {
        return image_url + "freshyummy/gambar/kategori/";
    }

    public static String getURLImageIklan() {
        return image_url + "freshyummy/gambar/iklan/";
    }

    public static String getURLImageProduk() {
        return image_url + "freshyummy/gambar/produk/";
    }

    public static String getURLImageBank() {
        return image_url + "freshyummy/gambar/bank/";
    }

    public static String getURLImageTransfer() { return image_url + "freshyummy/gambar/buktitransfer/"; }

    public static String getBaseURLPhp() {
        return php_url + "freshyummy/android/user/";
    }

    public static void strikeThroughText(TextView price){
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void setUser(Context context, User user) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("xUser", json);
        prefsEditor.apply();
    }

    public static void signOutUser(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        prefsEditor.putBoolean("xLogin", false);
        prefsEditor.apply();
    }

    public static User getUser(Context context) {
        if (isLogin(context)) {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

            Gson gson = new Gson();
            String json = mPrefs.getString("xUser", "");
            return gson.fromJson(json, User.class);
        } else {
            return new User();
        }
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static Boolean isLogin(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("xLogin", false);
    }

    public static void setLogin(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        prefsEditor.putBoolean("xLogin", true);
        prefsEditor.apply();
    }

    public static String getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("token", token);
        if (token == null) {
            token = "";
        }
        return token;
    }

//    public static void showAsToast(Context context, String text) {
//        if (mToast != null) mToast.cancel();
//        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//        mToast.show();
//    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String capitalizeWord(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z-éá])([a-z-éá]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    public static String getCurrency(String value) {
        DecimalFormat currency = (DecimalFormat) NumberFormat.getInstance();
        Locale currentLocale = new Locale("in", "ID");
        String symbol = Currency.getInstance(currentLocale).getSymbol(currentLocale);
        currency.setGroupingUsed(true);
        currency.setPositivePrefix(symbol + " ");
        currency.setNegativePrefix("-" + symbol + " ");
        return currency.format(Double.parseDouble(value));
    }

    public static String getRandom(int x){
        String charcarter = "0123456789abcdefghijklmnopqrstuvwqyz";
        final int N = charcarter.length();
        Random r = new Random();
        String alpha="";
        for (int i = 0; i < x; i++) {
            alpha = alpha+ charcarter.charAt(r.nextInt(N));
        }
        return alpha;
    }

    public static String getArrayByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //return stream.toByteArray();
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }
}
