package com.example.timetable;

import static com.example.timetable.CalendarUtils.daysInWeekArray;
import static com.example.timetable.CalendarUtils.monthYearFromDate;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timetable.api.ApiService;
import com.example.timetable.model.TimetableData;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, EventAdapter.EventListner
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private EventAdapter eventAdapter;
    SqlConnections sqlConnections;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        sqlConnections = new SqlConnections(StaticData.ip, StaticData.port, StaticData.database, StaticData.username, StaticData.password,WeekViewActivity.this);
        sqlConnections.Connect();
        StaticData.sqlConnections = sqlConnections;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventAdapter.setEventListner(this);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(WeekViewActivity.this, EventEditActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDeteleListener(int position) {
        Event e = Event.eventsList.get(position);
        //Toast.makeText(this, e.getId() +"~"+ e.getName() + e.getTimeStart() + e.getTimeEnd(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeekViewActivity.this);
        alertDialog.setTitle("Thông báo");
        alertDialog.setMessage("Bạn chắc chứ ?");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResultSet resultSet = StaticData.sqlConnections.ResultQuery("Delete from TimetableData where TDID = " + e.getId());
                callDeleteTimetableData(position);
                setEventAdpater();
            }
        });
        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onEditListener(int position){
        Event e = Event.eventsList.get(position);
        Intent intent = new Intent(WeekViewActivity.this,EventFixActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("Id",e.getId());
        bundle.putString("eventName",e.getName());
        bundle.putString("eventDate",CalendarUtils.formattedDate(e.getDate()));
        bundle.putString("timeStart",e.getTimeStart());
        bundle.putString("timeEnd",e.getTimeEnd());
        intent.putExtra("Detail",bundle);
        startActivity(intent);
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
                startActivity(new Intent(WeekViewActivity.this, Setting.class));
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callDeleteTimetableData(int id){
//        ApiService.apiService.deleteTimetableDataData(id).enqueue(new Callback<TimetableData>() {
//            @Override
//            public void onResponse(Call<TimetableData> call, Response<TimetableData> response) {
//            }
//
//            @Override
//            public void onFailure(Call<TimetableData> call, Throwable t) {
//            }
//        });
    }
}