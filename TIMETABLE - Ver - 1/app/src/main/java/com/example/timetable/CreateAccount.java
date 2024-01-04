package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timetable.api.ApiService;
import com.example.timetable.model.PostModelAcc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccount extends AppCompatActivity {

    EditText edtNEmail, edtNPassword, edtRePassword;
    Button btnCreateAcc;
    String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    String chekPass = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
    SqlConnections sqlConnections;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        edtNEmail = findViewById(R.id.edtNEmail);
        edtNPassword = findViewById(R.id.edtNPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnCreateAcc = findViewById(R.id.btnCreateAcc);
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCreateAccount();
            }
        });
    }
//    private void callCreateAccount(){
//        if(edtNEmail.getText().toString() == "" || edtNPassword.getText().toString() == "" || edtRePassword.getText().toString() ==""){
//            Toast.makeText(CreateAccount.this, "Erro", Toast.LENGTH_SHORT).show();
//        }
//        else if(!edtNPassword.getText().toString().trim().contentEquals(edtRePassword.getText().toString().trim())){
//            Toast.makeText(CreateAccount.this, "Erro", Toast.LENGTH_SHORT).show();
//        }else{
//            PostModelAcc postModelAcc = new PostModelAcc(edtNEmail.getText().toString().trim(),edtNPassword.getText().toString().trim());
//            ApiService.apiService.postAccountData(postModelAcc).enqueue(new Callback<PostModelAcc>() {
//                @Override
//                public void onResponse(Call<PostModelAcc> call, Response<PostModelAcc> response) {
//                    Toast.makeText(CreateAccount.this, "Success", Toast.LENGTH_SHORT).show();
//                    CreateAccount.this.finish();
//                }
//
//                @Override
//                public void onFailure(Call<PostModelAcc> call, Throwable t) {
//                    Toast.makeText(CreateAccount.this, "call fail", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//    }
    private void callCreateAccount(){
        if(edtNEmail.getText().toString().trim().contentEquals("") || edtNPassword.getText().toString().trim().contentEquals("") || edtRePassword.getText().toString().trim().contentEquals("")){
            Toast.makeText(CreateAccount.this, "Erro", Toast.LENGTH_SHORT).show();
        }
        else if(!edtNEmail.getText().toString().trim().contentEquals("") && !Pattern.compile(regexPattern).matcher(edtNEmail.getText().toString()).matches()){
            Toast.makeText(CreateAccount.this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
        else if(!edtNPassword.getText().toString().trim().contentEquals(edtRePassword.getText().toString().trim())){
            Toast.makeText(CreateAccount.this, "Erro", Toast.LENGTH_SHORT).show();
        }
        else if(edtNPassword.getText().toString().trim().contentEquals(edtRePassword.getText().toString().trim()) &&
                !Pattern.compile(chekPass).matcher(edtNPassword.getText().toString().trim()).matches()){
            Toast.makeText(CreateAccount.this, "Password must have at least 8 characters, at least 1 letter and at least 1 number", Toast.LENGTH_SHORT).show();
        }
        else{
            sqlConnections = new SqlConnections(StaticData.ip, StaticData.port, StaticData.database, StaticData.username, StaticData.password,CreateAccount.this);
            sqlConnections.Connect();
            StaticData.sqlConnections = sqlConnections;
            if(StaticData.sqlConnections.isConnected()){
                boolean isUsed = false;
                ResultSet resultSet = StaticData.sqlConnections.ResultQuery("Select * from Account where Email = N'"
                        + edtNEmail.getText().toString().trim() + "'");
                try {
                    while(resultSet.next()) {
                        if (resultSet.getString(1) != "") {
                            Toast.makeText(CreateAccount.this, "Email has been used", Toast.LENGTH_SHORT).show();
                            isUsed = true;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(!isUsed){
                    resultSet = StaticData.sqlConnections.ResultQuery("Insert into Account values(N'"+
                            edtNEmail.getText().toString() +"',N'" + edtNPassword.getText().toString() + "')");
                    Toast.makeText(CreateAccount.this, "Success", Toast.LENGTH_SHORT).show();
                    CreateAccount.this.finish();
                }
            }
        }
    }

}