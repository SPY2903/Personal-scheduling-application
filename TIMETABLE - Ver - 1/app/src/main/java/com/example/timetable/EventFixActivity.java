package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.timetable.api.ApiService;
import com.example.timetable.model.PostModelTimetableData;
import com.example.timetable.model.TimetableData;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFixActivity extends AppCompatActivity {
    EditText eventFNameET;
    TextView eventFDateTV;
    Button btnFStartTime, btnFEndTime, Fixbtn, Cancelbtn;
    String eventName, eventDate, stime, etime;
    private int Id,tSHour, tSMinute, tEHour, tEMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_fix);
        initWidgets();
        try{
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("Detail");
            Id = bundle.getInt("Id");
            eventName = bundle.getString("eventName");
            eventDate = bundle.getString("eventDate");
            stime = bundle.getString("timeStart");
            etime= bundle.getString("timeEnd");
            eventFNameET.setText(eventName);
            eventFDateTV.setText(eventDate);
            btnFStartTime.setText(stime);
            btnFEndTime.setText(etime);
        }catch (Exception e){

        }

        btnFStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EventFixActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
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
                            btnFStartTime.setText(f24Hours.format(date));
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

        btnFEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EventFixActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
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
                            btnFEndTime.setText(f24Hours.format(date));
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

        Fixbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

//                PostModelTimetableData postModelTimetableData = new PostModelTimetableData(CalendarUtils.formattedDate(CalendarUtils.selectedDate),StaticData.Email,eventName,stime,etime);
//                postModelTimetableData.setTDID(Id);
//                ApiService.apiService.putTimetableDataData(Id,postModelTimetableData).enqueue(new Callback<TimetableData>() {
//                    @Override
//                    public void onResponse(Call<TimetableData> call, Response<TimetableData> response) {
//                        EventFixActivity.this.finish();
//                    }
//
//                    @Override
//                    public void onFailure(Call<TimetableData> call, Throwable t) {
//
//                    }
//                });
                ResultSet resultSet = StaticData.sqlConnections.ResultQuery("Update TimetableData Set" +
                        " Detail = N'"+ eventFNameET.getText().toString().trim() +"', TimeStart = N'"+ stime
                        +"', TimeEnd = N'" + etime + "' where TDID = " + Id);
                EventFixActivity.this.finish();
            }
        });
        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFixActivity.this.finish();
            }
        });
    }

    private void initWidgets(){
        eventFNameET = findViewById(R.id.edtFNameEvent);
        eventFDateTV = findViewById(R.id.tvFDate);
        btnFStartTime = findViewById(R.id.btnFSTime);
        btnFEndTime = findViewById(R.id.btnFETime);
        Fixbtn = findViewById(R.id.btnCP);
        Cancelbtn = findViewById(R.id.btnCPCancel);
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
                startActivity(new Intent(EventFixActivity.this, Setting.class));
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}