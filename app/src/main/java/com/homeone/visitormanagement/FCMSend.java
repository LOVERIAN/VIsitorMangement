package com.homeone.visitormanagement;

import android.app.DownloadManager;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "key=AAAA9151mus:APA91bHBWaGr-rPv-JAMnMvMwWtLEfTJPSytQPYLBD995vqKY4-oOJc_4W7-WrGCeqLmiwRxF4QgJxZZcZMJaT7tpjv7QhaJGbZj213eaU4KdFF5nNhLDemdZpaiulx48IqZcS2hYeGf";

    public static void pushNotification(Context context,String token, String title, String message) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            JSONObject json = new JSONObject();
            json.put("to",token);
            JSONObject notification  = new JSONObject();
            notification.put("title",title);
            notification.put("body",message);
            json.put("notification",notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("TOKEN","SUCCESS");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.i("TOKEN","Failed");
                    String message = null;
                    if (volleyError instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                    } else if (volleyError instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                    }

                    Log.e("TOKEN", message);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

