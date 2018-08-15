package com.example.hp.quick_rest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;



public class Login2 extends AppCompatActivity {

    EditText emailid,pass;
    Button login;
    LoginButton loginButton;
    Session session;
    private FirebaseAuth auth;
    private ProgressDialog PD;





    CallbackManager callbackManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();



        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Login2.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Login2.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_login2);
        session = new Session(getApplicationContext());

        Button btn1 = (Button)findViewById(R.id.sign);
        final EditText emailid = (EditText) findViewById(R.id.email) ;
        final EditText pass = (EditText) findViewById(R.id.pass) ;
        Button login = (Button)findViewById(R.id.login);
          loginButton = (LoginButton)findViewById(R.id.loginbtn);

        /*LoginManager.getInstance().logInWithReadPermissions(Login2.this, Arrays.asList("public_profile", "user_friends"));*/


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();

                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {


                                         String username = emailid.getText().toString();
                                         String password = pass.getText().toString();

                                         if (emailid.getText().toString().trim().isEmpty() && pass.getText().toString().trim().isEmpty()) {
                                             emailid.setError("Enter email id");
                                             pass.setError("Enter your Password");
                                         } else if (TextUtils.isEmpty(emailid.getText().toString()) || emailid.getText().toString().length() < 10) {

                                             emailid.setError("Enter valid email id");

                                         } else if (TextUtils.isEmpty(pass.getText().toString())) {

                                             pass.setError("Enter your Password");

                                         } else {
                                                 if (password.length() > 0 && emailid.length() > 0) {
                                                     PD.show();
                                                     auth.signInWithEmailAndPassword(username, password)
                                                             .addOnCompleteListener(Login2.this, new OnCompleteListener<AuthResult>() {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                                                     if (!task.isSuccessful()) {
                                                                         Toast.makeText(
                                                                                 Login2.this,
                                                                                 "Authentication Failed",
                                                                                 Toast.LENGTH_LONG).show();
                                                                         Log.v("error", task.getResult().toString());
                                                                     } else {
                                                                         Intent intent = new Intent(Login2.this, First_page.class);
                                                                         startActivity(intent);
                                                                         finish();
                                                                     }
                                                                     PD.dismiss();
                                                                 }
                                                             });
                                                 } else {
                                                     Toast.makeText(
                                                             Login2.this,
                                                             "Fill All Fields",
                                                             Toast.LENGTH_LONG).show();
                                                 }

                                             }
                                         }
                                     });








        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Login2.this, Login.class));
                                    }
                                });


        Button btn_fb_login = (Button) findViewById(R.id.fb);

        btn_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
               }
        });


    }

    public void RequestData(){

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();

                Log.i("LoginActivity",
                        response.toString());
                try {
                    if(json != null){

                        String fb_id = object.getString("id");
                        try {
                            URL profile_pic = new URL(
                                    "http://graph.facebook.com/" + fb_id + "/picture?type=large");
                            Log.i("profile_pic",
                                    profile_pic + "");

                            String profile_url="https://graph.facebook.com/" + fb_id+ "/picture?type=large";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String fb_name = json.getString("name");
                        String fb_email =json.getString("email");
                        String Profile_link = json.getString("id");


                        Log.i("values",fb_name.toString()+"----"+fb_email.toString()+"-----"+Profile_link.toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

           callbackManager.onActivityResult(requestCode, resultCode, data);


            }



            public void hideKeyboard(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Login.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

    @Override    protected void onResume() {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login2.this, First_page.class));
            finish();
        }
        super.onResume();
    }


        }

