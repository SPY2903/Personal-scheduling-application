package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timetable.api.ApiService;
import com.example.timetable.model.PostModelTimetableData;
import com.example.timetable.model.PostModelTimetableInfor;
import com.example.timetable.model.TimetableInfor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private Button btnStartTime, btnEndTime, endTimeBtn;
    private LocalTime time;
    private int tSHour, tSMinute, tEHour, tEMinute;
    private String stime, etime,typeOfWork, progress;
    private Spinner sprTypeOfWork, sprProgress;
    Date sqlDateStart = null, sqlDateEnd = null;
//    private Connection connection = null;
//    SqlConnections sqlConnections;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
        sqlDateStart = java.sql.Date.valueOf(String.valueOf(CalendarUtils.selectedDate));
        //Toast.makeText(this, DetailConnect.Email, Toast.LENGTH_SHORT).show();

    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventNameET.setHint("Tên sự kiện");
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        btnStartTime = findViewById(R.id.btnStartTime);
        btnEndTime = findViewById(R.id.btnEndTime);
        endTimeBtn = findViewById(R.id.endTimeBtn);
        sprTypeOfWork = (Spinner) findViewById(R.id.sprTypeOfWork);
        ArrayList<String> lstTypeOfWork = new ArrayList<String>();
        ResultSet resultSet = StaticData.sqlConnections.ResultQuery("Select TypeOfWorkName from TypeOfWork");
        try{
            while (resultSet.next()){
                lstTypeOfWork.add(resultSet.getString(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(EventEditActivity.this, android.R.layout.simple_spinner_item,lstTypeOfWork);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.notifyDataSetChanged();
        sprTypeOfWork.setAdapter(arrayAdapter);
        sprTypeOfWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfWork = lstTypeOfWork.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sprProgress = findViewById(R.id.sprProgress);
        ArrayList<String> lstProgress = new ArrayList<>();
        resultSet = StaticData.sqlConnections.ResultQuery("Select ProgressName from Progress");
        try{
            while (resultSet.next()){
                lstProgress.add(resultSet.getString(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        ArrayAdapter adapter = new ArrayAdapter(EventEditActivity.this,android.R.layout.simple_spinner_item,lstProgress);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        sprProgress.setAdapter(adapter);
        sprProgress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progress = lstProgress.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EventEditActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tSHour = hourOfDay;
                        tSMinute = minute;
                        String time = tSHour + ":" + tSMinute;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                        try{
                            Date date = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                            //btnStartTime.setText(f12Hours.format(date));
                            btnStartTime.setText(f24Hours.format(date));
                            stime = f24Hours.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 24,0,true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(tSHour, tSMinute);
                timePickerDialog.show();
            }
        });
        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EventEditActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tEHour = hourOfDay;
                        tEMinute = minute;
                        String time = tEHour + ":" + tEMinute;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                        try{
                            Date date = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                            //btnStartTime.setText(f12Hours.format(date));
                            btnEndTime.setText(f24Hours.format(date));
                            etime = f24Hours.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 24,0,true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(tEHour, tEMinute);
                timePickerDialog.show();
            }
        });
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int dday = calendar.get(Calendar.DATE);
                int mmonth = calendar.get(Calendar.MONTH);
                int yyear = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date input = calendar.getTime();
                        LocalDate localDate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        sqlDateEnd = java.sql.Date.valueOf(String.valueOf(localDate));
                        endTimeBtn.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },yyear,mmonth,dday);
                datePickerDialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveEventAction(View view)
    {
        ResultSet resultSet;
        String eventName = eventNameET.getText().toString();
        //Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time, stime,etime);
        if(eventNameET.getText().toString().trim().contentEquals("")){
            Toast.makeText(EventEditActivity.this, "Null event", Toast.LENGTH_SHORT).show();
        }
//        else if( (tSHour == tEHour && tSMinute > tEMinute) || (tSHour == tEHour && tSMinute == tEMinute && tSHour!=0 && tSMinute!=0)){
//            Toast.makeText(this, "Invalid time", Toast.LENGTH_SHORT).show();
        else if(sqlDateEnd != null && sqlDateStart.after(sqlDateEnd)){
            Toast.makeText(EventEditActivity.this, "Invalid time", Toast.LENGTH_SHORT).show();
        }
        else{
            String TypeOfWorkID = "";
            resultSet = StaticData.sqlConnections.ResultQuery("Select TypeOfWorkID from TypeOfWork where TypeOfWorkName = N'" + typeOfWork + "'");
            try {
                if(resultSet.next()){
                    TypeOfWorkID = resultSet.getString(1);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            String ProgressID = "";
            resultSet = StaticData.sqlConnections.ResultQuery("Select ProgressID from Progress where ProgressName = N'" + progress + "'");
            try {
                if(resultSet.next()){
                    ProgressID = resultSet.getString(1);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            resultSet = StaticData.sqlConnections.ResultQuery("Select WorkID from work where WorkName = N'" + eventNameET.getText().toString().trim() +"'");
            String newWorkID = "";
            try{
                if(!resultSet.next()){
                    resultSet = StaticData.sqlConnections.ResultQuery("Select WorkID from work order by WorkID desc");
                    if(resultSet.next()){
                        String workID = resultSet.getString(1);
                        char[] workIDChar = workID.toCharArray();
                        int index = workIDChar[1] - '0';
                        index++;
                        newWorkID = String.valueOf(workIDChar[0]) + index;
                        resultSet = StaticData.sqlConnections.ResultQuery("Insert into Work values (N'"+ newWorkID +"',N'"+ eventNameET.getText().toString().trim() +"',N'"+TypeOfWorkID+"')");
                    }
                }else{
                    newWorkID = resultSet.getString(1);
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            String newCycleID = "";
            String sqlQuery;
            if(sqlDateEnd == null){
                sqlQuery = "Select CycleID from Cycle where DateStart = '" + sqlDateStart.toString() + "' and DateEnd IS NULL";
            }else{
                sqlQuery = "Select CycleID from Cycle where DateStart = '" + sqlDateStart.toString() + "' and DateEnd = '" + sqlDateEnd.toString() + "'";
            }
            resultSet = StaticData.sqlConnections.ResultQuery(sqlQuery);
            try{
                if(resultSet.next()){
                    newCycleID = resultSet.getString(1);
                }else{
                    resultSet = StaticData.sqlConnections.ResultQuery("Select CycleID from Cycle order by CycleID desc");
                    try{
                        if(resultSet.next()){
                            String CycleID = resultSet.getString(1);
                            char[] CycleIDChar = CycleID.toCharArray();
                            int index = CycleIDChar[1] - '0';
                            index++;
                            newCycleID = String.valueOf(CycleIDChar[0]) + index;
                            if(sqlDateEnd == null){
                                resultSet = StaticData.sqlConnections.ResultQuery("Insert into Cycle values (N'"+newCycleID+"','"+ sqlDateStart.toString() +"',null)");
                            }else{
                                resultSet = StaticData.sqlConnections.ResultQuery("Insert into Cycle values (N'"+newCycleID+"','"+ sqlDateStart.toString() +"','"+ sqlDateEnd.toString() +"')");
                            }
                        }
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            String newPersonalWorkID = "";
            resultSet = StaticData.sqlConnections.ResultQuery("Select PersonalWorkID from PersonalWork order by PersonalWorkID desc");
            try{
                if(resultSet.next()){
                    String PersonalWorkID = resultSet.getString(1);
                    char[] PersonalWorkIDChar = PersonalWorkID.toCharArray();
                    int index = PersonalWorkIDChar[2] - '0';
                    index++;
                    newPersonalWorkID = String.valueOf(PersonalWorkIDChar[0]) + String.valueOf(PersonalWorkIDChar[1])  + index;
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            resultSet = StaticData.sqlConnections.ResultQuery("Insert into PersonalWork values (N'"+ newPersonalWorkID+"',N'"+StaticData.Email+"',N'"+newWorkID+"',N'"+ProgressID+"',N'"+newCycleID+"',N'"+eventNameET.getText().toString().trim()+"')");
//            Toast.makeText(EventEditActivity.this, newWorkID, Toast.LENGTH_SHORT).show();
//            resultSet = StaticData.sqlConnections.ResultQuery("Insert into Work values (N'" +
//                    newWorkID + "',N'" + eventNameET.getText().toString().trim()  + "',N'HT')");
//            ResultSet resultSet = StaticData.sqlConnections.ResultQuery("Insert into TimetableInfor values(N'"+ CalendarUtils.formattedDate(CalendarUtils.selectedDate) +"',0)");
//            resultSet = StaticData.sqlConnections.ResultQuery("Insert into TimetableData values(N'"+
//                    CalendarUtils.formattedDate(CalendarUtils.selectedDate)+"', N'"+ StaticData.Email+
//                    "', N'"+ eventName +"',N' "+ stime +"',N'"+ etime +"')");
//            ApiService.apiService.getTimetableInfor(CalendarUtils.formattedDate(CalendarUtils.selectedDate)).enqueue(new Callback<TimetableInfor>() {
//                @Override
//                public void onResponse(Call<TimetableInfor> call, Response<TimetableInfor> response) {
//                    TimetableInfor timetableInfor = response.body();
//                    if(timetableInfor==null){
//                        PostModelTimetableInfor postModelTimetableInfor = new PostModelTimetableInfor(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
//                        ApiService.apiService.postTimetableInforData(postModelTimetableInfor).enqueue(new Callback<PostModelTimetableInfor>() {
//                            @Override
//                            public void onResponse(Call<PostModelTimetableInfor> call, Response<PostModelTimetableInfor> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<PostModelTimetableInfor> call, Throwable t) {
//                                Toast.makeText(EventEditActivity.this, "Call fail", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<TimetableInfor> call, Throwable t) {
//
//                }
//            });
//
//            PostModelTimetableData postModelTimetableData = new PostModelTimetableData(CalendarUtils.formattedDate(CalendarUtils.selectedDate),StaticData.Email,eventName,stime,etime);
//            ApiService.apiService.postTimetableDataData(postModelTimetableData).enqueue(new Callback<PostModelTimetableData>() {
//                @Override
//                public void onResponse(Call<PostModelTimetableData> call, Response<PostModelTimetableData> response) {
//                    EventEditActivity.this.finish();
//                }
//
//                @Override
//                public void onFailure(Call<PostModelTimetableData> call, Throwable t) {
//                    Toast.makeText(EventEditActivity.this, "Call Fail", Toast.LENGTH_SHORT).show();
//                }
//            });
            EventEditActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mSetting:
                startActivity(new Intent(EventEditActivity.this, Setting.class));
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}