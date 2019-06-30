package com.foodo.foodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    private TextView register, forget;
    private EditText contact, password;
    //shared keys
    static final String PREFS = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Login");

        contact = findViewById(R.id.contact);
        password = findViewById(R.id.password);

        //new registration
        register = findViewById(R.id.new_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this, Register.class);
                startActivity(register);
                finish();
            }
        });

        //forget password
        forget = findViewById(R.id.forget_password);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Foreget password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(View view) {
        //Toast.makeText(getBaseContext(),"Login",Toast.LENGTH_SHORT).show();

        contact = findViewById(R.id.contact);
        password = findViewById(R.id.password);
        final String user_con = contact.getText().toString().replaceAll("\\s+","");
        final String user_pas = password.getText().toString().replaceAll("\\s+","");
        if(!user_con.equals("") && !user_pas.equals("")){
        //Register new user
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.getUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.trim().equals("invalid")){
                        try {
                            //converting the string to json array object
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("user");
                            //Toast.makeText(getBaseContext(),response.trim(), Toast.LENGTH_SHORT).show();
                            //Retrieving response
                           for (int i = 0; i < array.length(); i++) {

                               //getting product object from json array
                               JSONObject user = array.getJSONObject(i);
                               String uid = user.getString("ID");
                               String username =  user.getString("Username");
                               String uphone =  user.getString("Phone");

                               //store in shared prefs
                               SharedPreferences User = getSharedPreferences(PREFS,0);
                               SharedPreferences.Editor edit = User.edit();
                               edit.putString("uid",uid);
                               edit.putString("user",username);
                               edit.putString("phone",uphone);
                               edit.apply();
                           }
                            Intent login = new Intent(Login.this, Main.class);
                            startActivity(login);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }else {
                            Toast.makeText(getBaseContext(),"Invalid Login", Toast.LENGTH_SHORT).show();
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
                params.put("Contact", user_con);
                params.put("Password", user_pas);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
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
