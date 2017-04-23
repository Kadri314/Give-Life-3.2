package com.example.kareemkanaan.cardview2;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class OpenCase extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private Button add1,add2,submit,addImg,addImg2;
    private EditText etTitle,etDescription,etNumber,typeEditText1, itemEditText1,typeEditText2, itemEditText2;
    private StorageReference storageReference;
    static final int MAX_ROWS=2, GALLARY_INTENT=2121,GALLARY_INTENT2=123;
    private int rowsNumber=1;
    private Uri imgUrl,imgUrl2;
    private ProgressDialog progressDialog;
    private String type1,item1,type2,item2;
    private TableLayout table;
    private TableRow tableRow2;
    private Spinner numberOfNeedsSpinner, citiesSpinner;


    public void saveState(Intent i){
        i.putExtra("type1",typeEditText1.getText().toString());
        i.putExtra("type2",typeEditText2.getText().toString());
        i.putExtra("item1",itemEditText1.getText().toString());
        i.putExtra("item2",itemEditText2.getText().toString());
        i.putExtra("title",etTitle.getText().toString());
        i.putExtra("phone",etNumber.getText().toString());
        i.putExtra("location",citiesSpinner.getSelectedItemPosition());
        i.putExtra("description",etDescription.getText().toString());
        i.putExtra("addImgButton1",addImg.getText().toString());
        i.putExtra("addImgButton2",addImg2.getText().toString());
        i.putExtra("position",numberOfNeedsSpinner.getSelectedItemPosition());

        i.putExtra("rowsNumber",rowsNumber);
        if(imgUrl!=null){
            i.putExtra("imgUrl",imgUrl.toString());
        }else{
            i.putExtra("imgUrl","null1");

        }
        if(imgUrl2!=null){
            i.putExtra("imgUrl2",imgUrl2.toString());
        }else{
            i.putExtra("imgUrl2","null1");
        }

    }
    public void retreiveState(){
        Bundle savedInstanceState= getIntent().getExtras();
        type1=savedInstanceState.getString("type1");
        type2=savedInstanceState.getString("type2");
        item1=savedInstanceState.getString("item1");
        item2=savedInstanceState.getString("item2");
        itemEditText1.setText(item1);
        itemEditText2.setText(item2);
        typeEditText1.setText(type1);
        typeEditText2.setText(type2);
        etTitle.setText(savedInstanceState.getString("title"));
        etNumber.setText(savedInstanceState.getString("phone"));
        citiesSpinner.setSelection(savedInstanceState.getInt("location"));
        etDescription.setText(savedInstanceState.getString("description"));
        addImg.setText(savedInstanceState.getString("addImgButton1"));
        addImg2.setText(savedInstanceState.getString("addImgButton2"));
        rowsNumber=savedInstanceState.getInt("rowsNumber");
        numberOfNeedsSpinner.setSelection(savedInstanceState.getInt("position"));


        if(!savedInstanceState.getString("imgUrl").equalsIgnoreCase("null1")){
            imgUrl=Uri.parse(savedInstanceState.getString("imgUrl"));
        }
        if(!savedInstanceState.getString("imgUrl2").equalsIgnoreCase("null1")){
            imgUrl2=Uri.parse(savedInstanceState.getString("imgUrl2"));

        }
        if(!item1.equalsIgnoreCase("Others"))itemEditText1.setEnabled(false);
        else itemEditText1.setEnabled(true);
        if(!item2.equalsIgnoreCase("Others"))itemEditText1.setEnabled(false);
        else itemEditText1.setEnabled(true);

    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_case);

        // getting references to the objects:
        getReference();
        // set user Default phone number inside the editText
            setUserPhoneNumber();
        // get the cities from dataBase and add them into the cities' Spinner
        getCities();


        // add actionListiner into numberOfNeedsSpinner
        handleListinerSPinner();

        if(getIntent().getExtras()!=null){
            retreiveState();
            // this method checkMessages check the type and item that are passed from the choose_item activity
            checkMessages();
        }

        // adding action listener to submit Button:
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
                //finish();
            }
        });

        // adding action listener to add Button1:
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(OpenCase.this,Choose_Type.class);
                i.putExtra("add1","add1");
                saveState(i);
                startActivity(i);
            }
        });
        // adding action listener to add2 Button1:
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(OpenCase.this,Choose_Type.class);
                i.putExtra("add2","add2");
                saveState(i);
                startActivity(i);
            }
        });
        // adding action listener to addImg Button
        addImg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLARY_INTENT);
            }
        });
        // adding action listener to addImg2 Button
        addImg2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLARY_INTENT2);
            }
        });


    }
    public void handleListinerSPinner(){
        // adding action listener to  numberOfNeedsSpinner
        numberOfNeedsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    // user wants to post only one needs
                    //Hence, we need to remove the second row from the table
                    tableRow2.setVisibility(View.GONE);
                    rowsNumber=1;

                }
                if(position==1){
                    // user wants to post two needs so we add a column
                    // Hence, we need to add a second row
                    rowsNumber=2;
                    tableRow2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void checkMessages(){
        if(getIntent().getExtras()!=null){
            // a massege is passed to this activity
            Intent i = getIntent();
            if(i.hasExtra("add1")){
                type1=i.getExtras().getString("type");
                typeEditText1.setText(type1);
                Log.v("sdsdsdsd",type1);
                if(type1.equalsIgnoreCase("Others")){
                    itemEditText1.setEnabled(true);
                    itemEditText1.setHint("Your Need?");
                }else{
                    item1=i.getExtras().getString("item");
                    itemEditText1.setText(item1);
                }

            }
            if(i.hasExtra("add2")){
                type2=i.getExtras().getString("type");
                typeEditText2.setText(type2);
                if(type2.equalsIgnoreCase("Others")){
                    itemEditText2.setEnabled(true);
                    itemEditText2.setHint("Your Need?");
                }else{
                    item2=i.getExtras().getString("item");
                    itemEditText2.setText(item2);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLARY_INTENT && resultCode==RESULT_OK){

            imgUrl= data.getData();
            addImg.setText("Image Selected. Re-Select?");
        }
        if(requestCode==GALLARY_INTENT2 && resultCode==RESULT_OK){
            imgUrl2= data.getData();
            addImg2.setText("Image Selected. Re-Select?");
        }
    }

    // this method add another Row


    public void getReference(){
        submit = (Button)findViewById(R.id.submit);
        typeEditText1= (EditText) findViewById(R.id.typeEditText) ; typeEditText1.setEnabled(false);
        itemEditText1= (EditText) findViewById(R.id.itemEditText); itemEditText1.setEnabled(false);
        typeEditText2= (EditText) findViewById(R.id.typeEditText2) ; typeEditText2.setEnabled(false);
        itemEditText2= (EditText) findViewById(R.id.itemEditText2); itemEditText2.setEnabled(false);
        tableRow2=(TableRow) findViewById(R.id.row2);         tableRow2.setVisibility(View.GONE);

        progressDialog= new ProgressDialog(OpenCase.this);
        add1 = (Button)findViewById(R.id.add1);
        add2 = (Button)findViewById(R.id.add2);
        etTitle=(EditText) findViewById(R.id.titletext);
        etTitle.requestFocus();
        etDescription=(EditText) findViewById(R.id.description);
        etNumber=(EditText) findViewById(R.id.numbertext);
        citiesSpinner= (Spinner) findViewById(R.id.citiesSpinner);
        addImg=(Button)findViewById(R.id.addImg);
        addImg2=(Button)findViewById(R.id.addImg2);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference();
            // initiate  numberOfNeedsSpinner and adding arrayAdapter to it
            numberOfNeedsSpinner= (Spinner) findViewById(R.id.spinnerNumberOfNeeds);
            ArrayAdapter<CharSequence> adpt=  ArrayAdapter.createFromResource(OpenCase.this, R.array.numberOfNeeds, android.R.layout.simple_list_item_1);
            numberOfNeedsSpinner.setAdapter(adpt);




    }

    public void submitData() {
        String title, des, location, needs, phone;
        title = etTitle.getText().toString().trim();
        des = etDescription.getText().toString().trim();
        location = citiesSpinner.getSelectedItem().toString().trim();
        phone = etNumber.getText().toString().trim();
        needs = null;
        Log.v("LOOL",rowsNumber+"");
        // initializing needs

        // checking if all info are inserted by the user
        if (TextUtils.isEmpty(title)) {
            // email is empty
            Toast.makeText(this, "Please enter Title", Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            // email is empty
            Toast.makeText(this, "Please enter Phone", Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        if (TextUtils.isEmpty(location)) {
            // email is empty
            Toast.makeText(this, "Please enter Location", Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }
        if (TextUtils.isEmpty(des)) {
            // email is empty
            Toast.makeText(this, "Please enter Description", Toast.LENGTH_SHORT).show();
            // stoping executing the function
            return;
        }

        if (imgUrl == null && imgUrl2 == null) {
            // user Didn't upload an img
            Toast.makeText(this, "Please Upload atleast one image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!itemEditText1.isEnabled()) {
            if (TextUtils.isEmpty(type1)) {

                Toast.makeText(this, "Please Select Your  Need ", Toast.LENGTH_SHORT).show();
                // stoping executing the function
                return;
            }
        }
        else {
            if (TextUtils.isEmpty(itemEditText1.getText())) {
                Toast.makeText(this, "Please  specify your needs! ", Toast.LENGTH_SHORT).show();
                // stoping executing the function
                return;
            }
        }
            if (rowsNumber == 2) {
                if (!itemEditText2.isEnabled()) {
                    if (TextUtils.isEmpty(item2) || TextUtils.isEmpty(type2)) {
                        Toast.makeText(this, "Please Select Your second Needs ", Toast.LENGTH_SHORT).show();
                        // stoping executing the function
                        return;
                    }
                }
                else {
                    if (TextUtils.isEmpty(itemEditText2.getText())) {
                        Toast.makeText(this, "Please  specify your needs! ", Toast.LENGTH_SHORT).show();
                        // stoping executing the function
                        return;
                    }
                }

            }

            // if we reach here the data are correct


            progressDialog.setMessage("Posting ...");
            progressDialog.show();
            // what left add image then insert data into all and
            //1- uploading image to FireBase
            String imgUniqueName, imgUniqueName2, imgsName = null;
            if (imgUrl != null && imgUrl2 != null) {
                imgUniqueName = uploadImgToFireBase(imgUrl);
                imgUniqueName2 = uploadImgToFireBase(imgUrl2);
                imgsName = imgUniqueName + "," + imgUniqueName2;
            } else if (imgUrl != null) {
                imgUniqueName = uploadImgToFireBase(imgUrl);
                imgsName = imgUniqueName;
            } else {
                imgUniqueName2 = uploadImgToFireBase(imgUrl2);
                imgsName = imgUniqueName2;
            }
            if (imgsName == null) {
                //means something fail to connect to the server and upload img so we cancel uploading
                Toast.makeText(this, "Please Try Again!", Toast.LENGTH_SHORT).show();
                return;
            }
            // if we reach here then image has been uploaded successfuly

            //2- storing Data in CaseData object
            CaseData case1 = new CaseData(title, phone, location, des, imgsName, needs);
            //3- storing case Data in firebase under path: root/Requests/type/item/
            String tempItem1 = null;
            String tempItem2 = null;
            //a- uploading the firt need
        if (type1.equalsIgnoreCase("Others")) {
                // means the user chose others as type so we have to put items to Req: According to firebase
                tempItem1 = "Req";
            }else{
                tempItem1=item1;
            }
            case1.Needs=itemEditText1.getText().toString();
            databaseReference.child("Requests").child(type1).child(tempItem1).push().setValue(case1);
            //b- check if there is second needs then we proccess uploading second needs
            if (rowsNumber == 2) {
                if (type2.equalsIgnoreCase("others")) {
                    // means the user chose others as type so we have to put items to Req: According to firebase
                    tempItem2 = "Req";
                }else{
                    tempItem2=item2;
                }
                case1.Needs=itemEditText2.getText().toString();
                databaseReference.child("Requests").child(type2).child(tempItem2).push().setValue(case1);
            }

            //4- storing Case data in firebase under path root/requests/AllRequests/
            if(rowsNumber==2) case1.Needs=itemEditText1.getText().toString()+", "+ itemEditText2.getText().toString();
            databaseReference.child("Requests").child("AllRequests").child("Req").push().setValue(case1);
            // done From posting data
            progressDialog.dismiss();
            Toast.makeText(OpenCase.this, "Posted Successfuly!", Toast.LENGTH_SHORT).show();
            finish();
            MainActivity.mainActivity.finish();
            startActivity(new Intent(OpenCase.this, MainActivity.class));


    }

    public String uploadImgToFireBase(Uri imgCurrentURI) {

        String imgUniqueName = databaseReference.push().getKey();
        StorageReference filePath = storageReference.child("images").child(imgUniqueName);

        filePath.putFile(imgCurrentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Toast.makeText(OpenCase.this,"Image Uploaded Successfuly",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OpenCase.this, "Something Went Wrong with Img uploading try again please!", Toast.LENGTH_SHORT).show();

                return;
            }
        });
        return imgUniqueName;
    }

    public void setUserPhoneNumber() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String phoneNumber = dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).child("phone").getValue(String.class);
                etNumber.setText(phoneNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getCities(){
        progressDialog= new ProgressDialog(OpenCase.this);
        progressDialog.setMessage("Loading . . .");
        progressDialog.show();
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
                if(getIntent().getExtras()!=null){
                    citiesSpinner.setSelection(getIntent().getExtras().getInt("location"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}