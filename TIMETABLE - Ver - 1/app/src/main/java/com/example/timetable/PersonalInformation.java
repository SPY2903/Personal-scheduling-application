package com.example.timetable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class PersonalInformation extends AppCompatActivity {

    TextView tvEM;
    EditText editTextTextPersonName, editTextNum;
    Button dateTimeBtn, btnIFOK, btnIFCancel;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        tvEM = findViewById(R.id.tvEM);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextNum = findViewById(R.id.editTextNum);
        dateTimeBtn = findViewById(R.id.dateTimeBtn);
        btnIFOK = findViewById(R.id.btnIFOK);
        btnIFCancel = findViewById(R.id.btnIFCancel);
        tvEM.setText(StaticData.Email);
        dateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int dday = calendar.get(Calendar.DATE);
                int mmonth = calendar.get(Calendar.MONTH);
                int yyear = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInformation.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date input = calendar.getTime();
                        LocalDate localDate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        date = java.sql.Date.valueOf(String.valueOf(localDate));
                        dateTimeBtn.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },yyear,mmonth,dday);
                datePickerDialog.show();
            }
        });
        btnIFCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalInformation.this.finish();
            }
        });
        btnIFOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextTextPersonName.getText().toString().trim().contentEquals("") || editTextNum.getText().toString().trim().contentEquals("")){
                    Toast.makeText(PersonalInformation.this, "Erro", Toast.LENGTH_SHORT).show();
                }
                else if(dateTimeBtn.getText().toString().trim().contentEquals("../../....")){
                    Toast.makeText(PersonalInformation.this, "Erro", Toast.LENGTH_SHORT).show();
                }
                else{
                    ResultSet resultSet = StaticData.sqlConnections.ResultQuery("Insert into PersonalInformation values (N'"+editTextTextPersonName.getText().toString().trim()+"','"+date.toString()+"',N'"+StaticData.Email+"',N'"+editTextNum.getText().toString().trim()+"')");
                    PersonalInformation.this.finish();
                }
            }
        });
    }
}