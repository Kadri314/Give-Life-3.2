package com.example.kareemkanaan.cardview2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FBLogIn extends AppCompatActivity {
    private static final int RC_SIGN_IN=0;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblog_in);
        firebaseAuth=FirebaseAuth.getInstance().getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        //check if user is already loged in
        if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }else{
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setProviders(
                    AuthUI.FACEBOOK_PROVIDER,
                    AuthUI.GOOGLE_PROVIDER,
                    AuthUI.EMAIL_PROVIDER)
                    .build(),RC_SIGN_IN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                // user Logged In
                Log.v("jajaja"," Authenticated ");
                // calling a function that check if the user has loged in before. if so it redirects him into MainActivity, otherwise we take user;s info from CompleteRegisterActivity
                checkIfUserFirstTimeRegister();
            }
            else{
                // user Not Authentecated
                Log.v("jajaja","Not Authenticated ");
            }
        }
    }
    public void checkIfUserFirstTimeRegister(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(firebaseAuth.getCurrentUser()!=null){
                    // now we need to check if this is the first time that the user is loging in
                    boolean isUserKnown=dataSnapshot.child("Users").hasChild(firebaseAuth.getCurrentUser().getUid());
                    if(!isUserKnown){
                        // means this is the first time the user log in into pur app. so we need to store his info
                        // thus we redirect him into CompleteRegisterActivity
                        finish();
                        startActivity(new Intent(FBLogIn.this,CompleteRegisterActivity.class));


                    }else{
                        // means the user has loged in before so we had stored his data before \. Thus we move him into MainActivity
                        finish();
                        startActivity(new Intent(FBLogIn.this,MainActivity.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
