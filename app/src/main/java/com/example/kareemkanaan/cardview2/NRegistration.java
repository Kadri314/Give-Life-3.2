package com.example.kareemkanaan.cardview2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NRegistration extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail,etPassword;
    private TextView tvlogIn;
    private Button bRegister;
    private ProgressDialog proDia;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nregistration);
        // getting refrenecs
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvlogIn= (TextView) findViewById(R.id.tvSignIn);
        bRegister= (Button) findViewById(R.id.bLogIn);
        // adding action listeners
        bRegister.setOnClickListener(this);
        tvlogIn.setOnClickListener(this);

        // declaring objects
        proDia= new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();



    }

    @Override
    public void onClick(View v) {
        if(v==bRegister){
            registerUser();
        }
        if(v==tvlogIn){
            Intent logInActivity= new Intent(this,NLogIn.class);
            startActivity(logInActivity);
        }
    }
    public void registerUser(){
        String email,password;
        email= etEmail.getText().toString().trim();
        password=etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            // email is empty
            Toast.makeText(this,"Please enter Email",Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        if(TextUtils.isEmpty(password)){
            // passowrd is empty
            Toast.makeText(this,"Please enter Password",Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        // valid input
        proDia.setMessage("Registering User ...");
        proDia.show();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                proDia.dismiss();
                if(task.isSuccessful()){
                    // user register successfuly
                    // will inform user that he is register
                    Toast.makeText(NRegistration.this,"We sent an email to "+etEmail.getText()+" , Please verify your email address",Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                    FirebaseAuth.getInstance().signOut();
                    // the user email and password are registered successfuly
                    // now we take him into CompleteRegister activity  where the user fill info about him
//                    finish();
//                    startActivity(new Intent(NRegistration.this,CompleteRegisterActivity.class));
                }else{
                    Toast.makeText(NRegistration.this,"This Email is Already Registered!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}