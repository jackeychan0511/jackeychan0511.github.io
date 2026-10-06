package com.jackeychankey.neonlane;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/** 네온 레인: 웹 게임(assets/www)을 https 가상 도메인으로 서빙하는 WebView 셸. 광고·결제 없음. */
public class MainActivity extends Activity {
    private static final String HOST = "appassets.androidplatform.net";
    private static final String START_URL = "https://" + HOST + "/assets/index.html";
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        web = new WebView(this);
        web.setBackgroundColor(Color.BLACK);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setUserAgentString(s.getUserAgentString() + " NeonLaneApp/1.0");

        web.addJavascriptInterface(new Bridge(), "NeonLane");
        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri u = request.getUrl();
                if (HOST.equals(u.getHost()) && u.getPath() != null && u.getPath().startsWith("/assets/")) {
                    return serveAsset(u.getPath().substring("/assets/".length()));
                }
                return null;
            }

            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {   // API 24+ 에서도 기본 구현이 이 메서드로 위임됨
                Uri u = Uri.parse(url);
                if (HOST.equals(u.getHost())) return false;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, u));
                } catch (ActivityNotFoundException ignored) {
                }
                return true;
            }
        });

        if (savedInstanceState != null) {
            web.restoreState(savedInstanceState);
        } else {
            web.loadUrl(START_URL);
        }
        registerPredictiveBack();
    }

    /** Android 13+(특히 targetSdk 36)에서는 onBackPressed 가 호출되지 않으므로 OnBackInvokedCallback 을 리플렉션으로 등록 */
    private void registerPredictiveBack() {
        if (Build.VERSION.SDK_INT < 33) return;
        try {
            Class<?> cbClass = Class.forName("android.window.OnBackInvokedCallback");
            Object cb = Proxy.newProxyInstance(cbClass.getClassLoader(), new Class<?>[]{cbClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) {
                    if ("onBackInvoked".equals(method.getName())) handleBack();
                    return null;
                }
            });
            Object dispatcher = Activity.class.getMethod("getOnBackInvokedDispatcher").invoke(this);
            dispatcher.getClass().getMethod("registerOnBackInvokedCallback", int.class, cbClass).invoke(dispatcher, 0, cb);
        } catch (Throwable ignored) {
            // 실패해도 onBackPressed 경로로 동작
        }
    }

    private WebResourceResponse serveAsset(String path) {
        if (path.isEmpty() || path.contains("..")) path = "index.html";
        try {
            InputStream in = getAssets().open("www/" + path);
            return new WebResourceResponse(mime(path), path.endsWith(".png") ? null : "UTF-8", in);
        } catch (IOException e) {
            Map<String, String> h = new HashMap<String, String>();
            return new WebResourceResponse("text/plain", "UTF-8", 404, "Not Found", h, null);
        }
    }

    private static String mime(String p) {
        if (p.endsWith(".html")) return "text/html";
        if (p.endsWith(".js")) return "application/javascript";
        if (p.endsWith(".png")) return "image/png";
        if (p.endsWith(".json") || p.endsWith(".webmanifest")) return "application/json";
        if (p.endsWith(".css")) return "text/css";
        return "application/octet-stream";
    }

    /** 웹 → 앱 */
    private class Bridge {
        @JavascriptInterface
        public void share(String text) {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setType("text/plain");
            send.putExtra(Intent.EXTRA_TEXT, text);
            Intent chooser = Intent.createChooser(send, null);
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(chooser);
        }
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void handleBack() {
        // 웹(nlBack)이 처리하면 종료하지 않음. 홈 화면에서만 앱 종료
        web.evaluateJavascript("(window.nlBack&&window.nlBack())?'1':'0'", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String v) {
                if (v == null || !v.contains("1")) finish();
            }
        });
    }

    @Override
    protected void onPause() {
        web.evaluateJavascript("window.nlPause&&window.nlPause()", null);
        web.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.removeJavascriptInterface("NeonLane");
            web.destroy();
        }
        super.onDestroy();
    }
}
