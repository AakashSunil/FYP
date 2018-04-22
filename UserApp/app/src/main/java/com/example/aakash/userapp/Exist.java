/**
 *
 */
package com.example.aakash.userapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Exist extends AppCompatActivity {

    EditText mobile;
    Button check;
    TextView test;

    DatabaseReference mRef1;

    private static final int PERMISSION_REQUEST_CODE = 1;

    //String urluser = "https://test-kit-1-users.firebaseio.com/";
    String urlotp = "https://test-kit-1-otpgengkit.firebaseio.com/";

    Firebase mRef2;

    int min = 10000;
    int max = 99999;
    int val;

    Float amt;
    Integer age;
    String name = null, mob = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist);

        Firebase.setAndroidContext(this);

        mobile = findViewById(R.id.phone);
        test = findViewById(R.id.gen);
        check = findViewById(R.id.chk);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checking(v);
            }
        });
    }
    public void generate() {
        try {

            mRef1 = FirebaseDatabase.getInstance(urlotp).getReference();
            val = min + (int) (Math.random() * ((max - min) + 1));
            test.setText(Integer.toString(val));
            mRef1.child(Integer.toString(val)).child("Phone:").setValue(mobile.getText().toString());
            mRef1.child(Integer.toString(val)).child("Wallet:").setValue(Float.valueOf(amt));
            mRef1.child(Integer.toString(val)).child("Name:").setValue(name);
            mRef1.child(Integer.toString(val)).child("Age:").setValue(Integer.valueOf(age));

        }
        catch (Exception e)
        {

        }
    }
    public void checking(View v) {

        mob = mobile.getText().toString();
        test.setText(mob);
        String url = "https://test-kit-1-users.firebaseio.com/" + mob;
        mRef2 = new Firebase(url);
        mRef2.addValueEventListener(new com.firebase.client.ValueEventListener() {
        @Override
        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
            try {
                 age = dataSnapshot.child("Age").getValue(Integer.class);
                 name = dataSnapshot.child("Name").getValue(String.class);
                 amt = dataSnapshot.child("Wallet Balance").getValue(Float.class);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
        });
        try
        {
            if (amt == null || age == null) {

                Toast.makeText(Exist.this, "Checking...", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRef2.addValueEventListener(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                try
                                {
                                    age = dataSnapshot.child("Age").getValue(Integer.class);
                                    name = dataSnapshot.child("Name").getValue(String.class);
                                    amt = dataSnapshot.child("Wallet Balance").getValue(Float.class);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                },2000);
            }
            else
            {
                Toast.makeText(Exist.this, "Exists", Toast.LENGTH_LONG).show();
                generate();
                check.setClickable(false);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}