package com.example.timetable.model;

public class TimetableData {
    private int TDID ;
    private String Id;
    private String Email;
    private String Detail;
    private String TimeStart;
    private String TimeEnd;
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
