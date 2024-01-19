package com.homeone.visitormanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.homeone.visitormanagement.databinding.ActivityMainBinding;
import com.homeone.visitormanagement.modal.User;
import com.homeone.visitormanagement.utility.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String TAG = "VOLLEY";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.getRoot().setVisibility(View.INVISIBLE);
        binding.progressBarMain.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.loginButton.setOnClickListener(view -> {

            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "empty field", Toast.LENGTH_SHORT).show();
            } else {
                checkCred(email, password);
            }
        });

        binding.registerText.setOnClickListener(view -> {
            //open browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://apartment.homeonetechnologies.in/?page_id=13"));
            startActivity(intent);
        });

        binding.forgotPass.setOnClickListener(view -> {
            //open browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://apartment.homeonetechnologies.in/wp-login.php?action=lostpassword"));
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Config.initUrl();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("uid");
            ref = ref.child(currentUser.getUid()).child("role");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    updateUI(Objects.requireNonNull(snapshot.getValue()).toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            //set visibility gone
            binding.progressBarMain.setVisibility(View.GONE);
            binding.getRoot().setVisibility(View.VISIBLE);
        }
    }

    private void checkCred(String email, String password) {

        binding.progressBarMain.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL;
            if (Config.serverUrl != null) {
                URL = Config.serverUrl + "auth_user";
            } else {
                Toast.makeText(this, "Server is not available please contact admin", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user", email);
            jsonBody.put("pass", password);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    String emailJs = "";
                    JSONObject role;
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean b = jsonObject.getBoolean("success");
                            if (b) {
                                emailJs = jsonObject.getString("email");
                                role = jsonObject.getJSONObject("role");
                                Log.i("VOLLEY", role.keys().next());
                                user = new User(email,role.keys().next(),emailJs,password);
                                loginFirebase(user);
                            }else {
                                binding.progressBarMain.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"INCORRECT CREDENTIALS",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

                    if (message != null)
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    binding.progressBarMain.setVisibility(View.GONE);

                    Log.e("VOLLEY", message + "errrrr");
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

    private void loginFirebase(User user) {
         mAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            myRef = database.getReference("uid").child(firebaseUser.getUid());
                            myRef.setValue(user);
                            updateUI(user.role);
                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                //Already Registered sign In
                                mAuth.signInWithEmailAndPassword(user.email, user.password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.i(TAG, "signInWithEmail:success");
                                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("uid");
                                                    ref = ref.child(firebaseUser.getUid()).child("role");
                                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            updateUI(Objects.requireNonNull(snapshot.getValue()).toString());
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.i(TAG, "signInWithEmail:failure", task.getException());
                                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    //updateUI(null);
                                                }
                                            }
                                        });
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    }
                });
    }

    private void updateUI(String role) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        myRef = database.getReference("uid").child(mAuth.getCurrentUser().getUid());
                        myRef.child("token").setValue(token);
                    }
                });
        Intent intent;
        if (role.equals("gatekeeper")) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        } else {
            intent = new Intent(MainActivity.this, OwnerActivity.class);
        }
        startActivity(intent);
        finish();

    }
}