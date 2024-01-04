package com.example.timetable;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }
    private EventListner eventListner;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        TextView etimeTV = convertView.findViewById(R.id.etimeTV);
        ImageView imgVEdit = convertView.findViewById(R.id.imgVEdit);
        ImageView imgVDelete = convertView.findViewById(R.id.imgVDelete);
        imgVEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListner.onEditListener(position);
            }
        });
        imgVDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListner.onDeteleListener(position);
            }
        });
        String eventTitle = event.getName() +" "+ CalendarUtils.formattedTime(event.getTime());
        String eventTime = event.getTimeStart() + " ~ " + event.getTimeEnd();
        eventCellTV.setText(event.getName());
        etimeTV.setText(eventTime);
        return convertView;
    }

    interface EventListner{
        void onDeteleListener(int position);
        void onEditListener(int position);
    }
    public void setEventListner(EventListner eventListner){
        this.eventListner = eventListner;
    }
}

