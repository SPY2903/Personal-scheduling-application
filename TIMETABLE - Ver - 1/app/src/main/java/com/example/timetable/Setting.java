package com.example.timetable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.Manifest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Setting extends AppCompatActivity {
    TextView tvCurrentEmail;
    Button btnChangePass, btnExport, btnAnnounceTime, btnLogout, btnChangeInfor;
    Switch swAnnounce;
    LinearLayout LnLAnnounce;
    Spinner spnDate;
    ArrayList<String> lstDate = new ArrayList<>();
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    MaterialTimePicker picker;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int anHour, anMinute;
    File filePath = new File(Environment.getExternalStorageDirectory() + "/Timetable.xls");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //createNotificationChanel();
        initWidgets();
        tvCurrentEmail.setText(StaticData.Email);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, ChangePassword.class);
                startActivity(intent);
            }
        });
        btnChangeInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, PersonalInformation.class);
                startActivity(intent);
            }
        });
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Setting.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFSheet hssfSheet = hssfWorkbook.createSheet();
//                HSSFRow hssfRow = hssfSheet.createRow(0);
//                HSSFCell hssfCell = hssfRow.createCell(0);
//                hssfCell.setCellValue(StaticData.Email);
                try{
                    SqlConnections sqlConnections;
                    sqlConnections = new SqlConnections(StaticData.ip, StaticData.port, StaticData.database, StaticData.username, StaticData.password,Setting.this);
                    sqlConnections.Connect();
                    ResultSet resultSet = sqlConnections.ResultQuery("select * from TimetableData where Email = N'"+StaticData.Email+"'");
                    int i = 0;
                    while(resultSet.next()){
                        HSSFRow hssfRow = hssfSheet.createRow(i);
                        HSSFCell hssfCell0 = hssfRow.createCell(0);
                        hssfCell0.setCellValue(resultSet.getString(2));
                        HSSFCell hssfCell1 = hssfRow.createCell(1);
                        hssfCell1.setCellValue(resultSet.getString(4));
                        HSSFCell hssfCell2 = hssfRow.createCell(2);
                        hssfCell2.setCellValue(resultSet.getString(5));
                        HSSFCell hssfCell3 = hssfRow.createCell(3);
                        hssfCell3.setCellValue(resultSet.getString(6));
                        i++;
                    }
                }catch (Exception ex){

                }
                try{
                    if(!filePath.exists()){
                        filePath.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                    hssfWorkbook.write(fileOutputStream);
                    if(fileOutputStream!=null){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        Toast.makeText(Setting.this, "Export success", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    //Toast.makeText(Setting.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        swAnnounce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    LnLAnnounce.setVisibility(buttonView.VISIBLE);
                    StaticData.isAnnounce = true;

                }else{
                    LnLAnnounce.setVisibility(buttonView.GONE);
                    StaticData.isAnnounce = false;
                    pendingIntent.cancel();
                }
            }
        });
        lstDate.add("Trước 1 ngày");
        lstDate.add("Trong ngày");
        ArrayAdapter arrayAdapter = new ArrayAdapter(Setting.this, R.layout.color_spinner_layout,lstDate);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(arrayAdapter);
        if(StaticData.isAnnounce){
            //swAnnounce.setSelected(true);
            swAnnounce.setChecked(true);
            if(StaticData.dateAnnounce != "Trước 1 ngày"){
                lstDate.clear();
                lstDate.add("Trong ngày");
                lstDate.add("Trước 1 ngày");
                ArrayAdapter narrayAdapter = new ArrayAdapter(Setting.this, R.layout.color_spinner_layout,lstDate);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnDate.setAdapter(narrayAdapter);
            }
            btnAnnounceTime.setText(StaticData.timeAnnounce);
        }
        spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StaticData.dateAnnounce = lstDate.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAnnounceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TimePickerDialog timePickerDialog = new TimePickerDialog(
//                        Setting.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        anHour = hourOfDay;
//                        anMinute = minute;
//                        String time = anHour + ":" + anMinute;
//                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
//                        try{
//                            Date date = f24Hours.parse(time);
//                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
//                            //btnAnnounceTime.setText(f12Hours.format(date));
//                            btnAnnounceTime.setText(f24Hours.format(date));
//                            StaticData.timeAnnounce = f24Hours.format(date);
//                            calendar = Calendar.getInstance();
//                            calendar.set(Calendar.HOUR_OF_DAY,anHour);
//                            calendar.set(Calendar.MINUTE,anMinute);
//                            calendar.set(Calendar.SECOND,0);
//                            calendar.set(Calendar.MILLISECOND,0);
//                            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                                Intent intent = new Intent(Setting.this, AlarmManager.class);
//                                pendingIntent = PendingIntent.getBroadcast(Setting.this, 0,intent,0);
//                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
//                                    AlarmManager.INTERVAL_DAY,pendingIntent);
//                            Toast.makeText(Setting.this, "Hour : " + calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Setting.this, "Alarm set successfully", Toast.LENGTH_SHORT).show();
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 24,0,true);
//                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                timePickerDialog.updateTime(anHour, anMinute);
//                timePickerDialog.show();
                timePickerDialog = new TimePickerDialog(Setting.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String timeAnnounce = hourOfDay + ":" + minute;
                        if(minute < 10){
                            timeAnnounce = hourOfDay + ":0" + minute;
                        }
                        btnAnnounceTime.setText(timeAnnounce);
                        StaticData.timeAnnounce = timeAnnounce;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        Intent intent = new Intent(Setting.this, AlarmReceiver.class);
                        intent.setAction("MyAction");
                        intent.putExtra("time" , timeAnnounce);
                        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        pendingIntent = PendingIntent.getBroadcast(Setting.this,0,
                                intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
                    }
                }, 24,00,true);
                timePickerDialog.show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting.this, Login.class));
                StaticData.isAnnounce = false;
                Setting.this.finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChanel() {
        CharSequence name = "Thông báo";
        String decription = "Bạn có một sự kiện";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
        channel.setDescription(decription);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void initWidgets() {
        tvCurrentEmail = findViewById(R.id.tvCurrentEmail);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnExport = findViewById(R.id.btnExport);
        btnLogout = findViewById(R.id.btnLogout);
        swAnnounce = findViewById(R.id.swAnnounce);
        LnLAnnounce = findViewById(R.id.LnLAnnounce);
        spnDate = findViewById(R.id.spnDate);
        btnAnnounceTime = findViewById(R.id.btnAnnounceTime);
        btnChangeInfor = findViewById(R.id.btnChangeInfor);
    }
}