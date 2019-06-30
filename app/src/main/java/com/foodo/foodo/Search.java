package com.foodo.foodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class Search extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText search;
    private LinearLayout reslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reslist = findViewById(R.id.reslist);

        //search text input
        search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getApplicationContext(),"typing",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String tosearch = search.getText().toString();
                if(tosearch.length()>2){
                    reslist.removeAllViews();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.getsearch,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(!response.trim().equals("invalid")){
                                        try {
                                            //converting the string to json array object
                                            JSONObject obj = new JSONObject(response);
                                            JSONArray array = obj.getJSONArray("ressearch");
                                            //Toast.makeText(getBaseContext(),response.trim(), Toast.LENGTH_SHORT).show();
                                            //Retrieving response
                                            for (int i = 0; i < array.length(); i++) {

                                                //getting product object from json array
                                                JSONObject user = array.getJSONObject(i);
                                                final String id = user.getString("ID");
                                                final String name =  user.getString("Name");
                                                String add =  user.getString("Address");
                                                String city =  user.getString("City");
                                                //show result
                                                View div = new View(getBaseContext());
                                                div.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));
                                                div.setBackgroundResource(R.color.colorPrimary);
                                                reslist.addView(div);
                                                LinearLayout single = new LinearLayout(getBaseContext());
                                                single.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                                single.setOrientation(LinearLayout.HORIZONTAL);
                                                single.setPadding(20,20,20,20);
                                                single.setBackgroundColor(Color.parseColor("#eeeeee"));
                                                reslist.addView(single);
                                                ImageView thumb = new ImageView(getBaseContext());
                                                thumb.setLayoutParams(new LinearLayout.LayoutParams(200,200));
                                                thumb.setImageResource(R.drawable.logo);
                                                single.addView(thumb);
                                                LinearLayout ttl = new LinearLayout(getBaseContext());
                                                ttl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                                ttl.setOrientation(LinearLayout.VERTICAL);
                                                ttl.setPadding(25,0,0,0);
                                                single.addView(ttl);
                                                TextView rname = new TextView(getBaseContext());
                                                rname.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                                rname.setText(name);
                                                rname.setTextSize(18);
                                                rname.setTypeface(Typeface.DEFAULT_BOLD);
                                                TextView rad = new TextView(getBaseContext());
                                                rad.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                                rad.setText(add+", "+city);
                                                rad.setTextSize(14);
                                                ttl.addView(rname);
                                                ttl.addView(rad);

                                                single.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //Toast.makeText(getBaseContext(),id,Toast.LENGTH_SHORT).show();
                                                        Intent menu = new Intent(Search.this, Menu.class);
                                                        menu.putExtra("RES_ID", id);
                                                        menu.putExtra("RES_NAME", name);
                                                        startActivity(menu);
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
                            params.put("tosearch", tosearch);
                            return params;
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
                }else{
                    reslist.removeAllViews();
                }
            }
        });
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
