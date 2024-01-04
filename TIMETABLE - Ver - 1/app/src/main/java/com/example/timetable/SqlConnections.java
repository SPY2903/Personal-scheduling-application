package com.example.timetable;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.StrictMode;

import androidx.core.app.ActivityCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Result;

public class SqlConnections {
    private String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private String ip = "";
    private String port = "";
    private String database = "";
    private String username = "";
    private String password = "";
    private Activity activity;
    private String txtConnections = "";
    public SqlConnections(String ip, String port, String database, String username, String password, Activity activity) {
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.activity = activity;
    }

    private Connection connection = null;

    public void Connect(){
        String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url,username,password);
            txtConnections = "SUCCESS";
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            txtConnections = "ERROR";
        }catch(SQLException e){
            e.printStackTrace();
            txtConnections = "FAILURE";
        }
    }

    public boolean isConnected(){
        if(connection!=null){
            return true;
        }
        return false;
    }

    public String TestConnections(){
        return txtConnections;
    }

    public ResultSet ResultQuery(String sqlQuery){
        if(connection != null){
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                return resultSet;
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        else{
            txtConnections = "Connection is null";
        }
        return null;
    }
}
