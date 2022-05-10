package com.example.agrijuan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.agrijuan.Adapter.ToDoAdapter;
import com.example.agrijuan.Model.ToDoModel;
import com.example.agrijuan.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDo extends AppCompatActivity implements OnDialogCloseListner {

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        mRecyclerView = findViewById(R.id.recyclerview);
        fab  = findViewById(R.id.fab);
        myDB = new DataBaseHelper(ToDo.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(ToDo.this,myDB);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        mList  = myDB.getAllTask();
        Collections.reverse(mList);
        adapter.setTasks(mList);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ToDo.this,Dashboard.class));
        finish();
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList  = myDB.getAllTask();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }
}