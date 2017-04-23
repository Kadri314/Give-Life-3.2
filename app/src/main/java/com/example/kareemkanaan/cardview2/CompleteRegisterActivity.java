package com.example.kareemkanaan.cardview2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompleteRegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etAge,etPhone,etName;
    private Button bSubmit;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private Spinner citiesSpinner;
    protected void onCreate(Bundle savedInstanceState) {
        // calling a function that check if the user has loged in before. if so it redirects him into MainActivity, otherwise we take his info in this activity then we redirect him
        //checkIfUserFirstTimeRegister();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_register);

        // getting references
        getReference();

        //getCities retrieve the cities from dataBase and add them into the spinner
        getCities();





    }
    public void getReference(){
        etAge=(EditText) findViewById(R.id.etAge);
        etPhone=(EditText) findViewById(R.id.etPhoneNumber);
        etName=(EditText) findViewById(R.id.etFullName);
        etName.requestFocus();
        bSubmit=(Button) findViewById(R.id.btSubmit);
        // adding action listener
        bSubmit.setOnClickListener(this);
        //instintiate objectst
        progressDialog= new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
//        etName.setText(profile.getFirstName()+" "+profile.getLastName());
//        etPhone.setText(profile.getId());
    }

    @Override
    public void onClick(View v) {
        if(v==bSubmit){
            submitData();
        }
    }
    public void submitData(){
        String phone,age,name,location;
        phone=etPhone.getText().toString().trim();
        age=etAge.getText().toString().trim();
        name=etName.getText().toString().trim();
        location= citiesSpinner.getSelectedItem().toString();

        if(TextUtils.isEmpty(name)){
            // passowrd is empty
            Toast.makeText(this,"Please enter Name",Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        if(TextUtils.isEmpty(phone)){
            // passowrd is empty
            Toast.makeText(this,"Please enter Phone",Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        if(TextUtils.isEmpty(age)){
            // passowrd is empty
            Toast.makeText(this,"Please enter age",Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        // valid input submit data to database
        progressDialog.setMessage("Submitting...");
        progressDialog.show();
        UserInformation info= new UserInformation(name,phone,age,location);
        String userID=auth.getCurrentUser().getUid();
        databaseReference.child("Users").child(userID).setValue(info);

        progressDialog.dismiss();
        Toast.makeText(this,"Information Saved!",Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,userID,Toast.LENGTH_SHORT).show();

        // take user into another activity
        finish();
        startActivity(new Intent(CompleteRegisterActivity.this,MainActivity.class));
    }
    public void getFBProfile(){

    }
    public String getUserID(){
        String userID="Test";
        // check if the user logedin by email or facebook
        if(auth.getCurrentUser()==null){
            // means the user loged in by facebok
            userID=Profile.getCurrentProfile().getId();
        }else{
            //means the user loged in by email
            userID=auth.getCurrentUser().getUid();
        }
        return userID;
    }
    public void getCities(){
        progressDialog= new ProgressDialog(CompleteRegisterActivity.this);
        progressDialog.setMessage("Loading . . .");
        progressDialog.show();
         citiesSpinner= (Spinner) findViewById(R.id.spinner);
        final List<String> dataHolder= new ArrayList<String>();
        final ArrayAdapter adp =  new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, dataHolder );

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot child : dataSnapshot.child("Countries").child("Lebanon").getChildren()){
                    dataHolder.add(child.getKey());
                }
                Collections.sort(dataHolder,String.CASE_INSENSITIVE_ORDER);
                adp.notifyDataSetChanged();
                citiesSpinner.setAdapter(adp);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

