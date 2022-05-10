package com.example.agrijuan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
FirebaseAuth auth;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button sout;
    FirebaseAuth firebaseAuth;
    ImageView icon;
    TextView nme;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile.this,Dashboard.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        sout = findViewById(R.id.google_out);
        nme= findViewById(R.id.userfname);
        icon = findViewById(R.id.pfp);


        sout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(Profile.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog_one);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        signOut();
                    }
                },3500);

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser account = firebaseAuth.getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null || account != null){
            String pName = acct.getDisplayName();
            Uri photo = acct.getPhotoUrl();
            Picasso.with(this).load(photo).into(icon);
            nme.setText(pName);
        }else{
            signOut();
        }

    }
    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 finish();
                 startActivity(new Intent(Profile.this,Login.class));
            }
        });
    }
}