package com.freshyummy.android;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView wvPrivacyPolicy;
    ImageView back;
    ProgressDialog pDialog;
    boolean loadingFinished = true;
    boolean redirect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wvPrivacyPolicy = findViewById(R.id.wv_privacy_policy);
        back = findViewById(R.id.back);

        pDialog = new ProgressDialog(PrivacyPolicyActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        wvPrivacyPolicy.setWebViewClient(new MyBrowser());
        wvPrivacyPolicy.getSettings().setLoadsImagesAutomatically(true);
        //wvPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        wvPrivacyPolicy.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT >= 19) {
            wvPrivacyPolicy.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            wvPrivacyPolicy.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        wvPrivacyPolicy.loadUrl("http://freshyummy.co.id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(urlNewString);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            loadingFinished = false;
            pDialog.show();
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                //HIDE LOADING IT HAS FINISHED
                pDialog.dismiss();
                //wvPrivacyPolicy.setVisibility(View.INVISIBLE);
            } else {
                redirect = false;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Utilities.checkBanned(PrivacyPolicyActivity.this);
    }
}
