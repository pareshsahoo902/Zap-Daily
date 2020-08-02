package com.zappvtltd.zapdaily.DrawerUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zappvtltd.zapdaily.R;

public class About extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        webView=findViewById(R.id.aboutweb);
        webView.loadUrl("https://pareshsahoo902.github.io/ZapDaily.github.io/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}