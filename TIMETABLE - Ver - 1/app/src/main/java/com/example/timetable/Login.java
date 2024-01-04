package com.example.timetable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetable.api.ApiService;
import com.example.timetable.model.Account;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText edtEmai, edtPassword;
    TextView tvForgetPassword, tvCreateAccount;
    Button btnLogin;
    SqlConnections sqlConnections;
    CircleImageView rimgGoogle;
    View vi;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmai = findViewById(R.id.edtNEmail);
        edtPassword = findViewById(R.id.edtNPassword);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        btnLogin = findViewById(R.id.btnCreateAcc);
       //Toast.makeText(this, "Welcom back <3", Toast.LENGTH_SHORT).show();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogin();
                vi = v;
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, CreateAccount.class);
                v.getContext().startActivity(intent);
            }
        });
    }

//    private void callLogin(){
//        ApiService.apiService.getAccount(edtEmai.getText().toString().trim()).enqueue(new Callback<Account>() {
//            @Override
//            public void onResponse(Call<Account> call, Response<Account> response) {
//                Account getAccount = response.body();
//                if(getAccount != null){
//                    if(edtPassword.getText().equals(getAccount.getPassword())){
//                        Intent intent = new Intent(Login.this, MainActivity.class);
//                        vi.getContext().startActivity(intent);
//                        Login.this.finish();
//                    }else{
//                        Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Account> call, Throwable t) {
//                Toast.makeText(Login.this, "Call fail", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private void callLogin(){
        sqlConnections = new SqlConnections(StaticData.ip, StaticData.port, StaticData.database, StaticData.username, StaticData.password,Login.this);
        sqlConnections.Connect();
        StaticData.sqlConnections = sqlConnections;
        if(edtEmai.getText().toString() != "" && edtPassword.getText().toString() != ""){
            ResultSet resultSet = sqlConnections.ResultQuery("Select * from Account where Email = N'"
                + edtEmai.getText().toString() + "' and Password = N'" + edtPassword.getText().toString() + "'");
//            ResultSet resultSet = sqlConnections.ResultQuery("select * from Account");
        if(StaticData.sqlConnections.isConnected()){
            //Toast.makeText(Login.this, StaticData.sqlConnections.TestConnections(), Toast.LENGTH_SHORT).show();
            try {

                if(resultSet.next() == false) {
                    Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
                resultSet = StaticData.sqlConnections.ResultQuery("Select * from Account where Email = N'"
                        + edtEmai.getText().toString() + "' and Password = N'" + edtPassword.getText().toString() + "'");
                while(resultSet.next()) {
//                                Toast.makeText(Login.this, "acc :" + resultSet.getString(1), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(Login.this, "pass :" + resultSet.getString(2), Toast.LENGTH_SHORT).show();
                    if (resultSet.getString(1) != "") {
                        StaticData.Email = resultSet.getString(1);

                        //Toast.makeText(Login.this, "acc :" + DetailConnect.Email, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        Login.this.finish();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
                Toast.makeText(Login.this, StaticData.sqlConnections.TestConnections(), Toast.LENGTH_SHORT).show();;
            }
        }
    }


}