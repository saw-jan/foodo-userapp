package com.foodo.foodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Checkout extends AppCompatActivity {

    DatabaseHelper myDB;
    private TextView name,subtotal,vat,total;
    private ImageView del;
    private Button order;
    private LinearLayout itmlist;
    static final String PREFS = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("PLACE ORDER");

        SharedPreferences User = getSharedPreferences(PREFS,0);
        final String UID = User.getString("uid","");

        myDB = new DatabaseHelper(getApplicationContext());
        name = findViewById(R.id.name);
        subtotal = findViewById(R.id.subtotal);
        vat = findViewById(R.id.vat);
        total = findViewById(R.id.total);
        del = findViewById(R.id.del);
        order = findViewById(R.id.order);
        itmlist = findViewById(R.id.itm_list);

        Intent intent = getIntent();
        final String rID = intent.getStringExtra("R_ID");
        final String rNAME = intent.getStringExtra("R_NAME");

        name.setText(rNAME);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.deleteData(rID);
                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
                Intent basket = new Intent(getApplicationContext(),MyBasket.class);
                startActivity(basket);
                finish();
            }
        });
        Cursor sTtl = myDB.getItemSum(rID);
        String sub="0.00";
        if(sTtl.getCount() != 0){
            while (sTtl.moveToNext()){
                sub = sTtl.getString(0);
                subtotal.setText("Rs. "+sub);
                break;
            }
        }
        Double vatamt = (0.13 * Double.parseDouble(sub));
        vat.setText("Rs. "+vatamt.toString());
        final Double gtotal = Double.parseDouble(sub) + vatamt;
        total.setText("Rs. "+gtotal.toString());

        String[] itm_names = new String[10];
        String[] itm_qty = new String[10];
        Cursor items = myDB.getItems(rID);
        if(items.getCount() != 0){
            Integer i =0;
            while (items.moveToNext()) {
                final String item = items.getString(0);
                final String price = items.getString(1);
                final String qty = items.getString(2);

                itm_names[i] = item;
                itm_qty[i] = qty;
                i++;

                final LinearLayout row = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams pcm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(pcm);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(0,0,0,20);
                itmlist.addView(row);
                final TextView iname = new TextView(getApplicationContext());
                LinearLayout.LayoutParams p0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,40);
                p0.weight = 5;
                iname.setLayoutParams(p0);
                iname.setGravity(Gravity.LEFT);
                iname.setText(item+" ("+qty+")");
                final TextView iprice = new TextView(getApplicationContext());
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,40);
                p1.weight = 5;
                iprice.setLayoutParams(p0);
                iprice.setGravity(Gravity.RIGHT);
                iprice.setText("Rs. "+price);
                row.addView(iname);
                row.addView(iprice);
            }

            Integer a=0;
            String names="";
            String qties="";
            while (itm_names[a]!=null){
                names +=itm_names[a]+",";
                qties +=itm_qty[a]+",";
                a++;
            }
            final String finalNames = names;
            final String finalQties = qties;
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),finalNames,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Ordered",Toast.LENGTH_SHORT).show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.neworder,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Toast.makeText(getApplicationContext(),response.trim().toString()+"response",Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("UID",UID);
                            params.put("RID",rID);
                            params.put("ITEMS", finalNames);
                            params.put("QTY", finalQties);
                            params.put("TOTAL",gtotal.toString());
                            return params;
                        }
                    };
                    Volley.newRequestQueue(getBaseContext()).add(stringRequest);

                    myDB.deleteData(rID);
                    Intent main = new Intent(getApplicationContext(),MyBasket.class);
                    startActivity(main);
                    finish();
                }
            });
        }

    }
}
