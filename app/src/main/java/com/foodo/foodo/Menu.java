package com.foodo.foodo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Menu extends AppCompatActivity {

    private TextView fLet,rName,rAdd,rCity;
    private LinearLayout mlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Menu");

        fLet = findViewById(R.id.rLeft);
        rName = findViewById(R.id.rName);
        rAdd = findViewById(R.id.rAdd);
        rCity = findViewById(R.id.rCity);
        mlist = findViewById(R.id.menulist);

        Intent intent = getIntent();
        final String rID = intent.getStringExtra("RES_ID");
        final String rNAME = intent.getStringExtra("RES_NAME");
        //Toast.makeText(getApplicationContext(),rID,Toast.LENGTH_SHORT).show();
        //fetch this restaurant
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.getthisres,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.trim().equals("invalid")){
                            try {
                                //converting the string to json array object
                                JSONObject obj = new JSONObject(response);
                                JSONArray array = obj.getJSONArray("res");
                                //Toast.makeText(getBaseContext(),response.trim(), Toast.LENGTH_SHORT).show();
                                //Retrieving response
                                String id="";
                                String name="";
                                String add="";
                                String city="";
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject user = array.getJSONObject(i);
                                    id = user.getString("ID");
                                    name =  user.getString("Name");
                                    add =  user.getString("Address");
                                    city =  user.getString("City");
                                }
                                fLet.setText(name.substring(0,1));
                                rName.setText(name);
                                rAdd.setText(add+",");
                                rCity.setText(city);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            //Toast.makeText(getBaseContext(),"", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rID", rID);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        //res menu list
        StringRequest stringReq = new StringRequest(Request.Method.POST, Api.getresmenu,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.trim().equals("invalid")){
                            try {
                                //converting the string to json array object
                                JSONObject obj = new JSONObject(response);
                                JSONArray array = obj.getJSONArray("resmenu");
                                //Toast.makeText(getBaseContext(),response.trim(), Toast.LENGTH_SHORT).show();
                                //Retrieving response
                                String cateogry="";
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject user = array.getJSONObject(i);
                                    final String mid = user.getString("mID");
                                    final String itm_name =  user.getString("Item");
                                    final String price =  user.getString("Price");
                                    String cat =  user.getString("mCat");
                                    LinearLayout menul = new LinearLayout(getBaseContext());
                                    if(!cat.equals(cateogry)){
                                        TextView menuc = new TextView(getBaseContext());
                                        LinearLayout.LayoutParams pcm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        pcm.setMargins(0,10,0,10);
                                        menuc.setLayoutParams(pcm);
                                        menuc.setText(cat);
                                        menuc.setTextSize(16);
                                        menuc.setPadding(10,10,10,10);
                                        menuc.setTypeface(Typeface.DEFAULT_BOLD);
                                        menuc.setBackgroundColor(Color.parseColor("#999999"));
                                        menuc.setTextColor(Color.parseColor("#ffffff"));
                                        mlist.addView(menuc);
                                        menul.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                        menul.setOrientation(LinearLayout.HORIZONTAL);
                                        menul.setPadding(10,20,10,20);
                                        TextView itm = new TextView(getBaseContext());
                                        LinearLayout.LayoutParams pn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,40);
                                        pn.weight = 80;
                                        itm.setLayoutParams(pn);
                                        itm.setText(itm_name);
                                        itm.setGravity(Gravity.LEFT);
                                        TextView pric = new TextView(getBaseContext());
                                        LinearLayout.LayoutParams p0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,40);
                                        p0.weight = 40;
                                        pric.setLayoutParams(p0);
                                        pric.setText("RS. "+price);
                                        pric.setGravity(Gravity.RIGHT);
                                        pric.setTypeface(Typeface.DEFAULT_BOLD);
                                        menul.addView(itm);
                                        menul.addView(pric);
                                        mlist.addView(menul);
                                        cateogry = cat;
                                    }else{
                                        menul.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                        menul.setOrientation(LinearLayout.HORIZONTAL);
                                        menul.setPadding(10,20,10,20);
                                        TextView itm = new TextView(getBaseContext());
                                        LinearLayout.LayoutParams pn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,40);
                                        pn.weight = 80;
                                        itm.setLayoutParams(pn);
                                        itm.setText(itm_name);
                                        itm.setGravity(Gravity.LEFT);
                                        TextView pr = new TextView(getBaseContext());
                                        LinearLayout.LayoutParams p0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,40);
                                        p0.weight = 40;
                                        pr.setLayoutParams(p0);
                                        pr.setText("RS. "+price);
                                        pr.setGravity(Gravity.RIGHT);
                                        pr.setTypeface(Typeface.DEFAULT_BOLD);
                                        menul.addView(itm);
                                        menul.addView(pr);
                                        mlist.addView(menul);
                                    }
                                    menul.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Toast.makeText(getBaseContext(),mid, Toast.LENGTH_SHORT).show();
                                            Intent item = new Intent(Menu.this, Item.class);
                                            item.putExtra("R_ID",rID);
                                            item.putExtra("R_NAME",rNAME);
                                            item.putExtra("I_ID",mid);
                                            item.putExtra("I_NAME",itm_name);
                                            item.putExtra("I_PRICE",price);
                                            startActivity(item);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            //Toast.makeText(getBaseContext(),"", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rID", rID);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringReq);
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
