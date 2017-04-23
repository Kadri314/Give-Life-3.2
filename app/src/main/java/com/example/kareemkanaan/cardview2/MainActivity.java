package com.example.kareemkanaan.cardview2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageHelper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataHolder;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<Case> myDataset= new ArrayList<Case>();;
    private Spinner typeSpinner,itemSpinner, sortSpinner;
    private ArrayAdapter<CharSequence> adp1,adp2;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private Button fillterButton;
    public List<Uri>imgs ;
    private ProgressDialog dialog;
    private boolean isImageRetrieved;
    private String type,item; /* type and item will be used for filltering purpose
                               if type and item are passed from NavigateByItem activity, then we
                               must load the data according to the passed item and type, otherwise we load all data from firebase*/
    private boolean navigateAllItem=false; // to be turned  true if the user chose naviagte all items of given type. For example, display all items of appliances
    public static MainActivity mainActivity;
    private City userCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        // adding sort spinner with an action listener
        implementSortSpinner();

        // adding action listener to refresh button
        Button refresh= (Button) findViewById(R.id.resfresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                myDataset.clear();
                mAdapter.notifyDataSetChanged();
                retrieveData();
            }
        });

        // checking if type, and main are passed from NavigateByItem Activity
        if(getIntent().hasExtra("type")){
            Intent intent= getIntent();
            this.type=intent.getExtras().getString("type");
            this.item=intent.getExtras().getString("item");
            if(item.equals("All")) navigateAllItem=true;
        }else{
            // means no parameter passed from nNavigateByItem Activity
            this.type="AllRequests";
            this.item="Req";
            navigateAllItem=false;
        }

        // adding action listener to filter button :
        fillterButton= (Button) findViewById(R.id.fillterButton);
        fillterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NavigateByType.class));
            }
        });



        // constructing the recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset,MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        // retreiving data from firebase:
        retrieveData();
        //adding action listener on clicking on of the card view
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String s = (new Gson().toJson(myDataset.get(position)));
                        Intent intent = new Intent(getApplicationContext(),CaseDetails.class);
                        intent.putExtra("case",s);
                        intent.putExtra("type",type);
                        intent.putExtra("item",item);
                        startActivity(intent);

                    }
                })
        );
        //adding action lsitener to openCase button
        Button openCaseButton =(Button) findViewById(R.id.openCaseButton);
        openCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(view.getContext(), OpenCase.class);
                startActivity(Intent);
            }
        });
        // initilizing userCity from fireBase
        initilizingUserCityFromFireBase();



    }
    public void implementSortSpinner(){
        sortSpinner= (Spinner) findViewById(R.id.sortSpinner) ;
        adp1 = ArrayAdapter.createFromResource(MainActivity.this,R.array.sort,android.R.layout.simple_list_item_1);
        sortSpinner.setAdapter(adp1);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    // user chose to sort by Date ASC
                    Collections.sort(myDataset, new Case.DateAsc());
                }else if(position==1){
                    // user chose to sort by Date DESC
                    Collections.sort(myDataset, new Case.DateDesc());
                }else if(position==2){
                    // user chose to sort by Location
                   Double[] location= getCurrentLocation();
                    if(location!=null){
                        // means the user has enabled his location
                        userCity.setLatitude(location[1]);
                        userCity.setLongitude(location[0]);
                        // if location== null then we sort data according to THE DEFAULT  location of the user( how it's stored in firebase)
                    }
                    Case.compareDistance.myLatitude=userCity.getLatitude();
                    Case.compareDistance.myLongitude=userCity.getLongitude();

                    Collections.sort(myDataset, new Case.compareDistance());
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void initilizingUserCityFromFireBase(){
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String cityName=dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("location").getValue(String.class);
                double latitude=dataSnapshot.child("Countries").child("Lebanon").child(cityName).child("Latitude").getValue(Double.class);
                double longitude=dataSnapshot.child("Countries").child("Lebanon").child(cityName).child("Longitude").getValue(Double.class);
                MainActivity.this.userCity= new City(cityName,latitude,longitude);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void retrieveData(){
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                List<String> dataHolder= new ArrayList<String>();
                if(!navigateAllItem){
                    // storing the data into dataHolder:
                    for(DataSnapshot parent: dataSnapshot.child("Requests").child(type).child(item).getChildren()){
                        String caseId=parent.getKey();
                        for(DataSnapshot child: parent.getChildren()){
                            dataHolder.add((String)child.getValue());
                        }
                        dataHolder.add(caseId);
                    }
                    // create new cases and add it to myDataSet
                    createNewCase(dataHolder);
                }
                else{
                    // user wants to navigate all items of the  given type, for examle navigate everything inside appliances
                    // storing the data into dataHolder:
                    for(DataSnapshot parent: dataSnapshot.child("Requests").child(type).getChildren()){
                        for(DataSnapshot child: parent.getChildren()){
                            String caseId=child.getKey();
                            for(DataSnapshot child2: child.getChildren()){
                                dataHolder.add((String)child2.getValue());
                            }
                            dataHolder.add(caseId);
                        }
                    }
                    // create new cases and add it to myDataSet
                    createNewCase(dataHolder);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void check(List<String> d){

    }
    public void createNewCase(final List<String> dataHolder){
        if(dataHolder.size()==0){
            Toast.makeText(MainActivity.this,"This category has no requests Yet",Toast.LENGTH_LONG).show();

        }
        else{
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // dataHolder=[Description1,ImgsSource1,Location1,Needs1,Phone#1,Title1,DateOfPost1,caseId1,
                    //             Description2,ImgsSource2,Location2,Needs2,Phone#2,Title2,DateOfPost2,caseId2]; // according to how data are
                    String dateOfPost, des,imgsSource,location,needs,phone,title,caseId;
                    for(int i=0;i<dataHolder.size(); i+=8){
                        des=dataHolder.get(i);imgsSource=dataHolder.get(1+i);location=dataHolder.get(2+i);needs=dataHolder.get(3+i);
                        phone=dataHolder.get(4+i);title=dataHolder.get(5+i);dateOfPost=dataHolder.get(i+6);caseId=dataHolder.get(i+7);
                        // we will get the location's latitude and longitude from fireBase accodring to location's value
                        double latitude=dataSnapshot.child("Countries").child("Lebanon").child(location).child("Latitude").getValue(Double.class);
                        double longitude=dataSnapshot.child("Countries").child("Lebanon").child(location).child("Longitude").getValue(Double.class);
                        // imgsSources will have the following formate: soruce1,source2,source3....
                        String[] imgsSourceArray= imgsSource.split(",");
                        // but for this activity we will use only the first img:  imgsSourceArray[0]
                        String filePath=imgsSourceArray[0];
                        // getting image uri from firebase and adding it to the myDataSet by calling the function getImgUri(filePath)
                        getImgUri(filePath,dateOfPost,des,location,needs,phone,title,caseId,latitude,longitude);
                    }
                }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
            });

        }

    }



    public void getImgUri(final String  filePath,final String dateOfPost, final String des, final String location, final String needs, final String phone, final String title,final String caseId,final double lattude,final double longitude){
        storageReference.child("images").child(filePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                City city= new City(location,lattude,longitude);
                myDataset.add(new Case(dateOfPost,des,uri.toString(),city,needs,phone,title,caseId));
                Collections.sort(myDataset, new Case.DateDesc());
                mAdapter.notifyDataSetChanged();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.v("321321","321312");
                //Toast.makeText(MainActivity.this,"Image failed to get the img",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public Double[] getCurrentLocation(){
        Double lon = null;
        Double lat = null;
        Double alt = null;
        try {
            GpsTool gpsTool = new GpsTool(this);
            Location location = gpsTool.getLocation();

            lon = location.getLongitude();
            lat = location.getLatitude();
            alt = location.getAltitude();

        } catch (Exception e) {
            // This will catch any exception, because they are all descended from Exception
            Toast.makeText(MainActivity.this,"Enable Location", Toast.LENGTH_LONG).show();
            return null;
        }
        // means location is enabled
        Double[] loc = {lon, lat, alt};
        return loc;

    }
//    public void filterButtonBySpinners(){
//        fillterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(typeSpinner.getVisibility()==View.VISIBLE){
//                    typeSpinner.setVisibility(View.GONE);
//                    itemSpinner.setVisibility(View.GONE);
//                } else typeSpinner.setVisibility(View.VISIBLE);
//            }
//        });
//        // initializing item and type spinners and
//        addValuetoSpinner();
//    }
//    public void addValuetoSpinner() {
//        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
//        itemSpinner = (Spinner) findViewById(R.id.itemSpinner);
//
//        adp1 = ArrayAdapter.createFromResource(MainActivity.this,R.array.mainitems,android.R.layout.simple_list_item_1);
//        adp2=ArrayAdapter.createFromResource(MainActivity.this,R.array.Appliances,android.R.layout.simple_list_item_1);
//        typeSpinner.setAdapter(adp1);
//        itemSpinner.setAdapter(adp2);
//
//        // adding action listener to typeSpinner
//        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position==0){
//                    adp2=ArrayAdapter.createFromResource(MainActivity.this,R.array.Home,android.R.layout.simple_list_item_1);
//
//                }else if(position==1){
//                    myDataset.remove(0);
//                    if(mAdapter!=null)
//                        mAdapter.update(myDataset);
//                    adp2=ArrayAdapter.createFromResource(MainActivity.this,R.array.Clothes,android.R.layout.simple_list_item_1);
//                }else if(position==2){
//                    adp2=ArrayAdapter.createFromResource(MainActivity.this,R.array.Appliances,android.R.layout.simple_list_item_1);
//                }else if(position==3){
//                    adp2=ArrayAdapter.createFromResource(MainActivity.this,R.array.Books,android.R.layout.simple_list_item_1);
//                }else if(position==4){
//                    adp2=ArrayAdapter.createFromResource(MainActivity.this,R.array.Medication,android.R.layout.simple_list_item_1);
//                }else{
//                    adp2=ArrayAdapter.createFromResource(MainActivity.this,R.array.Toys,android.R.layout.simple_list_item_1);
//                }
//                itemSpinner.setAdapter(adp2);
//                itemSpinner.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//
//    }
}