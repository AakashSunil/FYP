package com.example.aakash.cartmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {
    private Button mButton;

    public static Float Amount_wallet,Amount_Ref;
    public static int item_count;

    static EditText otp;
    static String otpstr,name,phone;
    TextView t,t1,t2,t3,t4;

    DatabaseReference sec;
    static int age;
    Float amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        otp = findViewById(R.id.OTP);
        t = findViewById(R.id.test);
        t1 = findViewById(R.id.test1);
        t2 = findViewById(R.id.test2);
        t3 = findViewById(R.id.test3);
        t4 = findViewById(R.id.test4);
        Amount_wallet = Float.valueOf((0));
        Firebase.setAndroidContext(this);
        mButton = findViewById(R.id.cont);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpstr = otp.getText().toString();

                switchActivity(v);
            }
        });
    }

    public void switchActivity(View v) {

        if(otpstr.isEmpty())
        {
            Toast.makeText(this,"Please Enter OTP",Toast.LENGTH_LONG).show();
        }
        else {


                sec = FirebaseDatabase.getInstance("https://test-kit-1-otpgengkit.firebaseio.com/").getReference(otpstr);

                sec.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                        try {

                            amt = dataSnapshot.child("Wallet:").getValue(Float.class);
                            name = dataSnapshot.child("Name:").getValue(String.class);
                            age = dataSnapshot.child("Age:").getValue(Integer.class);
                            phone = dataSnapshot.child("Phone:").getValue(String.class);

                            Amount_wallet += amt;

                            Amount_Ref = Amount_wallet;

                            finish();
                            Intent intent = new Intent(MainActivity.this,Confirm.class);
                            Bundle b = new Bundle();
                            b.putString("Name",name);
                            b.putString("OTP",otpstr);
                            intent.putExtras(b);
                            startActivity(intent);


                        } catch (Exception e) {

                            Toast.makeText(MainActivity.this,"Wrong OTP",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }
    }

    @Override
    public void onBackPressed() {

        // Simply Do noting!
    }
}
