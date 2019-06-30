package com.foodo.foodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Item extends AppCompatActivity {

    private TextView itm_name,price,qty;
    private Button plus, minus,atc;
    DatabaseHelper myDB;
    static final String PREFS = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        myDB = new DatabaseHelper(getApplicationContext());

        SharedPreferences User = getSharedPreferences(PREFS,0);
        final String uid = User.getString("uid","");

        Intent intent = getIntent();
        final String rID = intent.getStringExtra("R_ID");
        final String rNAME = intent.getStringExtra("R_NAME");
        final String iID = intent.getStringExtra("I_ID");
        final String iName = intent.getStringExtra("I_NAME");
        final String iPrice = intent.getStringExtra("I_PRICE");

        itm_name = findViewById(R.id.itm_name);
        price = findViewById(R.id.price);
        qty = findViewById(R.id.qty);
        plus = findViewById(R.id.add);
        minus = findViewById(R.id.sub);
        atc = findViewById(R.id.atc);

        itm_name.setText(iName);
        price.setText("RS. "+iPrice);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer q = Integer.parseInt(qty.getText().toString());
                if(q ==00){
                    q = 0;
                }
                Integer count = q +1;
                qty.setText(count.toString());
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer q = Integer.parseInt(qty.getText().toString());
                if(q > 0) {
                    Integer count = q-1;
                    qty.setText(count.toString());
                }
            }
        });
        //add to  cart
        atc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!uid.equals("")){
                    final Integer q = Integer.parseInt(qty.getText().toString());
                    //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                    if(q > 0) {
                        //Toast.makeText(getApplicationContext(), rID + ", " + iID + ", " + iName + ", " + iPrice + ", " + q, Toast.LENGTH_SHORT).show();
                        myDB.insertData(rID,rNAME,iName,iPrice,q.toString(),uid);
                        //open my basket
                        Intent basket = new Intent(getApplicationContext(),MyBasket.class);
                        startActivity(basket);
                        finish();
                    }
                }else{
                    Intent login = new Intent(getApplicationContext(), Login.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }
}
