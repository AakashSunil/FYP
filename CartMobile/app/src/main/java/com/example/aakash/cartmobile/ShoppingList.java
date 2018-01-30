package com.example.aakash.cartmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class ShoppingList extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    Button pre,check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        pre = findViewById(R.id.CS);
        check = findViewById(R.id.check);

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
    }

    public void Previous(View v) {
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
        intent.putExtra(BarcodeScannerActivity.AutoFocus, true);
    }
    public void Billing(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.BarcodeObject);
                    Intent next = new Intent(this,ShoppingList.class);
                    next.putExtra("Barcode",barcode.displayValue);
                    startActivity(next);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Toast.makeText(this, "Barcode Failed!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(this, "Barcode error!", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
