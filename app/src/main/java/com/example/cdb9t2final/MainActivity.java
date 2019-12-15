package com.example.cdb9t2final;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.content.DialogInterface;

import org.json.JSONObject;
import org.json.JSONArray;


public class MainActivity extends AppCompatActivity {
    //url of thermometer server
    //using Flask to serve JSON content, so port 5000
    //Would be best to make this dynamic, but for this particular implementation there isn't much point
    static final String serverURL = "http://ec2-3-15-14-91.us-east-2.compute.amazonaws.com:5000/all";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thermometer[] thermometers;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fetchTemps(serverURL);

        //Floating Action Button is used to refresh the temperature data
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
        //https://developer.android.com/training/volley/simple.html

        final TextView textView = (TextView)findViewById(R.id.testDisplay);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        display(parseJSON(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("");
                createAlertDialog("Could not connect to server",error.toString());
                Log.d("debug", error.toString());
            }
        });

        //actually perform the request
        queue.add(stringRequest);
    }

    public void createAlertDialog(String title, String message){
        //Module 6 : Alert Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Could not connect to server");
        alertDialogBuilder.setMessage(message);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void display(Thermometer[] thermometers){
        //will change. just the simple textview
        final TextView textView = (TextView)findViewById(R.id.testDisplay);
        final TableLayout tableLayout = (TableLayout)findViewById(R.id.contentTable);

        Log.d("debug",Integer.toString(tableLayout.getChildCount()));


        //if(tableLayout.getChildCount()!=0) tableLayout.removeViews(0,thermometers.length);

        Log.d("debug",Integer.toString(tableLayout.getChildCount()));
        Log.d("debug","length: " + Integer.toString(thermometers.length));

        tableLayout.setStretchAllColumns(true);
        tableLayout.bringToFront();
        //https://stackoverflow.com/questions/18049708/how-to-create-a-dynamic-table-of-data-in-android/47495055
        //Allows for any number of rows

        //initialize table
            for (int i = 0; i < thermometers.length; i++) {
                TableRow tr = new TableRow(this);
                TextView c1 = new TextView(this);
                c1.setText(Integer.toString(thermometers[i].getTemp()));
                TextView c2 = new TextView(this);
                c2.setText(Integer.toString(thermometers[i].getId()));
                TextView c3 = new TextView(this);
                c3.setText(thermometers[i].getName());
                //Screwed up the order on these lol
                tr.addView(c2);
                tr.addView(c3);
                tr.addView(c1);
                tableLayout.addView(tr);
                Log.d("debug", "loop");
            }
    }

    //will take the String returned by the server and put it into an array of Thermometers
    public Thermometer[] parseJSON(String jsonString){
        try{JSONArray jArray = new JSONArray(jsonString);
            //create empty array
            Thermometer[] thermometers = new Thermometer[jArray.length()];
            for(int i = 0; i < jArray.length(); i++) {
                //iterate through JSONObject, create new thermometer object for each item in JSONArray, put it in thermometers[]
                JSONObject obj = jArray.getJSONObject(i);
                thermometers[i] = new Thermometer(obj.getInt("id"),obj.getInt("temp"),obj.getString("name"));
            }
            return thermometers;
        }catch(Exception e){
            //display error
            createAlertDialog("Data appears to be corrupted or could otherwise not be handled",e.toString());
        }

        return null;
    }


}
