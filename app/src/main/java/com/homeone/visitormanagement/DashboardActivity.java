package com.homeone.visitormanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.homeone.visitormanagement.modal.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class DashboardActivity extends AppCompatActivity {

    private WebView myWebView;
    private ConstraintLayout constraintLayout;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button logout;
    private DatabaseReference myRef;
    private User userdata;
    private ImageView captureImage;
    private ImageView clickImage;
    private FirebaseStorage storage;
    private String id;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference("uid");

        //logout = findViewById(R.id.logoutBtn);
        constraintLayout = findViewById(R.id.constraintWebView);
        myWebView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBarDash);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;

        myRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userdata = snapshot.getValue(User.class);
                assert userdata != null;
                if (userdata.role.equals("gatekeeper")) {
                    login();
                    Log.i("TOKEN","token");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*logout.setOnClickListener( view -> {
            auth.signOut();
            android.webkit.CookieManager.getInstance().removeAllCookie();
            Intent i = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });*/

    }

    /*
        private void checkCred() {

            try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                String URL = "http://34.93.115.120:8081/send_prompt";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("prompt", "show tables");
                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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

                        Log.e("VOLLEY", message);
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        //String responseString = "";
                        //if (response != null) {
                        //  responseString = String.valueOf(response.statusCode);
                        //}
                        return super.parseNetworkResponse(response);
                    }

                };

                requestQueue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    */
    private static class WebPortal extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void login() {

        myWebView.setWebViewClient(new DashboardActivity.WebPortal());

        //logout.setVisibility(View.GONE);

        myWebView.getSettings().setJavaScriptEnabled(true);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);


        myWebView.loadUrl("https://apartment.homeonetechnologies.in/");
       // myWebView.setVisibility(View.INVISIBLE);
        myWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {

                if (url.equals("https://apartment.homeonetechnologies.in/")) {
                    myWebView.loadUrl("javascript:{" +
                            "ins=document.getElementsByTagName('input');" +
                            "ins[0].value='" + userdata.name + "';" +
                            "ins[1].value='" + userdata.password + "';" +
                            "ins[2].value=true;" +
                            "document.getElementsByTagName('form')[0].submit();" +
                            "};");
                }

                if (url.equals("https://apartment.homeonetechnologies.in/?apartment-dashboard=user&page=visitor-manage&tab=visitor-checkinlist&message=1")) {

                    Log.i("WEBVIEW","CALLED");
                    //myWebView.setVisibility(View.GONE);
                    formSubmitted(userdata.name);
                }

                progressBar.setVisibility(View.GONE);
            }
        });

        myWebView.setVisibility(View.VISIBLE);
        myWebView.clearCache(true);
        myWebView.clearHistory();

        WebView view = (WebView) this.findViewById(R.id.webView);

        String url = "https://apartment.homeonetechnologies.in/";
        view.loadUrl(url);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //logout.setVisibility(View.VISIBLE);
    }

    private void sendPhoto(String uid) {

        BottomSheetDialog bottomSheet = new BottomSheetDialog(id);
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");

    }

    private void formSubmitted(String gatekeeperName) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://34.93.115.120:8081/form_submit";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user", gatekeeperName);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        id = jsonObject.getString("id");

                        Log.i("TOKEN",id);
                        sendNotification(id);
                        sendPhoto(id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
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

                    Log.e("VOLLEY", message);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    //String responseString = "";
                    //if (response != null) {
                    //  responseString = String.valueOf(response.statusCode);
                    //}
                    return super.parseNetworkResponse(response);
                }

            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String uid) {
        myRef = FirebaseDatabase.getInstance().getReference("requests");
        Log.i("TOKEN",id);
        myRef.child(id).child("owner").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String owner = snapshot.getValue(String.class);
                Log.i("TOKEN",owner);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("uid");
                //myRef = FirebaseDatabase.getInstance().getReference("uid");
                databaseReference.orderByChild("name").equalTo(owner).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String token = "";
                        for (DataSnapshot shot : snapshot.getChildren()) {
                            for (DataSnapshot shot1 : shot.getChildren()) {
                                if (shot1.getKey().equals("token")) {
                                    token = shot1.getValue(String.class);
                                }
                            }
                        }
                        Log.i("TOKEN",token);
                        FCMSend.pushNotification(
                                DashboardActivity.this,
                                token,
                                "New Visitor",
                                "You Have New Visitor"
                        );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}