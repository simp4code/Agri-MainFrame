package com.example.agrijuan;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CustomCalendarView extends AppCompatActivity {
CustomCalendar customCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_calendar_view);

        customCalendarView = (CustomCalendar)findViewById(R.id.custom_calendar_view);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CustomCalendarView.this, Dashboard.class));
        finish();
    }
}