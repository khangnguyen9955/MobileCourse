package com.example.forminputexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem i = menu.findItem(R.id.itemNext);
        i.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                handleButtonClick(null);
                return false;
            }
        });
        MenuItem iHelloList = menu.findItem(R.id.itemHelloList);
        iHelloList.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(getApplicationContext(), HellloListActivity.class);
                startActivity(i);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void handleButtonClick(View v) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
        if (!cb.isChecked()) {
            Toast t = Toast.makeText(this, "You must agree to the terms", Toast.LENGTH_LONG);
            t.show();
            return;
        } else {
            displayAlert();
//            Intent i = new Intent(this, MainActivity2.class);
//            startActivity(i);
        }
    }

    public void displayAlert() {
        EditText eName = (EditText) findViewById(R.id.editTextText);
        String name = eName.getText().toString();
        EditText eEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        String email = eEmail.getText().toString();
        EditText ePhone = (EditText) findViewById(R.id.editTextPhone);
        String phone = ePhone.getText().toString();
        RadioGroup workStatusGroup = (RadioGroup) findViewById(R.id.radioGroup4);
        RadioButton workStatusSelection = (RadioButton) findViewById(workStatusGroup.getCheckedRadioButtonId());
        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton genderSelection = (RadioButton) findViewById(genderGroup.getCheckedRadioButtonId());
        String gender = genderSelection.getText().toString();
        String workStatus = workStatusSelection.getText().toString();
        new AlertDialog.Builder(this).setTitle("Details information").setMessage("Details information:\n" + name + "\n" + email + "\n" + phone + "\n" + gender + "\n" + workStatus + "\n").setNeutralButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Lab 3 part 2", "onStart event called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Lab 3 part 2", "onRestart event called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lab 3 part 2", "onResume event called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lab 3 part 2", "onPause event called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Lab 3 part 2", "onStop event called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Lab 3 part 2", "onDestroy event called");
    }


}