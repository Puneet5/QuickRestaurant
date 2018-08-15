package com.example.hp.quick_rest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText user,mail,numb,password;
    Button signup,btnLogin;
    private FirebaseAuth auth;
    private ProgressDialog PD;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        mDatabase = FirebaseDatabase.getInstance().getReference();



        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, First_page.class));
            finish();
        }


        Button btn = (Button) findViewById(R.id.signup);
        Button btnLogin = (Button) findViewById(R.id.button5);
        user = (EditText) findViewById(R.id.user);
        mail = (EditText) findViewById(R.id.mail);
        numb = (EditText) findViewById(R.id.numb);
        password = (EditText) findViewById(R.id.password);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    final String username = user.getText().toString().trim();
                    final String email = mail.getText().toString();
                    final String pass = password.getText().toString();
                    final String number = numb.getText().toString().trim();

                    // create user object and set all the properties

                    try {
                        if (password.length() > 0 && email.length() > 0) {


                            PD.show();
                            auth.createUserWithEmailAndPassword(email, pass)
                                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {


                                                Toast.makeText(
                                                        Login.this,
                                                        "Authentication Failed",
                                                        Toast.LENGTH_LONG).show();
                                                Log.v("error", task.getResult().toString());
                                            } else {
                                                final String uid = task.getResult().getUser().getUid();
                                                User user  = new User();
                                                user.setUsername(username);
                                                user.setNumber(number);
                                                user.setPassword(pass);
                                                user.setEmail(email);
                                                mDatabase.child(uid).setValue(user);
                                                Intent intent = new Intent(Login.this, First_page.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            PD.dismiss();
                                        }
                                    });
                        } else {
                            Toast.makeText(
                                    Login.this,
                                    "Fill All Fields",
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }






                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                finish();
            }
        });


    }


                                   // save the user at UserNode under user UID


                                   // to check if user filled all the required fieds
                                   public boolean validateForm() {
                                       boolean alldone = true;
                                       String username = user.getText().toString().trim();
                                       String number = numb.getText().toString().trim();
                                       String pass = password.getText().toString().trim();
                                       String email = mail.getText().toString().trim();
                                       if (TextUtils.isEmpty(username)) {
                                           user.setError("Enter your first name");
                                           return false;
                                       } else {
                                           alldone = true;
                                           user.setError(null);
                                       }
                                       if (TextUtils.isEmpty(number)) {
                                           numb.setError("Enter your last name");
                                           return false;
                                       } else {
                                           alldone = true;
                                           numb.setError(null);
                                       }
                                       if (TextUtils.isEmpty(pass)) {
                                           password.setError("Enter your Age");
                                           return false;
                                       } else {
                                           alldone = true;
                                           password.setError(null);
                                       }
                                       if (TextUtils.isEmpty(email)) {
                                           mail.setError("Enter your Email");
                                           return false;
                                       } else {
                                           alldone = true;
                                           mail.setError(null);
                                       }
                                       return alldone;
                                   }











    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Login.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
