package com.example.kareemkanaan.cardview2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class NavigateByType extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_by_type);
        //mGoogleApiClient = new GoogleApiClient(this);

        // getting references to buttons (if not following option of automatically creating buttons)
        final Button allButton = (Button) findViewById(R.id.choose_all);
        final Button button2 = (Button) findViewById(R.id.choose_type_1);
        final Button button3 = (Button) findViewById(R.id.choose_type_2);
        final Button button4 = (Button) findViewById(R.id.choose_type_3);
        final Button button5 = (Button) findViewById(R.id.choose_type_4);
        final Button othersButton = (Button) findViewById(R.id.choose_others);
        final Button signOut = (Button) findViewById(R.id.signOutButton);

        final Intent intent = new Intent(NavigateByType.this, NavigateByItem.class);
        // add action listener to buttons
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing message to the upcomming activity
                Button b = (Button) v;
                Log.v("message", b.getText().toString());
                intent.putExtra("type", b.getText().toString());
                NavigateByType.this.startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing message to the upcomming activity
                Button b = (Button) v;
                intent.putExtra("type", b.getText());
                NavigateByType.this.startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing message to the upcomming activity
                Button b = (Button) v;
                intent.putExtra("type", b.getText());
                NavigateByType.this.startActivity(intent);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing message to the upcomming activity
                Button b = (Button) v;
                intent.putExtra("type", b.getText());
                NavigateByType.this.startActivity(intent);
            }
        });

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing message to the upcomming activity
                Button b = (Button) v;
                Intent i= new Intent(NavigateByType.this,MainActivity.class);
                NavigateByType.this.startActivity(i);
            }
        });

        othersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passing message to the upcomming activity
                Button b = (Button) v;
                Intent i= new Intent(NavigateByType.this,MainActivity.class);
                i.putExtra("type","Others");
                i.putExtra("item", "Req");
                startActivity(i);

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getApplicationContext(),NLogIn.class);
                                startActivity(i);
                            }
                        });
                finish();
                startActivity(new Intent(NavigateByType.this, NLogIn.class));
            }
        });


    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }


}
