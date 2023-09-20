package com.example.forminputexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HellloListActivity extends AppCompatActivity {

    static final String[] WEEKDAYS = new String[] {
            "Monday", "Tuesday","Wednesday", "Thursday","Friday","Saturday","Sunday"
    };
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helllo_list);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new ArrayAdapter<String>(this,R.layout.list_item,WEEKDAYS));
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),((TextView)view).getText(),Toast.LENGTH_LONG).show();
            }
        });
    }
}