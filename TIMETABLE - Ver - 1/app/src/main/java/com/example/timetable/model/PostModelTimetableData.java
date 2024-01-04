package com.example.timetable.model;

import com.google.gson.annotations.SerializedName;

public class PostModelTimetableData {
    @SerializedName("TDID")
    private int TDID ;
    @SerializedName("Id")
    private String Id;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Detail")
    private String Detail;
    @SerializedName("TimeStart")
    private String TimeStart;
    @SerializedName("TimeEnd")
    private String TimeEnd;

    public PostModelTimetableData(String id, String email, String detail, String timeStart, String timeEnd) {
        Id = id;
        Email = email;
        Detail = detail;
        TimeStart = timeStart;
        TimeEnd = timeEnd;
    }

    public int getTDID() {
        return TDID;
    }

    public void setTDID(int TDID) {
        this.TDID = TDID;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getTimeEnd() {
        return TimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        TimeEnd = timeEnd;
    }
}
