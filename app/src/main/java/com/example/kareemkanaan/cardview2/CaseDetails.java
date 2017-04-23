package com.example.kareemkanaan.cardview2;

import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CaseDetails extends AppCompatActivity {

   /* CostumSwipeAdapter adapter;
    ViewPager viewPager;*/
   private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private  Case caseObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details);

        // getting reference:
        TextView title= (TextView) findViewById(R.id.textView11);
        TextView location= (TextView) findViewById(R.id.textView9);
        TextView description= (TextView) findViewById(R.id.textView);
        TextView needs= (TextView) findViewById(R.id.textView7);
        TextView phone= (TextView) findViewById(R.id.textView5);

        // getting messages that are passed from mainAcitivty :
        Bundle bundle =getIntent().getExtras();
        final String type=bundle.getString("type");
        final String item=bundle.getString("item");
        String CaseAsJsonFormate= bundle.getString("case");
        caseObject= new Gson().fromJson(CaseAsJsonFormate, Case.class);
        // adding action listner into phone and message buttons :
        phoneAndMessage();

        //  initilizing text views from caseObject, which is passed from mainActivity
        title.setText(caseObject.getTitle());
        location.setText(caseObject.getCity().getCityName());
        description.setText(caseObject.getDescription());
        needs.setText(caseObject.getNeeds());
        phone.setText(caseObject.getPhoneNumber());

        final String caseId=caseObject.getCaseId();
        final List<Uri> URIlist= new ArrayList<>();
        // adding the first img uri to URIlist. First img uri is passed as parameter from mainActitivty
        URIlist.add(Uri.parse(caseObject.getImgURIAsString()));

        // getting imgs source from firebase according to the caseId
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgsSources=dataSnapshot.child("Requests").child(type).child(item).child(caseId).child("ImgSource").getValue(String.class);
                // imgsSources will have the form: source1,source2
                String[] imgsArray=imgsSources.split(",");
                if(imgsArray.length==1){
                    // there is only one img
                    displayImgOnSwiper(URIlist);
                }
                else{
                    // we need to retreive the uri for the second img
                    // first img uri already passed as parameter from Mainactivity
                    storageReference=FirebaseStorage.getInstance().getReference();
                    storageReference.child("images").child(imgsArray[1]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            URIlist.add(uri);
                            displayImgOnSwiper(URIlist);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.v("321321","321312");
                            //Toast.makeText(MainActivity.this,"Image failed to get the img",Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void displayImgOnSwiper(List<Uri> URIlist){
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        if(URIlist.size()==2){
            CostumSwipeAdapter adapter = new CostumSwipeAdapter(this,URIlist);
            viewPager.setAdapter(adapter);
        }else{
            // user only uploaded one img in this case
            // make the second img same as the first img
            URIlist.add(Uri.parse(caseObject.getImgURIAsString()));
            CostumSwipeAdapter adapter = new CostumSwipeAdapter(this,URIlist);
            viewPager.setAdapter(adapter);

        }
    }
    public void phoneAndMessage(){
        // adding action listener into phone button
        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String phoneNb = caseObject.getPhoneNumber();
                callIntent.setData(Uri.parse("tel:" + phoneNb) );

                startActivity(callIntent);
            }
        });
        // adding action listener into message button
        findViewById(R.id.msg).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", caseObject.getPhoneNumber(), null));
                startActivity(intent);
            }
        });
    }


}