/**
 *  Java class that handles item removal....only scanning the item is the main job at hand
 */
package com.example.aakash.cartmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class ItemRemoval extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(ItemRemoval.this, BarcodeScannerActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
        intent.putExtra(BarcodeScannerActivity.AutoFocus, true);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {

                    Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.BarcodeObject);
                    Intent next = new Intent(this,ShoppingList.class);
                    Bundle b = new Bundle();
                    b.putString("Barcode",barcode.displayValue);
                    b.putString("Action","Remove");
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
}