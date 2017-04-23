package com.example.kareemkanaan.cardview2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class NavigateByItem extends AppCompatActivity {
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_by_item);


        // getting references to buttons (if not following option of automatically creating buttons)
        final Button all = (Button) findViewById(R.id.choose_all);
        final Button item_1 = (Button) findViewById(R.id.choose_type_1);
        final Button item_2 = (Button) findViewById(R.id.choose_type_2);
        final Button item_3 = (Button) findViewById(R.id.choose_type_3);
        final Button item_4 = (Button) findViewById(R.id.choose_type_4);
        final Button item_5 = (Button) findViewById(R.id.choose_item_5);

        TextView header= (TextView) findViewById(R.id.textView2);

        // getting the message which was sent from NavigateByType Acitivity
        final String type = getIntent().getExtras().getString("type");
        header.setText(type);
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

        //preparte to send message  into MainActivity
        final Intent intent = new Intent(NavigateByItem.this, MainActivity.class);
        intent.putExtra("type",type);

        // add action listener to buttons
        all.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                NavigateByItem.this.startActivity(intent);
            }
        });

        item_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                NavigateByItem.this.startActivity(intent);
            }
        });

        item_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                NavigateByItem.this.startActivity(intent);
            }
        });

        item_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                NavigateByItem.this.startActivity(intent);
            }
        });
        item_4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                NavigateByItem.this.startActivity(intent);
            }
        });

        item_5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // passing message to the upcomming activity
                Button b=(Button)v;
                intent.putExtra("item",b.getText());
                NavigateByItem.this.startActivity(intent);
            }
        });

    }
}

