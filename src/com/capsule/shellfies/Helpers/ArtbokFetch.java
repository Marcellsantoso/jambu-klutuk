/*******************************************************************************
 * Copyright 2013 Comcast Cable Communications Management, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.capsule.shellfies.Helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.capsule.shellfies.Fragments.BaseFragmentShellfies;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

public class ArtbokFetch {
	
	public static final String TAG = "DribbbleFetch";

	
	public void load(final BaseFragmentShellfies caller, int itemsPerPage, int page) {
		
		new AsyncTask<String, Void, String>() {

			OkHttpClient client = new OkHttpClient();

			private Exception exception;

			protected String doInBackground(String... urls) {
				try {
					return get(new URL(urls[0]));

				} catch (Exception e) {
					this.setException(e);
					Log.e(TAG, "Exception: " + e);
					return null;
				}
			}

			protected void onPostExecute(String data) {
				ArtbookFeed feed  = new Gson().fromJson(data, ArtbookFeed.class);
				caller.onDataLoaded(feed);
			}

			String get(URL url) throws IOException {
				HttpURLConnection connection = client.open(url);
				InputStream in = null;
				try {
					in = connection.getInputStream();
					byte[] response = readFully(in);
					return new String(response, "UTF-8");
				} finally {
					if (in != null)
						in.close();
				}
			}

			byte[] readFully(InputStream in) throws IOException {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				for (int count; (count = in.read(buffer)) != -1;) {
					out.write(buffer, 0, count);
				}
				return out.toByteArray();
			}

			@SuppressWarnings("unused")
			public Exception getException() {
				return exception;
			}

			public void setException(Exception exception) {
				this.exception = exception;
			}

		}.execute("http://api.dribbble.com/shots/popular?per_page="+itemsPerPage+"&page="+page);
	}

}
