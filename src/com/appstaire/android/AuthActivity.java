package com.appstaire.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class AuthActivity extends Activity implements OnClickListener {
  private WebView webview;
  private String authURL;
  static final String CALLBACK_ROOT_URL = "http://appstaire.com";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_auth);

    TextView header = (TextView) findViewById(R.id.textViewAuthHeader);
    header.setText("Appstaire");
    Typeface typeface = Typeface.createFromAsset(getAssets(),
        "fonts/SourceSansPro-Black.ttf");

    header.setTypeface(typeface);

    Button buttonSignInWithFacebook = (Button) findViewById(R.id.button_sign_in_with_facebook);

    buttonSignInWithFacebook.setOnClickListener(this);

    webview = (WebView) findViewById(R.id.webview);
    configWebView();
  }
  
  private void handleAuthResponse(String provider, String auth_token) {
    SharedPreferences preferences = getSharedPreferences("AppstairePreferences", MODE_PRIVATE);
    SharedPreferences.Editor preferencesEditor = preferences.edit();
    preferencesEditor.putString("user_token", auth_token);
    preferencesEditor.commit();
    
    Intent intent = new Intent(this, MainActivity.class);
    intent.putExtra("activityCaller", "AuthActivity");
    this.startActivity(intent);
    
    finish();
  }

  @Override
  public void onClick(View view) {
    this.authURL = CALLBACK_ROOT_URL + "/auth/facebook";

    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.removeAllCookie();
    
    webview.loadUrl(this.authURL);
    webview.setVisibility(View.VISIBLE);
    webview.bringToFront();
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void configWebView() {
    WebSettings webSettings = webview.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setSavePassword(false);
    webSettings.setSaveFormData(false);
    
    this.webview.setVisibility(View.GONE);
    this.webview.requestFocus(View.FOCUS_DOWN);
    this.webview.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_UP:
          if (!v.hasFocus()) {
            v.requestFocus();
          }
          break;
        }
        return false;
      }
    });

    this.webview.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageFinished(WebView view, String url) {
        Uri uri = Uri.parse(url);

        if (url.startsWith(CALLBACK_ROOT_URL + "/users/token")) {
          String provider = uri.getQueryParameter("provider");
          String auth_token = uri.getQueryParameter("user_token");
          
          handleAuthResponse(provider, auth_token);
          
          webview.setVisibility(View.GONE);
        }

        super.onPageFinished(view, url);
      }
    });
  }
}
