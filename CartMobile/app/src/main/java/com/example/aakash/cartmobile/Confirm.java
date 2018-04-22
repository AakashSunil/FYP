/**
 *  Confirmation Page for user to verify the details before starting to shop
 */
package com.example.aakash.cartmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Confirm extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    DatabaseReference mRef;

    TextView name_field, balance_field;

    Button mButton;

    String otpstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        String urlhist = "https://test-kit-1-history.firebaseio.com/";
        mRef = FirebaseDatabase.getInstance(urlhist).getReference();

        name_field = findViewById(R.id.name);
        balance_field = findViewById(R.id.balance);
        mButton = findViewById(R.id.continueShop);
        Bundle b = getIntent().getExtras();
        String n = b.getString("Name");
        otpstr = b.getString("OTP");
        name_field.setText(n);
        balance_field.setText(String.valueOf(MainActivity.Amount_wallet));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Confirm.this, BarcodeScannerActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                intent.putExtra(BarcodeScannerActivity.AutoFocus, true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {

                    Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.BarcodeObject);
                    Intent next = new Intent(this, ShoppingList.class);
                    Bundle b = new Bundle();

                    b.putString("Barcode", barcode.displayValue);
                    b.putString("Action", "Add");
                    b.putString("OTP",otpstr);
                    next.putExtras(b);
                    startActivity(next);
                    //Toast.makeText(this,barcode.displayValue,Toast.LENGTH_LONG).show();
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