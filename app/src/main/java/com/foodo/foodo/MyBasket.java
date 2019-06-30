package com.foodo.foodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MyBasket extends AppCompatActivity {

    DatabaseHelper myDB;
    LinearLayout basket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_basket);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("MY BASKET");
        myDB = new DatabaseHelper(getApplicationContext());

        basket = findViewById(R.id.basket);
        Cursor res = myDB.getRes();
        //String resID = "";
        if(res.getCount() != 0){
        while (res.moveToNext()){
            final String rid = res.getString(0);
            final String rname = res.getString(1);

            //if(!resID.equals(rid)){
                final LinearLayout row = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams pcm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                pcm.setMargins(0,0,0,20);
                row.setLayoutParams(pcm);
                row.setOrientation(LinearLayout.VERTICAL);
                row.setPadding(20,20,20,20);
                row.setBackgroundColor(Color.parseColor("#eeeeee"));
                basket.addView(row);
                final TextView name = new TextView(getApplicationContext());
                name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,30));
                name.setText(rname);
                name.setTypeface(Typeface.DEFAULT_BOLD);
                name.setTextSize(16);

                Cursor itmTtl = myDB.getItemSum(rid);
                    String Total="0.00";
                    if(itmTtl.getCount() != 0){
                        while (itmTtl.moveToNext()){
                            Total = itmTtl.getString(0);
                        }
                        //Toast.makeText(getApplicationContext(),"yes",Toast.LENGTH_SHORT).show();
                        //
                    }
                final TextView ttl = new TextView(getApplicationContext());
                ttl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,30));
                ttl.setText("TOTAL: Rs. "+Double.parseDouble(Total));
                ttl.setTextSize(12);

                Cursor itmCount = myDB.getItemCount(rid);
                String Count="0";
                if(itmCount.getCount() != 0){
                    while (itmCount.moveToNext()){
                        Count = itmCount.getString(0);
                    }
                    //Toast.makeText(getApplicationContext(),"yes",Toast.LENGTH_SHORT).show();
                    //
                }
                final TextView itm = new TextView(getApplicationContext());
                itm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,30));
                itm.setText("( "+Count+" ) Item");
                itm.setTextSize(12);
                ImageView del = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams p0 = new LinearLayout.LayoutParams(100,100);
                p0.setMargins(0,30,0,0);
                del.setLayoutParams(p0);
                del.setBackgroundColor(Color.parseColor("#ff0000"));
                del.setImageResource(R.drawable.ic_delete);

                row.addView(name);
                row.addView(ttl);
                row.addView(itm);
                row.addView(del);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(),rid,Toast.LENGTH_SHORT).show();
                        Intent checkout = new Intent(getApplicationContext(),Checkout.class);
                        checkout.putExtra("R_ID",rid);
                        checkout.putExtra("R_NAME",rname);
                        startActivity(checkout);
                    }
                });
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDB.deleteData(rid);
                        Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(getApplicationContext(),MyBasket.class);
                        startActivity(main);
                        finish();
                    }
                });
        }
        }
    }
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
