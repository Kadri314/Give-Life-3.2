package com.example.kareemkanaan.cardview2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ASUS on 4/15/2017.
 * This calss mainly created to read the file LebanonCities.CSV, which is in assets directory
 * and then store each city with its latitude and longitude in remote database firebase
 */

public class TestingDistance extends AppCompatActivity {
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_distance);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        InputStream in= null;
        try {
            in = getAssets().open("LebanonCities.CSV");
            BufferedReader reader= new BufferedReader(new InputStreamReader(in));
            String line=reader.readLine(); // to avoid the first line
            while((line=reader.readLine())!=null){
                String[] lineAsArray= line.split(",");
            String city=lineAsArray[3].replaceAll("\"", "");
            double Lat= Double.parseDouble(lineAsArray[5]);
            double Long= Double.parseDouble(lineAsArray[6]);
            databaseReference.child("Countries").child("Lebanon").child(city).child("Latitude").setValue(Lat);
            databaseReference.child("Countries").child("Lebanon").child(city).child("Longitude").setValue(Long);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        scaneFile.nextLine();scaneFile.nextLine(); // to avoid the first two lines
//        while(scaneFile.hasNextLine()){
//            String line=scaneFile.nextLine();
//            String[] lineAsArray= line.split(",");
//            String city=lineAsArray[3].replaceAll("\"", "");
//            double Lat= Double.parseDouble(lineAsArray[5]);
//            double Long= Double.parseDouble(lineAsArray[6]);
//            databaseReference.child("Countries").child("Lebanon").child(city).child("Latitude").setValue(Lat);
//            databaseReference.child("Countries").child("Lebanon").child(city).child("Longitude").setValue(Long);
//        }

    }
}
