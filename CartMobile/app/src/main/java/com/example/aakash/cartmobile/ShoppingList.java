/**
 *
 *  Main Shopping interface of the application
 *
 *  It basically shows the last item scanned into the cart along with its quantity and price
 *
 *  It has the options to continue shopping, remove item and checkout
 *
 */
package com.example.aakash.cartmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
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

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingList extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    Button pre,check;
    String bar,action,proName,otpstr;
    String proname,value,quant;
    Bundle b;
    float price;

    int quantity=0;
    private Firebase mRef,mRef2;

    DatabaseReference databaseReference,mRef1;

    static ArrayList<ListItems> list = new ArrayList<>();

    RecyclerView recyclerView;

    RecyclerView.Adapter adapter;


    TextView amt,code,name,itemhead,itemcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);


        Firebase.setAndroidContext(this);

        recyclerView = findViewById(R.id.Recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShoppingList.this));

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

        try{

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    databaseReference = FirebaseDatabase.getInstance("https://test-kit-1-shoplist.firebaseio.com/").getReference(MainActivity.otpstr + "/" + bar);
                    databaseReference.keepSynced(true);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {


                            for (int i = 0; i < 1; i++) {
                                if (!snapshot.hasChild("null")) {

                                    proname = String.valueOf(snapshot.child("Name:").getValue());
                                    value = String.valueOf(snapshot.child("Price:").getValue(Float.class));
                                    quant = String.valueOf(snapshot.child("Quantity:").getValue(Integer.class));

                                    list.add(new ListItems(proName, value, quant));
                                    adapter = new RecyclerViewAdapter(ShoppingList.this, list);
                                    recyclerView.setAdapter(adapter);

                                }
                            }


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            },5000);


    }
    catch(Exception e)
    {
    }


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

                                mRef1 = FirebaseDatabase.getInstance("https://test-kit-1-shoplist.firebaseio.com/").getReference();

                                String urllist = "https://test-kit-1-shoplist.firebaseio.com/";
                                mRef2 = new Firebase(urllist);
                                try {


                                    mRef2.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                                        @Override
                                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                                            try {

                                                if(!dataSnapshot.hasChild(MainActivity.otpstr)) {

                                                    mRef1.child(MainActivity.otpstr);
                                                }
                                                if(!dataSnapshot.child(MainActivity.otpstr).hasChild(bar)) {

                                                    mRef1.child(MainActivity.otpstr).child(bar);
                                                    mRef1.child(MainActivity.otpstr).child(bar).child("Name:").setValue(proName);
                                                    mRef1.child(MainActivity.otpstr).child(bar).child("Price:").setValue(price);
                                                }
                                                if(dataSnapshot.child(MainActivity.otpstr).child(bar).hasChild("Quantity:"))
                                                {
                                                    quantity = dataSnapshot.child(MainActivity.otpstr).child(bar).child("Quantity:").getValue(Integer.class);
                                                    mRef1.child(MainActivity.otpstr).child(bar).child("Quantity:").setValue(Integer.valueOf(quantity + 1));
                                                }
                                                else
                                                {
                                                    mRef1.child(MainActivity.otpstr).child(bar).child("Quantity:").setValue(Integer.valueOf(1));
                                                }

                                            }
                                            catch (Exception e){

                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                                catch (Exception e)
                                {

                                }
                            }
                        } catch (Exception e) {


                            Log.e("ERROR","exception",e);
                            amt.setText(String.valueOf(MainActivity.Amount_wallet));
                            itemcount.setText(String.valueOf(MainActivity.item_count));
                            Toast.makeText(ShoppingList.this, "Item Not Found in the Shop", Toast.LENGTH_LONG).show();
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

                                mRef2 = new Firebase("https://test-kit-1-shoplist.firebaseio.com/"+MainActivity.otpstr+"/"+bar);
                                try {

                                    mRef2.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                                        @Override
                                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                                                if(!dataSnapshot.hasChild(MainActivity.otpstr)) {

                                                }
                                                if(!dataSnapshot.child(MainActivity.otpstr).hasChild(bar)) {

                                                }
                                                if(dataSnapshot.hasChild("Quantity:"))
                                                {
                                                    quantity = dataSnapshot.child("Quantity:").getValue(Integer.class);
                                                    if(quantity - 1 == 0)
                                                    {
                                                        mRef2.removeValue();
                                                    }
                                                    else
                                                    {
                                                        mRef2.child("Quantity:").setValue(Integer.valueOf(quantity - 1));
                                                    }

                                                }
                                                else
                                                {
                                                    Toast.makeText(ShoppingList.this,"Item Not Found in the ---Cart----",Toast.LENGTH_LONG).show();
                                                }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                                catch (Exception e)
                                {

                                }
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
/*    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        TextView pronme,proprce,proquant;
        public BlogViewHolder(View itemView) {
            super(itemView);
            pronme = itemView.findViewById(R.id.ShowProductNameTextView);
            proprce = itemView.findViewById(R.id.ShowProductPriceTextView);
            proquant = itemView.findViewById(R.id.ShowProductQuantityTextView);
        }

        public void setProductName(String productName) {

            pronme.setText(productName);
        }

        public void setProductPrice(String productPrice) {

            proprce.setText(productPrice);
        }

        public void setProductQuantity(String productQuantity) {

            proquant.setText(productQuantity);
        }
    }*/
    @Override
    public void onBackPressed() {

        // Simply Do noting!
    }
}
