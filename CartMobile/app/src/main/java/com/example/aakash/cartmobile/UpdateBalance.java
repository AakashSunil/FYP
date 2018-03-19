package com.example.aakash.cartmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.nearby.messages.internal.Update;
import com.google.android.gms.vision.barcode.Barcode;

public class UpdateBalance extends AppCompatActivity {

    EditText updatewallet;
    Button resume;
    Float updateamt;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_balance);

        updatewallet = findViewById(R.id.UpdateWallet);
        resume = findViewById(R.id.update);

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateamt = Float.valueOf(updatewallet.getText().toString());
                MainActivity.Amount_wallet += updateamt;
                Intent intent = new Intent(UpdateBalance.this, BarcodeScannerActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                intent.putExtra(BarcodeScannerActivity.AutoFocus, true);

            }
        });
    }
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
                    Toast.makeText(this,barcode.displayValue,Toast.LENGTH_LONG).show();
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
