package com.example.kareemkanaan.cardview2;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by kareemkanaan on 3/4/17.
 * This class is used for the Mainactivity for myAdapter<Case>
 */

@IgnoreExtraProperties
public class Case /*implements Parcelable*/{
    public String Title;
    public String imgURIAsString;
    public City city;
    public String Description;
    public String Needs;
    public String PhoneNumber;
    public String caseId;
    public String DateOfPost;
    public String ImgSource;



    public Case(String DateOfPost,String Description, String imgURIAsString, City Location, String Needs, String PhoneNumber, String Title, String caseId) {
        this.Title = Title;
        this.imgURIAsString = imgURIAsString;
        this.city = Location;
        this.Needs = Needs;
        this.Description = Description;
        this.PhoneNumber=PhoneNumber;
        this.DateOfPost=DateOfPost;
        this.caseId=caseId;

    }
    public Case(){

    }
    // setters
    public void setTitle(String title) {
        Title = title;
    }

    public void setImgURIAsString(String imgURIAsString) {
        this.imgURIAsString = imgURIAsString;
    }

    public void setLocation(City city) {
        this.city = city;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setNeeds(String needs) {
        Needs = needs;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public void setDateOfPost(String dateOfPost) {
        DateOfPost = dateOfPost;
    }

    public void setImgSource(String imgSource) {
        ImgSource = imgSource;
    }

    //getters
    public String getTitle() {
        return Title;
    }

    public String getImgURIAsString() {
        return imgURIAsString;
    }

    public City getCity() {
        return city;
    }

    public String getDescription() {
        return Description;
    }

    public String getNeeds() {
        return Needs;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getDateOfPost() {
        return DateOfPost;
    }

    public String getImgSource() {
        return ImgSource;
    }

    public static class DateAsc implements Comparator<Case>{

        @Override
        public int compare(Case o1, Case o2) {
            return o1.DateOfPost.compareTo(o2.DateOfPost);
        }
    }
    public static class DateDesc implements Comparator<Case>{

        @Override
        public int compare(Case o1, Case o2) {
            return o1.DateOfPost.compareTo(o2.DateOfPost)*-1;
        }
    }



    public static class compareDistance implements Comparator<Case>{
        // myLatitude and myLongitude to be specified according to the user's location
        public static double myLatitude=0;
        public static double myLongitude=0;
        @Override
        public int compare(Case o, Case o2) {
            float[] result1 = new float[3];
            android.location.Location.distanceBetween(myLatitude, myLongitude, o.getCity().getLatitude(), o.getCity().getLongitude(), result1);
            Float distance1 = result1[0];

            float[] result2 = new float[3];
            android.location.Location.distanceBetween(myLatitude, myLongitude,o2.getCity().getLatitude(), o2.getCity().getLongitude(), result2);
            Float distance2 = result2[0];

            return distance1.compareTo(distance2);
        }

    }
}