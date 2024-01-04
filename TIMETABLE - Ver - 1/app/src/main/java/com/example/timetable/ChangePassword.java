package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetable.api.ApiService;
import com.example.timetable.model.PostModelAcc;

import java.sql.ResultSet;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    TextView tvCPEmail;
    EditText edtNewPass;
    Button btnCP, btnCPCancel;
    String chekPass = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initWidgets();
        tvCPEmail.setText(StaticData.Email);
        btnCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callChangePass();
            }
        });

        btnCPCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword.this.finish();
            }
        });
    }

    private void initWidgets() {
        tvCPEmail = findViewById(R.id.tvCPEmail);
        edtNewPass = findViewById(R.id.edtNewPass);
        btnCP = findViewById(R.id.btnCP);
        btnCPCancel = findViewById(R.id.btnCPCancel);
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
                startActivity(new Intent(ChangePassword.this, Setting.class));
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void callChangePass(){
//        if(edtNewPass.getText().toString().trim() !=""){
//            PostModelAcc postModelAcc = new PostModelAcc(StaticData.Email,edtNewPass.getText().toString().trim());
//            ApiService.apiService.putAccountData(StaticData.Email,postModelAcc).enqueue(new Callback<PostModelAcc>() {
//                @Override
//                public void onResponse(Call<PostModelAcc> call, Response<PostModelAcc> response) {
//                    ChangePassword.this.finish();
//                    Toast.makeText(ChangePassword.this, "Success", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<PostModelAcc> call, Throwable t) {
//                    Toast.makeText(ChangePassword.this, "Call fail", Toast.LENGTH_SHORT).show();
//                }
//            });
//            ChangePassword.this.finish();
//        }else{
//            Toast.makeText(ChangePassword.this, "Password null", Toast.LENGTH_SHORT).show();
//        }
//    }
    private void callChangePass(){
        if(edtNewPass.getText().toString().trim() !=""){
            if(!Pattern.compile(chekPass).matcher(edtNewPass.getText().toString().trim()).matches()){
                Toast.makeText(ChangePassword.this, "Password must have at least 8 characters, at least 1 letter and at least 1 number", Toast.LENGTH_SHORT).show();
            }else{
                ResultSet resultSet = StaticData.sqlConnections.ResultQuery("Update Account Set Password = N'"+
                        edtNewPass.getText().toString().trim() + "' where Email = N'"+
                        StaticData.Email + "'");
                Toast.makeText(ChangePassword.this, "Success", Toast.LENGTH_SHORT).show();
                ChangePassword.this.finish();
            }
        }else{
            Toast.makeText(ChangePassword.this, "Password null", Toast.LENGTH_SHORT).show();
        }
    }

}