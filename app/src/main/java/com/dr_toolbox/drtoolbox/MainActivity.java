/**
 * Created on 05/03/16 by Petko YOTOV www.pmwiki.org/petko
 * Copyright (c) 2016-2018 Petko YOTOV All rights reserved
 * Commissioned by and developed with Dr Toolbox who are granted
 * worldwide, perpetual, irrevocable, royalty-free permission,
 * to use, modify, copy, publish, sub-licence and sell their copies
 * of the software as long as their “open source licences” are respected
 */

package com.dr_toolbox.drtoolbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class MainActivity extends AppCompatActivity {
  private WebView mWebView;
  final Activity activity = this;

  public class MyWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
      new AlertDialog.Builder(activity)
          .setTitle("Dr Toolbox")
          .setMessage(message)
          .setPositiveButton(android.R.string.ok,
              new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  result.confirm();
                }
              }
          )
          .setCancelable(false)
          .create()
          .show();
      return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
      new AlertDialog.Builder(activity)
          .setTitle("Dr Toolbox")
          .setMessage(message)
          .setPositiveButton(android.R.string.ok,
              new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  result.confirm();
                }
              }
          )
          .setNegativeButton(android.R.string.cancel,
              new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  result.cancel();
                }
              }
          )
          .create()
          .show();
      return true;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();
    mWebView = (WebView) findViewById(R.id.activity_main_webview);

    WebSettings webSettings = mWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setDatabaseEnabled(true);

    webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());

    webSettings.setAppCacheEnabled(true);
//    webSettings.setAppCacheMaxSize(20 * 1024 * 1024); // deprecated?
    webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

    webSettings.setLoadsImagesAutomatically(true);
    webSettings.setAllowFileAccess(true);


    mWebView.setWebViewClient(new MyAppWebViewClient(activity));
    mWebView.setWebChromeClient(new MyWebChromeClient());


    if (savedInstanceState == null) {
      mWebView.loadUrl("https://www.dr-toolbox.com/dr-toolbox/app/?idevice=1");
    }

  }
  @Override
  public void onBackPressed() {
    if (mWebView.canGoBack()) {
      mWebView.goBack();
    }
    else {
      super.onBackPressed();
    }
  }

//  @Override
//  public void onStop() {
//    super.onStop();
////    activity.finish();
//  }
//
//  @Override
//  public void onDestroy() {
////    activity.finish();
//    super.onDestroy();
//  }
//

  @Override
  protected void onSaveInstanceState(Bundle outState ) {
    super.onSaveInstanceState(outState);
    mWebView.saveState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    mWebView.restoreState(savedInstanceState);
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (connectivityChangeReceiver!=null) unregisterReceiver(connectivityChangeReceiver);
  }

  @Override
  protected void onResume() {
    super.onResume();

    IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    registerReceiver(connectivityChangeReceiver, intentFilter);
  }

  private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {

    public void onReceive(Context context, Intent intent) {
        mWebView.setNetworkAvailable(isOnline());
    }
  };

  public boolean isOnline() {
    ConnectivityManager cm =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }
}



