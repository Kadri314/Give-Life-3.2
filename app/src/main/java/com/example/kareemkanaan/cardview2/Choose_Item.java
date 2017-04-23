package com.example.kareemkanaan.cardview2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Choose_Item extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__item);

        // getting references to buttons (if not following option of automatically creating buttons)
        final Button all = (Button) findViewById(R.id.choose_all);
        final Button item_1 = (Button) findViewById(R.id.choose_item_1);
        final Button item_2 = (Button) findViewById(R.id.choose_item_2);
        final Button item_3 = (Button) findViewById(R.id.choose_item_3);
        final Button item_4 = (Button) findViewById(R.id.choose_item_4);
        final Button item_5 = (Button) findViewById(R.id.choose_item_5);


        // getting the message which was sent from openCase Acitivity
        final String type = getIntent().getExtras().getString("type");
        // getting the message which was sent from openCase Acitivity






        String[] appliances = {"Oven", "Refrigerator", "Stove", "TV", "Washer"};
        String[] books = {"Drama", "Education", "Mystery", "Romance", "Science"};
        String[] clothes = {"Pants", "Shirt", "Sports", "T-Shirts", "Underwear"};
        String[] home = {"Bathroom", "Bed", "Couches", "Kitchen", "Table"};

        String[] dataHolder = null;
        if(type.equals("appliances")) {
            dataHolder = appliances;
        }else if(type.equals("books")) {
            dataHolder = books;
        }else if(type.equals("clothes")) {
            dataHolder = clothes;
        }else if(type.equals("home")) {
            dataHolder = home;
        }
        all.setText("All");
        item_1.setText(dataHolder[0]);
        item_2.setText(dataHolder[1]);
        item_3.setText(dataHolder[2]);
        item_4.setText(dataHolder[3]);
        item_5.setText(dataHolder[4]);

        //preparte to send message  into openCase Activity
        final Intent intent = new Intent(Choose_Item.this, OpenCase.class);
        int rowsNumber = getIntent().getExtras().getInt("rowsNumber");
        saveState(intent);
        // send message into openCase Activity
        intent.putExtra("type",type);
        Log.v("sese",rowsNumber+"");
        intent.putExtra("NeedNumber"+rowsNumber,rowsNumber);

        // add action listener to buttons
        all.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                Choose_Item.this.startActivity(intent);
            }
        });

        item_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                Choose_Item.this.startActivity(intent);
            }
        });

        item_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                Choose_Item.this.startActivity(intent);
            }
        });

        item_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                Choose_Item.this.startActivity(intent);
            }
        });
        item_4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                Choose_Item.this.startActivity(intent);
            }
        });

        item_5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                Choose_Item.this.startActivity(intent);
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
            Log.v("Ssss",savedInstanceState.getString("add1"));

        }else{
            //add2 button is clicked from openCase activity
            i.putExtra("add2",savedInstanceState.getString("add2"));
            Log.v("Ssss",savedInstanceState.getString("item1"));
            Log.v("Ssss",savedInstanceState.getString("type1"));


        }

    }
}
