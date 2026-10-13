package com.jackeychankey.blobdrop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.webkit.WebViewAssetLoader;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

/**
 * Blob Drop — 게임(HTML5)을 앱 안의 WebView에서 실행한다.
 * 게임 파일은 assets/www 에 들어 있고, https://appassets.androidplatform.net 가상 주소로 불러온다
 * (localStorage 저장, WebAudio, 진동 API가 일반 웹과 똑같이 동작).
 */
public class MainActivity extends Activity {

    private static final String HOST = "appassets.androidplatform.net";
    private static final int BG = 0xFF1B1535;

    private static final long MIN_AD_GAP_MS = 20_000; // 전면광고 최소 간격(연속 노출 방지)

    private WebView web;
    private InterstitialAd interstitial;
    private boolean adLoading;
    private long lastAdShown;
    private boolean resumed;
    private boolean adDonePending; // 광고가 닫혔고, 화면이 복귀하면 게임에 알려야 함

    /** 게임(JS)에서 window.AndroidAds.show() 로 호출 → 광고가 끝나면 window.__adDone() 실행 */
    private class AdBridge {
        @JavascriptInterface
        public void show() {
            runOnUiThread(MainActivity.this::showInterstitial);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 28) {
            window.getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(BG);
        web = new WebView(this);
        web.setBackgroundColor(BG);
        root.addView(web, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(root);

        // 상태바/노치/제스처 바 영역을 피해서 게임을 배치
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            int l, t, r, b;
            if (Build.VERSION.SDK_INT >= 30) {
                Insets i = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                l = i.left; t = i.top; r = i.right; b = i.bottom;
            } else {
                l = insets.getSystemWindowInsetLeft(); t = insets.getSystemWindowInsetTop();
                r = insets.getSystemWindowInsetRight(); b = insets.getSystemWindowInsetBottom();
            }
            v.setPadding(l, t, r, b);
            return Build.VERSION.SDK_INT >= 30 ? WindowInsets.CONSUMED : insets.consumeSystemWindowInsets();
        });

        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .setDomain(HOST)
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);                    // localStorage (기록·도감·설정 저장)
        s.setMediaPlaybackRequiresUserGesture(false);    // WebAudio(BGM/효과음)
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setTextZoom(100);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        web.setOverScrollMode(View.OVER_SCROLL_NEVER);
        web.setVerticalScrollBarEnabled(false);
        web.setHorizontalScrollBarEnabled(false);
        web.setLongClickable(false);
        web.setOnLongClickListener(v -> true);
        web.setHapticFeedbackEnabled(false);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return loader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // 게임 밖 주소로는 이동하지 않는다
                return !HOST.equals(request.getUrl().getHost());
            }
        });

        web.addJavascriptInterface(new AdBridge(), "AndroidAds");
        new Thread(() -> MobileAds.initialize(this, status -> runOnUiThread(this::loadInterstitial))).start();

        hideSystemBars();
        web.loadUrl("https://" + HOST + "/assets/www/index.html");
    }

    private void loadInterstitial() {
        if (interstitial != null || adLoading) return;
        adLoading = true;
        InterstitialAd.load(this, BuildConfig.ADMOB_INTERSTITIAL_ID, new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        adLoading = false;
                        interstitial = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        adLoading = false;
                        interstitial = null;
                    }
                });
    }

    private void showInterstitial() {
        long now = android.os.SystemClock.elapsedRealtime();
        if (interstitial == null || (lastAdShown != 0 && now - lastAdShown < MIN_AD_GAP_MS)) {
            loadInterstitial();
            notifyAdDone(); // 광고가 없으면 기다리지 않고 바로 게임 진행
            return;
        }
        final InterstitialAd ad = interstitial;
        interstitial = null;
        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                lastAdShown = android.os.SystemClock.elapsedRealtime();
                adDonePending = true;
                loadInterstitial();
                if (resumed) notifyAdDone();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError error) {
                loadInterstitial();
                notifyAdDone();
            }
        });
        ad.show(this);
    }

    private void notifyAdDone() {
        adDonePending = false;
        web.evaluateJavascript("window.__adDone&&window.__adDone()", null);
    }

    private void hideSystemBars() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 30) {
            window.setDecorFitsSystemWindows(false);
            WindowInsetsController c = window.getInsetsController();
            if (c != null) {
                c.hide(WindowInsets.Type.systemBars());
                c.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemBars();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumed = true;
        web.onResume();
        if (adDonePending) notifyAdDone();
    }

    @Override
    protected void onPause() {
        resumed = false;
        web.onPause(); // 백그라운드에서는 게임/BGM 정지
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        web.destroy();
        super.onDestroy();
    }
}
