package com.example.timetable.api;

import com.example.timetable.StaticData;
import com.example.timetable.model.Account;
import com.example.timetable.model.PostModelAcc;
import com.example.timetable.model.PostModelTimetableData;
import com.example.timetable.model.PostModelTimetableInfor;
import com.example.timetable.model.TimetableData;
import com.example.timetable.model.TimetableInfor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(StaticData.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("api/Account/")
    Call<List<Account>> getAllccount();
    @GET("api/Account/{email}")
    Call<Account> getAccount(@Path("email") String email);
    @POST("api/Account/")
    Call<PostModelAcc> postAccountData(@Body PostModelAcc postModelAcc);
    @PUT("api/Account/{email}")
    Call<PostModelAcc> putAccountData(@Path("email") String email,@Body PostModelAcc postModelAcc);

    @GET("api/TimetableInfor/")
    Call<List<TimetableInfor>> getAllTimetableInfor();
    @GET("api/TimetableInfor/{Id}")
    Call<TimetableInfor> getTimetableInfor(@Path("Id") String Id);
    @POST("api/TimetableInfor/")
    Call<PostModelTimetableInfor> postTimetableInforData(@Body PostModelTimetableInfor postModelTimetableInfor);

    @GET("api/TimetableData/")
    Call<List<TimetableData>> getAllTimetableData();
    @POST("api/TimetableData/")
    Call<PostModelTimetableData> postTimetableDataData(@Body PostModelTimetableData postModelTimetableData);
    @PUT("api/TimetableData/{TDID}")
    Call<TimetableData> putTimetableDataData(@Path("TDID") int id,@Body PostModelTimetableData postModelTimetableData);
    @DELETE("api/TimetableData/{TDID}")
    Call<TimetableData> deleteTimetableDataData(@Path("TDID") int TDID);
}
