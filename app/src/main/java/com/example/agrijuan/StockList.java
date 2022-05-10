package com.example.agrijuan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class StockList extends AppCompatActivity {
RecyclerView recyclerView;
MainAdapter mainAdapter;

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
recyclerView = findViewById(R.id.rv);
recyclerView.setLayoutManager(new LinearLayoutManager(this));

FirebaseRecyclerOptions<MainModel>options =
        new FirebaseRecyclerOptions.Builder<MainModel>()
        .setQuery(FirebaseDatabase.getInstance("https://agriculture-juan-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Stocks"),MainModel.class)
        .build();

mainAdapter = new MainAdapter(options);
recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StockList.this, Dashboard.class));
        finish();
    }
}