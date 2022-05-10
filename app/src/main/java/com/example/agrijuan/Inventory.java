package com.example.agrijuan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Inventory extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    EditText product_name,product_price,product_qty;
    Button saveItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        product_name = findViewById(R.id.p_name);
        product_price = findViewById(R.id.p_price);
        product_qty = findViewById(R.id.p_qty);
        saveItems = findViewById(R.id.addtostocks);



        saveItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemProcess();
            }
        });

    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(Inventory.this,Dashboard.class));
        finish();
    }
    private void itemProcess(){
        Map<String,Object> map = new HashMap<>();
        map.put("name",product_name.getText().toString());
        map.put("price",product_price.getText().toString());
        map.put("quantity",product_qty.getText().toString());

        FirebaseDatabase.getInstance("https://agriculture-juan-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Stocks").child(product_name.getText().toString())

                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        product_name.setText("");
                        product_price.setText("");
                        product_qty.setText("");
                        Toast.makeText(getApplicationContext(),"Successfully added!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });

    }


}