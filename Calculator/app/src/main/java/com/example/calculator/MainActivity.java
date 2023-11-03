package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textViewDisplay;
    String displayData = "";
    String operator = "";
    double value1 = 0.0, value2 = 0.0;
    boolean justCalculated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewDisplay = findViewById(R.id.textViewDisplay);
    }

    public void onButtonClick(View view) {
        Button button = (Button) view;
        String data = button.getText().toString();

        if(justCalculated) {
            switch(data){
                case "/":
                case "*":
                case "-":
                case "+":
                    justCalculated = false;
                default:
                    if((data.matches("\\d+"))) {
                        displayData = "";
                        justCalculated = false;
                    }
            }
        }

        switch(data) {
            case "C":
                displayData = "";
                operator = "";
                value1 = 0.0;
                value2 = 0.0;
                textViewDisplay.setText("0");
                break;
            case "=":
                performOperation();
                operator = "";
                displayData = String.valueOf(value1);
                textViewDisplay.setText(displayData);
                justCalculated = true;
                break;
            case "/":
            case "*":
            case "-":
            case "+":
                operator = data;
                value1 = Double.valueOf(displayData);
                displayData = "";
                break;
            default:
                displayData += data;
                textViewDisplay.setText(displayData);
                break;
        }
    }

    private void performOperation() {
        value2 = Double.valueOf(displayData);

        switch(operator) {
            case "/":
                value1 /= value2;
                break;
            case "*":
                value1 *= value2;
                break;
            case "-":
                value1 -= value2;
                break;
            case "+":
                value1 += value2;
                break;
        }
    }
}
