/**
 * Created on 05/03/16 by Petko YOTOV www.pmwiki.org/petko
 * Copyright (c) 2016 Petko YOTOV All rights reserved
 * Commissioned by and developed with Dr Toolbox who are granted
 * worldwide, perpetual, irrevocable, royalty-free permission,
 * to use, modify, copy, publish, sub-licence and sell their copies
 * of the software as long as their “open source licences” are respected
 */

package com.dr_toolbox.drtoolbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

public class MyAppWebViewClient extends WebViewClient {
  private final WeakReference<Activity> mActivityRef;


  public MyAppWebViewClient(Activity activity) {
    mActivityRef = new WeakReference<Activity>(activity);
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    final Activity activity = mActivityRef.get();

    if (url.startsWith("tel:")) {
      Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
      activity.startActivity(intent);
      view.reload();
      return true;
    }

    if (url.startsWith("mailto:")) {
      MailTo mt = MailTo.parse(url);
      Intent i = newEmailIntent(activity, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
      activity.startActivity(i);
      view.reload();
      return true;
    }

    if (url.startsWith("newtab:")) {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.substring(7)));
      view.getContext().startActivity(intent);
      return true;
    }

    return false;

  }

  private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
    intent.putExtra(Intent.EXTRA_TEXT, body);
    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
    intent.putExtra(Intent.EXTRA_CC, cc);
    intent.setType("message/rfc822");
    return intent;
  }
}
