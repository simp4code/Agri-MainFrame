package com.example.agrijuan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {


    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainModel model) {
        holder.n.setText(model.getName());
        holder.q.setText(model.getQuantity());
        holder.p.setText(model.getPrice());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final DialogPlus dialogPlus  = DialogPlus.newDialog(holder.n.getContext())
                    .setContentHolder(new ViewHolder(R.layout.update_popup))
                    .setExpanded(true,1200)
                    .create();



                View view = dialogPlus.getHolderView();
                EditText prname = view.findViewById(R.id.pntxt);
                EditText prprice = view.findViewById(R.id.ptxt);
                EditText prquan = view.findViewById(R.id.qttxt);

                Button upd = view.findViewById(R.id.updatebtn);
                Button del = view.findViewById(R.id.delbtn);
                prname.setText(model.getName());
                prprice.setText(model.getPrice());
                prquan.setText(model.getQuantity());

                dialogPlus.show();

                upd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("name",prname.getText().toString());
                        map.put("quantity",prquan.getText().toString());
                        map.put("price",prprice.getText().toString());

                        FirebaseDatabase.getInstance("https://agriculture-juan-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Stocks")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.n.getContext(),"Updated!",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.n.getContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });




            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        AlertDialog.Builder builder  = new AlertDialog.Builder(holder.n.getContext());
                        builder.setTitle("Are you sure?");
                        builder.setMessage("This action can't be undo.");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance("https://agriculture-juan-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Stocks")
                                        .child(getRef(position).getKey()).removeValue();
                                Toast.makeText(holder.n.getContext(),"Deleted!",Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(holder.n.getContext(),"Cancelled!",Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.show();
                    }


        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView n,p,q;
        Button btnEdit,btnDel;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            n = itemView.findViewById(R.id.pval1);
            q = itemView.findViewById(R.id.pval2);
            p = itemView.findViewById(R.id.pval3);


            btnEdit = itemView.findViewById(R.id.editbtn);
            btnDel = itemView.findViewById(R.id.delbtn);


        }

    }

}
