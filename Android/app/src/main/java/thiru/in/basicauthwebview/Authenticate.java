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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Access REST service to authentication the user and return the cookies
 *
 * Author - Thirumalai Veerasamy techy@thiru.in
 * Date - 13-Oct-2014
 */
public class Authenticate extends AsyncTask<String, Void, CookieStore> {

    private Context context;
    private ProgressDialog progDailog;

    public Authenticate(Context context) {
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(this.context);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        Log.i("OnPreExecute", "Show");
        progDailog.show();
    }
    @Override
    protected void onPostExecute(CookieStore unused) {
        super.onPostExecute(unused);
        if (progDailog.isShowing()) {
            progDailog.dismiss();
            Log.i("onPostExecute", "Dismiss");
        }
    }
    @Override
    protected CookieStore doInBackground(String... data) {
        String url = data[0];
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",data[1]));
        DefaultHttpClient httpClient = new DefaultHttpClient() ;

        BasicCookieStore cookieStore = new BasicCookieStore();
        httpClient.setCookieStore(cookieStore);

        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                List<Cookie> cookies = cookieStore.getCookies();

                for (Cookie cookie : cookies) {
                    Log.i("Cookie", cookie.getName() + " ==> " + cookie.getValue());
                }
            } else {
                Log.i("Authenticate", "Invalid user and password combination");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cookieStore;
    }
}
