package com.example.cdb9t2final;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



public class MainActivity extends AppCompatActivity {
    //url of thermometer server
    //using Flask to serve JSON content, so port 5000
    static final String serverURL = "http://ec2-3-15-14-91.us-east-2.compute.amazonaws.com:5000/all";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug","console!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView text=findViewById(R.id.testDisplay);
        fetchTemps(serverURL);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchTemps(serverURL);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchTemps(String serverURL){
        //contacts the server and retrieves the data
        //JSON format, contains temperature, name of thermometer, and datetime of when it was last updated
        //Will have to be asynchronous

        final TextView textView = (TextView)findViewById(R.id.testDisplay);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

    // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText(new String(error.networkResponse.data,"utf-8");
                textView.setText("Failed" + error.toString());
                Log.d("debug", error.toString());
            }
        });

        queue.add(stringRequest);
    }


}
