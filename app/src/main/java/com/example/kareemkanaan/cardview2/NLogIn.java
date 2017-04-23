package com.example.kareemkanaan.cardview2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NLogIn extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail, etPassword;
    private Button bLogin;
    private TextView tvRegister,tvForgetPassword;
    private FirebaseAuth auth;
    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;
    private Profile profile;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());



        // check if the user already loged in
        checkUserLogIn();

        // setting the layout
        setContentView(R.layout.activity_nlog_in);

        // creating references to the elemnts and initiate objects
        getReferenceAndInstantaite();

        // add action listener
        bLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        fbLoginOnClick();



    }
    public void checkUserLogIn(){
        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // means user already loged in
            // go to another activity
            finish();
            startActivity(new Intent(NLogIn.this, MainActivity.class));
        }
    }
    public void getReferenceAndInstantaite(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogIn);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvForgetPassword= (TextView) findViewById(R.id.tvForgetPassword);
        fbLoginButton=(LoginButton) findViewById(R.id.FBLoginButton);
        //loginButton=(LoginButton) findViewById(R.id.fbLogInButton);
        // instantiate ojects
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        callbackManager=CallbackManager.Factory.create();
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        if (v == tvRegister) {
            Intent registerIntent = new Intent(NLogIn.this, NRegistration.class);
            NLogIn.this.startActivity(registerIntent);
        }
        if (v == bLogin) {
            // we check database if userName and password exist we log user
            signInUser();
        }
        if(v==tvForgetPassword){
            startActivity(new Intent(NLogIn.this,ResetPassword.class));
        }
    }





    public void getFBProfile(){
        ProfileTracker fbProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // User logged in or changed profile
                profile=currentProfile;
            }
        };
    }
    public void signInUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            // passowrd is empty
            Toast.makeText(this, "Please enter Passowrd", Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        if (TextUtils.isEmpty(password)) {
            // name is empty
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        //valid input
        // signing in the user
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(NLogIn.this, "The combination of Email And password are wrong!", Toast.LENGTH_LONG).show();

                } else {
                    //Toast.makeText(NLogIn.this, "Loged In Successfuly", Toast.LENGTH_SHORT).show();
                    if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                        checkIfUserFirstTimeLogingIn();
                    }else{
                        // user didn't verify his email address
                        Toast.makeText(NLogIn.this,"We sent an email verification to "+etEmail.getText()+" , Please verify your email address",Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                        FirebaseAuth.getInstance().signOut();




                    }

                    // user is loged in successfuly
                    // check if this is the first time the user is loggin in  by calling the following function:
                    //startActivity(new Intent(NLogIn.this,MainActivity.class));


                }
            }
        });

    }
    public void checkIfUserFirstTimeLogingIn(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    // now we need to check if this is the first time that the user is loging in
                    boolean isUserKnown=dataSnapshot.child("Users").hasChild(firebaseAuth.getCurrentUser().getUid());
                    if(!isUserKnown){
                        // means this is the first time the user log in into pur app. so we need to store his info
                        // thus we redirect him into CompleteRegisterActivity
                        finish();
                        startActivity(new Intent(NLogIn.this,CompleteRegisterActivity.class));
                    }else{
                        // means the user has loged in before so we had stored his data before \. Thus we move him into MainActivity
                        finish();
                        startActivity(new Intent(NLogIn.this,MainActivity.class));
                    }
                    progressDialog.dismiss();
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    // from here every code segments is related to facebook logIN:

    public void fbLoginOnClick(){
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                }
                handleFacebookAccessToken(loginResult.getAccessToken());
                finish();
                startActivity(new Intent(NLogIn.this,MainActivity.class));

                //checkIfUserFirstTimeLogingIn();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(NLogIn.this,"Something went wrong try again!",Toast.LENGTH_SHORT).show();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("sd", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("sd", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("sd", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("sd", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("sd", "signInWithCredential", task.getException());
                            Toast.makeText(NLogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}



