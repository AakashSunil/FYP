package com.example.aakash.userapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class newuser extends AppCompatActivity {

    EditText name,age,wallet,phone;
    TextView otp;
    Button fire,otpgen;

    String urluser = "https://test-kit-1-users.firebaseio.com/";
    String urlotp = "https://test-kit-1-otpgengkit.firebaseio.com/";

    DatabaseReference mRef,mRef1;

    int min = 10000;
    int max = 99999;
    int val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);
        Firebase.setAndroidContext(this);

        name = findViewById(R.id.Name);
        age = findViewById(R.id.Age);
        wallet = findViewById(R.id.Wallet);
        otp = findViewById(R.id.OTP);
        phone = findViewById(R.id.Phone);
        fire = findViewById(R.id.Submit);


        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage(v);
            }
        });

    }

    public void storage(View v)
    {
        if(name.getText().toString().isEmpty() || age.getText().toString().isEmpty() || wallet.getText().toString().isEmpty()||phone.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Please Enter all the Fields Correctly",Toast.LENGTH_LONG).show();
        }
        else
        {
            mRef = FirebaseDatabase.getInstance(urluser).getReference();
            mRef.child(phone.getText().toString()).child("Name").setValue(name.getText().toString());
            mRef.child(phone.getText().toString()).child("Age").setValue(Integer.valueOf(age.getText().toString()));
            mRef.child(phone.getText().toString()).child("Wallet Balance").setValue(Float.valueOf(wallet.getText().toString()));

            mRef1 = FirebaseDatabase.getInstance(urlotp).getReference();
            val = min + (int)(Math.random() * ((max-min) + 1));
            otp.setText(Integer.toString(val));
            mRef1.child(Integer.toString(val)).child("Phone:").setValue(phone.getText().toString());
            mRef1.child(Integer.toString(val)).child("Wallet:").setValue(Float.valueOf(wallet.getText().toString()));
            mRef1.child(Integer.toString(val)).child("Name:").setValue(name.getText().toString());
            mRef1.child(Integer.toString(val)).child("Age:").setValue(Integer.valueOf(age.getText().toString()));

        }
    }
}
