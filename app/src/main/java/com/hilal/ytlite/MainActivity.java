package com.hilal.ytlite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Single-screen app. The whole interface lives in assets/index.html and is
 * loaded into a WebView with a real https base URL so the official YouTube
 * embed player behaves correctly (it needs a proper origin + referrer).
 */
public class MainActivity extends Activity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.parseColor("#030303"));

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setSupportZoom(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        s.setUserAgentString(
                "Mozilla/5.0 (Linux; Android 13; Redmi 12C) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // Full-screen video support inside the embed.
                super.onShowCustomView(view, callback);
            }
        });

        // Load the bundled page from assets, but give it an https origin.
        String html = readAsset("index.html");
        webView.loadDataWithBaseURL("https://www.youtube.com/", html, "text/html", "utf-8", null);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.parseColor("#030303"));
        root.addView(webView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(root);
    }

    private String readAsset(String name) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getAssets().open(name);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            return "<html><body style='background:#030303;color:#fff;font-family:sans-serif'>"
                    + "<h3 style='padding:24px'>Could not load interface.</h3></body></html>";
        }
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        // Let the page close an open player first, then fall back to WebView history.
        webView.evaluateJavascript(
                "(function(){try{return window.handleBack ? window.handleBack() : false}catch(e){return false}})();",
                value -> {
                    if (!"true".equals(value)) {
                        if (webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            MainActivity.super.onBackPressed();
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Keep the page alive so audio/embed state is not destroyed on screen-off.
    }
}
