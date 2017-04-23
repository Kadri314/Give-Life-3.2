package com.example.kareemkanaan.cardview2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Choose_Type extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__type);

        // getting references to buttons (if not following option of automatically creating buttons)
        final Button button1 = (Button) findViewById(R.id.choose_all);
        final Button button2 = (Button) findViewById(R.id.choose_type_1);
        final Button button3 = (Button) findViewById(R.id.choose_type_2);
        final Button button4 = (Button) findViewById(R.id.choose_type_3);
        final Button button5 = (Button) findViewById(R.id.choose_type_4);
        final Button button6 = (Button) findViewById(R.id.choose_others);
        final List<String> dataHolder= new ArrayList<String>();




        //preparte to send message  into chooseItem Activity
        final Intent intent = new Intent(Choose_Type.this, Choose_Item.class);
        // get the message the is sent from OpenCase Activity
        // send a message into ChooseItem ACtivity
        saveState(intent);

        // add action listener to buttons

        // button1 is others button
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Choose_Type.this, OpenCase.class);
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("type",b.getText().toString());
                Choose_Type.this.startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("type",b.getText());
                Choose_Type.this.startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("type",b.getText());
                Choose_Type.this.startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("type",b.getText());
                Choose_Type.this.startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("type",b.getText());
                Choose_Type.this.startActivity(intent);
            }
        });
        // other
        button6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Intent i = new Intent(Choose_Type.this, OpenCase.class);
                saveState(i);
                // passing message to the upcomming activity
                Button b=(Button)v;
                i.putExtra("type",b.getText().toString());
                Choose_Type.this.startActivity(i);
            }
        });
    }

    public void saveState(Intent i){
        Bundle savedInstanceState= getIntent().getExtras();
        i.putExtra("type1",savedInstanceState.getString("type1"));
        i.putExtra("type2",savedInstanceState.getString("type2"));
        i.putExtra("item1",savedInstanceState.getString("item1"));
        i.putExtra("item2",savedInstanceState.getString("item2"));
        i.putExtra("title",savedInstanceState.getString("title"));
        i.putExtra("phone",savedInstanceState.getString("phone"));
        i.putExtra("location",savedInstanceState.getInt("location"));
        i.putExtra("description",savedInstanceState.getString("description"));
        i.putExtra("addImgButton1",savedInstanceState.getString("addImgButton1"));
        i.putExtra("addImgButton2",savedInstanceState.getString("addImgButton2"));
        i.putExtra("position",savedInstanceState.getInt("position"));
        i.putExtra("rowsNumber",savedInstanceState.getInt("rowsNumber"));
        i.putExtra("imgUrl",savedInstanceState.getString("imgUrl"));
        i.putExtra("imgUrl2",savedInstanceState.getString("imgUrl2"));
        if(getIntent().hasExtra("add1")){
            //add1 button is clicked from openCase activity
            i.putExtra("add1",savedInstanceState.getString("add1"));

        }else{
            //add2 button is clicked from openCase activity
            i.putExtra("add2",savedInstanceState.getString("add2"));
        }


    }
}

