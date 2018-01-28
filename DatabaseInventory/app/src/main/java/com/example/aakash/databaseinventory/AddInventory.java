package com.example.aakash.databaseinventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aakash.databaseinventory.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddInventory extends AppCompatActivity {

    String BarCode;
    TextView text;
    EditText name,price,quantity,unit;
    Button btn;

    DatabaseReference mDatabase,item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        //FirebaseApp.initializeApp(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle b = getIntent().getExtras();
        BarCode = b.getString("Barcode");



        name = findViewById(R.id.name);
        price = findViewById(R.id.Price1);
        unit = findViewById(R.id.Unit1);
        text = findViewById(R.id.Barcode);

        text.setText(BarCode);
        btn = findViewById(R.id.Upload);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData(v);
            }
        });
    }
    public void UpdateData(View v)
    {
        Map<String, Object> items = new HashMap<>();
        item = mDatabase.child(BarCode);
        items.put("Product Name: ",name.getText().toString());
        items.put("Product Price: ",Float.valueOf(price.getText().toString()));
        items.put("Product Units: ",Integer.valueOf(unit.getText().toString()));
        item.updateChildren(items);
    }

}
