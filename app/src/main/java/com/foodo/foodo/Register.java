package com.foodo.foodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {

    EditText fname,lname,phone,email,password,cpassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");

    }

    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void register(View view) {
        //Toast.makeText(getBaseContext(),"Register",Toast.LENGTH_SHORT).show();
        //textbox and buttons
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);

        final String name = fname.getText().toString().replaceAll("\\s+","")+" "+lname.getText().toString().replaceAll("\\s+","");
        final String ph_num = phone.getText().toString().replace("\\s+","");
        final String e_mail = email.getText().toString().replace("\\s+","");
        final String pass = password.getText().toString();
        String cpass = cpassword.getText().toString().replace("\\s+","");

        if(!name.equals("") && !ph_num.equals("") && !pass.equals("") && !cpass.equals("")){
            if(pass.equals(cpass)){
            //Register new user
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.registerUser,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           if(response.trim().equals("inserted")){
                               Toast.makeText(getBaseContext(),"You are now registered.",Toast.LENGTH_SHORT).show();
                               Intent login = new Intent(Register.this, Login.class);
                               startActivity(login);
                               finish();
                           }else {
                               Toast.makeText(getBaseContext(),"Network Error",Toast.LENGTH_SHORT).show();
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
                    params.put("Name", name);
                    params.put("Contact", ph_num);
                    params.put("Email", e_mail);
                    params.put("Password", pass);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(stringRequest);
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
