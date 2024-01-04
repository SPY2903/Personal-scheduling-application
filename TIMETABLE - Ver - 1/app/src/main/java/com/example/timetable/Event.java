package com.example.timetable;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.timetable.api.ApiService;
import com.example.timetable.model.TimetableData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();
        LocalTime time;
        time = LocalTime.now();
//        callGetTimetableData();
//        for(Event event : eventsList)
//        {
//            if(event.getDate().equals(date))
//                events.add(event);
//        }
//        eventsList = events;
//        return events;
        //------------------------------
        ArrayList<Event> sqlevents = new ArrayList<>();
        ResultSet resultSet = StaticData.sqlConnections.ResultQuery("Select * from PersonalWork where Email = N'" + StaticData.Email + "'");
        int count = 0;
        try{
            while (resultSet.next()){
                String ID = resultSet.getString(1);
                String WorkID = resultSet.getString(3);
                String CycleID = resultSet.getString(5);
                String StartTime = "";
                String EndTime = "";
                String WorkName = "";
                ResultSet rs = StaticData.sqlConnections.ResultQuery("Select WorkName from Work where WorkID = N'" + WorkID + "'");
                if(rs.next()){
                    WorkName = rs.getString(1);
                }
                rs = StaticData.sqlConnections.ResultQuery("Select * from Cycle where CycleID = N'"+CycleID+"'");
                if(rs.next()){
                    StartTime = rs.getString(2);
                    EndTime = rs.getString(3);
                }
                if(EndTime == "") EndTime = "null";
                Date da = java.sql.Date.valueOf(StartTime);
                LocalDate lo = da.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                sqlevents.add(new Event(count,WorkName,lo,time,
                        StartTime,EndTime));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        for(Event event : sqlevents)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }
        eventsList = events;
        return events;
    }

    private int Id;
    private String name;

    private String timeStart;
    private String timeEnd;
    private LocalDate date;
    private static LocalTime time;

    public Event(int Id,String name, LocalDate date, LocalTime time, String timeStart, String timeEnd)
    {
        this.Id = Id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }
    public int getId() {return Id;}

    public void setId(int id) {Id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {this.date = date;}

    public LocalTime getTime() {return time;}

    public void setTime(LocalTime time) {this.time = time;}

    public String getTimeStart() {return timeStart;}

    public void setTimeStart(String timeStart) {this.timeStart = timeStart;}

    public String getTimeEnd() {return timeEnd;}

    public void setTimeEnd(String timeEnd) {this.timeEnd = timeEnd;}

    public static void callGetTimetableData(){
        ApiService.apiService.getAllTimetableData().enqueue(new Callback<List<TimetableData>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<TimetableData>> call, Response<List<TimetableData>> response) {
                List<TimetableData> TimetableDataList = response.body();
                if(TimetableDataList != null){
                    for (TimetableData t: TimetableDataList) {
                        eventsList.add(new Event(t.getTDID(),t.getDetail(),CalendarUtils.ConvertStringToDate(t.getId()),time,t.getTimeStart(),t.getTimeEnd()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TimetableData>> call, Throwable t) {
            }
        });
    }

}

