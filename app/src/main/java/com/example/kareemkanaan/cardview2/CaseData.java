package com.example.kareemkanaan.cardview2;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by ASUS on 3/15/2017.
 * This Class is created in order to facilitate the process of storing and retreieving case Date info from
 * firebase
 *
 */

public class CaseData {
    public String dateOfPost,Title, PhoneNumber, Location, Description, ImgSource, Needs;
    private String caseId;

    public CaseData() {
    }

    public CaseData(String title, String phoneNumber, String location, String description, String imgSource, String needs) {
        Title = title;
        PhoneNumber = phoneNumber;
        Location = location;
        Description = description;
        ImgSource = imgSource;
        Needs = needs;
        // initializing  dateOfPost
        Date date = new Date();
        dateOfPost= new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    public void setCaseId(String caseId){
        this.caseId=caseId;
    }

}