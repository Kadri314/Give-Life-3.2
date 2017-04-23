package com.example.kareemkanaan.cardview2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Button reSet= (Button) findViewById(R.id.button9);
        final EditText email= (EditText) findViewById(R.id.etEmail);
        reSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(String.valueOf(email.getText()));
                Toast.makeText(ResetPassword.this,"We a password Reset to "+email.getText(),Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(ResetPassword.this,NLogIn.class));
            }
        });

    }
}
