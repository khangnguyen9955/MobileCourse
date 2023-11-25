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
    double value1 = 0.0;
    boolean isNegative = false;
    boolean justCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewDisplay = findViewById(R.id.textViewDisplay);
    }

    public void onButtonClick(View view) {
        Button button = (Button) view;
        String data = button.getText().toString();
        switch (data) {
            case "C":
                displayData = "";
                operator = "";
                value1 = 0.0;
                textViewDisplay.setText("0");
                isNegative = false;
                justCalculated = false;
                break;
            case "=":
                if (!displayData.isEmpty()) performOperation();
                operator = "";
                isNegative = displayData.startsWith("-");
                textViewDisplay.setText(displayData);
                justCalculated = true;
                break;
            case "/":
            case "*":
            case "-":
            case "+":
                if (!displayData.isEmpty()) performOperation();
                operator = data;
                displayData = "";
                break;
            default:
                if (justCalculated) {
                    displayData = data;
                    justCalculated = false;
                } else {
                    displayData += data;
                }
                textViewDisplay.setText(displayData);
                break;
        }
    }

    private void performOperation() {
        double value2 = Double.valueOf(displayData);
        switch (operator) {
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
            default:
                value1 = value2;
        }
        displayData = String.valueOf(value1);
    }
}
