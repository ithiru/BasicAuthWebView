/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package thiru.in.basicauthwebview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Webview doesn't support Basic Authentication Prompt as the browser does.
 *
 * This app calls REST Service by passing user id and password, get the cookies
 * and access the protected application
 *
 * This app dependent on Nginx server with Lua Script to act as a proxy between the Android
 * application and the Basic Authentication Protected Web Application.
 *
 * The Lua script accept the cookie and in turn convert the cookie to Basic Authentication header
 * to access the protected app through Web View.
 *
 * Two Advantages
 *
 * 1. Access the internal application through internet
 * 2. Allow transparent access through Basic Authentication
 *
 * Author - Thirumalai Veerasamy techy@thiru.in
 * Date - 13-Oct-2014
 */
public class ShowWebViewActivity extends Activity {

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_view);

        // Initialize the Cookie Manager
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        String url = "http://auth.thiru.in/";
        // Call the REST Service to Authenticate
        // This is sample app, the app can have a login form, accept input, encode the
        // id and passowrd using Base64 for Validating
        AsyncTask<String, Void, CookieStore> cookieStoreTask = new Authenticate(this).execute(
                url + "app-auth",
                "ZGVtbzpkZW1v"); //Base64 Encoded for demo:demo

        try {
            cookieManager.removeAllCookie();
            CookieStore cookieStore = cookieStoreTask.get();
            List<Cookie> cookies = cookieStore.getCookies();
            // If the User id and password is wrong, the cookie store will not have any cookies
            // And the page will display 403 Access Denied
            for (Cookie cookie : cookies) {
                Log.i("Cookie", cookie.getName() + " ==> " + cookie.getValue());
                // Add the REST Service Cookie Responses to Cookie Manager to make it
                // Available for the WebViewClient
                cookieManager.setCookie(url,
                        cookie.getName() + "=" + cookie.getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(url);
    }

    // Handle Back button
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
