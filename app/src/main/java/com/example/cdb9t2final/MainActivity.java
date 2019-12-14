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


public class MainActivity extends AppCompatActivity {
    //url of thermometer server
    //using Flask to serve JSON content, so port 5000
    static final String URL = "http://ec2-3-15-14-91.us-east-2.compute.amazonaws.com:5000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug","console!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView text=findViewById(R.id.testDisplay);
        text.setText(fetchTemps(URL, text));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    public String fetchTemps(String serverURL, TextView textView){
        //contacts the server and retrieves the data
        //JSON format, contains temperature, name of thermometer, and datetime of when it was last updated
        //Will have to be asynchronous

        String result;
        HttpGetRequest getRequest = new HttpGetRequest();
        try{
            result = getRequest.execute(URL).get();
            Log.d("debug","Called getRequest.execute");
        }catch(Exception e){
            result = "Could not connect to server";
        }

        textView.setText(result);

        return result;   //change return type to a usable array
    }

}
