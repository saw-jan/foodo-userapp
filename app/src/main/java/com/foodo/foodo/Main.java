package com.foodo.foodo;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private static final int MY_MENU_1 = Menu.FIRST;
    private static final int MY_MENU_2 = Menu.FIRST+1;
    static final String PREFS = "user";
    AlertDialog.Builder alert;
    private TextView nr1,nr2,nr3,nr4,nr5,nrf1,nrf2,nrf3,nrf4,nrf5;
    private TextView hr1,hr2,hr3,hr4,hr5,hrf1,hrf2,hrf3,hrf4,hrf5;
    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        alert = new AlertDialog.Builder(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawerLayout);

        NavigationView navView = findViewById(R.id.drawermenu);
        navView.setNavigationItemSelectedListener(this);
        //dynamic header content
        View header = navView.getHeaderView(0);

        LinearLayout headerView = header.findViewById(R.id.headerView);
        TextView txtName = header.findViewById(R.id.name);
        TextView txtPhone = header.findViewById(R.id.phone);

        //log in status
        SharedPreferences User = getSharedPreferences(PREFS,0);
        String logged = User.getString("uid","");
        String user = User.getString("user","");
        String phone = User.getString("phone","");
        Menu menu = navView.getMenu();
        //dynamic menu item
        if(!logged.equals("")){
            //header
            txtName.setText(user.toUpperCase());
            txtPhone.setText(phone);
            //drawer
            menu.findItem(R.id.prefs)
                    .getSubMenu()
                    .add(0, MY_MENU_1, 0, "Logout");
        }else{
            //header
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,0,0);
            txtName.setLayoutParams(lp);
            txtName.setPadding(50,5,50,5);
            txtName.setText("LOGIN");
            txtName.setTextSize(25);
            txtName.setTextColor(Color.WHITE);
            txtName.setBackgroundResource(R.drawable.tb_border);
            txtName.setGravity(Gravity.CENTER);
            headerView.removeView(txtPhone);
            //drawer
            menu.findItem(R.id.prefs)
                    .getSubMenu()
                    .add(0, MY_MENU_2, 0, "Login");
        }
        //check for internet access
        /*if(!Validator.isNetAccess()){
            alert.setMessage("No  Internet Connection")
                    .setTitle("Network Error")
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                            }
                    });
            AlertDialog alt = alert.create();
            alt.show();
        }*/

        //new restaurants
        final String[] rid = new String[5];
        final String[] rname = new String[5];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.getnewres,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.trim().equals("invalid")){
                            try {
                                //converting the string to json array object
                                JSONObject obj = new JSONObject(response);
                                JSONArray array = obj.getJSONArray("newres");
                                //Toast.makeText(getBaseContext(),response.trim(), Toast.LENGTH_SHORT).show();
                                //Retrieving response
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject user = array.getJSONObject(i);
                                    rid[i] = user.getString("ID");
                                    rname[i] =  user.getString("Name");
                                    //Toast.makeText(getBaseContext(),rname[i],Toast.LENGTH_SHORT).show();
                                }
                                nr1 = findViewById(R.id.nr1);
                                nr1.setText(rname[0]);
                                nr2 = findViewById(R.id.nr2);
                                nr2.setText(rname[1]);
                                nr3 = findViewById(R.id.nr3);
                                nr3.setText(rname[2]);
                                nr4 = findViewById(R.id.nr4);
                                nr4.setText(rname[3]);
                                nr5 = findViewById(R.id.nr5);
                                nr5.setText(rname[4]);
                                nrf1 = findViewById(R.id.nrf1);
                                nrf1.setText(rname[0].substring(0,1));
                                nrf2 = findViewById(R.id.nrf2);
                                nrf2.setText(rname[1].substring(0,1));
                                nrf3 = findViewById(R.id.nrf3);
                                nrf3.setText(rname[2].substring(0,1));
                                nrf4 = findViewById(R.id.nrf4);
                                nrf4.setText(rname[3].substring(0,1));
                                nrf5 = findViewById(R.id.nrf5);
                                nrf5.setText(rname[4].substring(0,1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getBaseContext(),e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getBaseContext(),"Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);

        //hot restaurants
        final String[] hid = new String[5];
        final String[] hname = new String[5];
        StringRequest stringReq = new StringRequest(Request.Method.POST, Api.gethotres,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.trim().equals("invalid")){
                            try {
                                //converting the string to json array object
                                JSONObject obj = new JSONObject(response);
                                JSONArray array = obj.getJSONArray("hotres");
                                //Toast.makeText(getBaseContext(),response.trim(), Toast.LENGTH_SHORT).show();
                                //Retrieving response
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject user = array.getJSONObject(i);
                                    hid[i] = user.getString("ID");
                                    hname[i] =  user.getString("Name");
                                    //Toast.makeText(getBaseContext(),rname[i],Toast.LENGTH_SHORT).show();
                                }
                                hr1 = findViewById(R.id.hr1);
                                hr1.setText(hname[0]);
                                hr2 = findViewById(R.id.hr2);
                                hr2.setText(hname[1]);
                                hr3 = findViewById(R.id.hr3);
                                hr3.setText(hname[2]);
                                hr4 = findViewById(R.id.hr4);
                                hr4.setText(hname[3]);
                                hr5 = findViewById(R.id.hr5);
                                hr5.setText(hname[4]);
                                hrf1 = findViewById(R.id.hrf1);
                                hrf1.setText(hname[0].substring(0,1));
                                hrf2 = findViewById(R.id.hrf2);
                                hrf2.setText(hname[1].substring(0,1));
                                hrf3 = findViewById(R.id.hrf3);
                                hrf3.setText(hname[2].substring(0,1));
                                hrf4 = findViewById(R.id.hrf4);
                                hrf4.setText(hname[3].substring(0,1));
                                hrf5 = findViewById(R.id.hrf5);
                                hrf5.setText(hname[4].substring(0,1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getBaseContext(),e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getBaseContext(),"Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(this).add(stringReq);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                Intent search = new Intent(Main.this, Search.class);
                startActivity(search);
                finish();
                break;
            case R.id.cart:
                Intent basket = new Intent(getApplicationContext(),MyBasket.class);
                startActivity(basket);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add menu to toolbar
        getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        return true;
    }

    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int navID = item.getItemId();
        switch (navID){
            case R.id.ohistory:
                Toast.makeText(this,"Order History", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mbasket:
                Intent basket = new Intent(getApplicationContext(),MyBasket.class);
                startActivity(basket);
                break;
            case R.id.exit:
                System.exit(0);
                break;
            case MY_MENU_1:
                myDB = new DatabaseHelper(getApplicationContext());
                myDB.clearDB();
                //Toast.makeText(this, "Logout",Toast.LENGTH_SHORT).show();
                SharedPreferences User = getSharedPreferences(PREFS,0);
                SharedPreferences.Editor edit = User.edit();
                edit.putString("uid","");
                edit.putString("user","");
                edit.putString("phone","");
                edit.apply();

                Intent main = new Intent(getApplicationContext(),Main.class);
                startActivity(main);
                finish();
                break;
            case MY_MENU_2:
                Intent login = new Intent(getApplicationContext(), Login.class);
                startActivity(login);
                finish();
                break;

        }
        return  false;
    }
}