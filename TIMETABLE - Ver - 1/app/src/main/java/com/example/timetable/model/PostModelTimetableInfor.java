package com.example.timetable.model;

import com.google.gson.annotations.SerializedName;

public class PostModelTimetableInfor {
    @SerializedName("Id")
    private String Id;

    public PostModelTimetableInfor(String Id) {
        this.Id = Id;
    }

    public String getId() {return Id;}

    public void setId(String id) {Id = id;}
}
