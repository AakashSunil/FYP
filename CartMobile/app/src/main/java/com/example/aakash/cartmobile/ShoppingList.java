package com.example.aakash.cartmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShoppingList extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    Button pre,check;
    String bar,action,proName,otpstr;
    Bundle b;
    float price;

    private Firebase mRef;

    TextView amt,code,name,itemhead,itemcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);


        Firebase.setAndroidContext(this);

        amt = findViewById(R.id.Amount);
        code = findViewById(R.id.bar);
        pre = findViewById(R.id.CS);
        check = findViewById(R.id.check);
        name = findViewById(R.id.name);
        itemhead = findViewById(R.id.ItemCount);
        itemcount = findViewById(R.id.Items);
        b = getIntent().getExtras();
        bar = b.getString("Barcode");
        action = b.getString("Action");
        otpstr = b.getString("OTP");


        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Previous(v);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Billing(v);
            }
        });


        if(action.isEmpty()||bar.isEmpty())
        {
            amt.setText(String.valueOf(MainActivity.Amount_wallet));
            itemcount.setText(String.valueOf(MainActivity.item_count));
            Toast.makeText(this,"Item Not Found",Toast.LENGTH_LONG).show();
        }
        else {

            String url = "https://test-kit-1.firebaseio.com/" + bar;
            mRef = new Firebase(url);

            if (action.equalsIgnoreCase("Add")) {

                mRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                        try {

                            proName = dataSnapshot.child("Product Name: ").getValue(String.class);
                            price = dataSnapshot.child("Product Price: ").getValue(Float.class);

                            if ((MainActivity.Amount_wallet - price) < 0) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ShoppingList.this);
                                builder1.setMessage("Not Enough Balance....No Shopping\nAdd Balance?");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {

                                                Intent intent = new Intent(ShoppingList.this, UpdateBalance.class);
                                                startActivity(intent);

                                            }
                                        });

                                builder1.setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                amt.setText(String.valueOf(MainActivity.Amount_wallet));
                                itemcount.setText(String.valueOf(MainActivity.item_count));

                            } else {
                                code.setText(bar);
                                name.setText(proName);
                                MainActivity.Amount_wallet -= price;
                                amt.setText(String.valueOf(MainActivity.Amount_wallet));
                                MainActivity.item_count++;
                                itemcount.setText(String.valueOf(MainActivity.item_count));
                            }
                        } catch (Exception e) {


                            Log.e("ERROR","exception",e);
                            amt.setText(String.valueOf(MainActivity.Amount_wallet));
                            itemcount.setText(String.valueOf(MainActivity.item_count));
                            Toast.makeText(ShoppingList.this, "Item Not Found in the Shop", Toast.LENGTH_LONG).show();
                            /*Intent intent = new Intent(ShoppingList.this, ShoppingList.class);
                            Bundle check = new Bundle();
                            check.putString("Barcode", "");
                            check.putString("Action", "");
                            intent.putExtras(check);
                            startActivity(intent);*/
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
            else
            {
                mRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        try {
                            proName = dataSnapshot.child("Product Name: ").getValue(String.class);
                            price = dataSnapshot.child("Product Price: ").getValue(Float.class);

                            if (MainActivity.item_count - 1 >= 0) {

                                MainActivity.item_count--;
                                MainActivity.Amount_wallet += price;
                                itemcount.setText(String.valueOf(MainActivity.item_count));
                                amt.setText(String.valueOf(MainActivity.Amount_wallet));
                                name.setText(proName);
                                code.setText(bar);
                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ShoppingList.this);
                                builder1.setMessage("Cannot add to the shop.....Pfft!!!");
                                builder1.setCancelable(false);

                                builder1.setPositiveButton(
                                        "Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                amt.setText(String.valueOf(MainActivity.Amount_wallet));
                                itemcount.setText(String.valueOf(MainActivity.item_count));

                            }
                        } catch (Exception e) {

                            amt.setText(String.valueOf(MainActivity.Amount_wallet));
                            itemcount.setText(String.valueOf(MainActivity.item_count));
                            Toast.makeText(ShoppingList.this,"Item Not Found in the Cart",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
    }

    public void Previous(View v) {
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
        intent.putExtra(BarcodeScannerActivity.AutoFocus, true);
    }
    public void Billing(View v) {
        Intent intent = new Intent(this, Checkout.class);
        startActivity(intent);

    }

    public void remove(View v){

        Intent intent = new Intent(ShoppingList.this,ItemRemoval.class);
        startActivity(intent);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.BarcodeObject);
                    Intent next = new Intent(this,ShoppingList.class);
                    Bundle b = new Bundle();
                    b.putString("Barcode",barcode.displayValue);
                    b.putString("Action","Add");
                    next.putExtras(b);
                    startActivity(next);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    //Toast.makeText(this, "Barcode Failed!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                //Toast.makeText(this, "Barcode error!", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {

        // Simply Do noting!
    }
}
