package com.example.budgetapplication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView weeklySpendingTextView = findViewById(R.id.WeeklySpendingText);
        TextView weeklyAmountTextView = findViewById(R.id.WeeklyAmount);
        TextView foodTextView = findViewById(R.id.FoodText);
        TextView foodAmountView = findViewById(R.id.FoodAmount);
        TextView amenitiesTextView = findViewById(R.id.AmenitiesText);
        TextView amenitiesAmountView = findViewById(R.id.AmenitiesAmount);
        TextView transportationTextView = findViewById(R.id.TransportationText);
        TextView transportationAmountView = findViewById(R.id.TransportationAmount);
        TextView otherTextView = findViewById(R.id.OtherText);
        TextView otherAmount = findViewById(R.id.OtherAmount); // Initializing all TextViews for main menu
        String jsonData = readJsonData();
        if (!jsonData.isEmpty()) {
            try {
                double weeklyTotal = 0.0; // Initialize to 0.0 as a default
                double foodTotal = 0.0;
                double amenitiesTotal = 0.0;
                double transportationTotal = 0.0;
                double otherTotal = 0.0;

                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String category = jsonObject.getString("category");
                    double amount = jsonObject.getDouble("amount");

                    switch (category) {
                        case "Food":
                            foodTotal += amount;
                            break;
                        case "Amenities":
                            amenitiesTotal += amount;
                            break;
                        case "Transportation":
                            transportationTotal += amount;
                            break;
                        case "Other":
                            otherTotal += amount;
                            break;
                    }
                    weeklyTotal += amount;
                }
                double finalWeeklyTotal = weeklyTotal;
                double finalFoodTotal = foodTotal;
                double finalAmenitiesTotal = amenitiesTotal;
                double finalTransportationTotal = transportationTotal;
                double finalOtherTotal = otherTotal;
                weeklyAmountTextView.setText("$" + String.format("%.2f", finalWeeklyTotal));
                foodAmountView.setText("$" + String.format("%.2f", finalFoodTotal));
                amenitiesAmountView.setText("$" + String.format("%.2f", finalAmenitiesTotal));
                transportationAmountView.setText("$" + String.format("%.2f", finalTransportationTotal));
                otherAmount.setText("$" + String.format("%.2f", finalOtherTotal));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Button addButton = findViewById(R.id.btnAddPurchase);

        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddPurchaseActivity.class);
            startActivity(intent);
        });
    }
    private String readJsonData() {
        StringBuilder jsonBuilder = new StringBuilder();
        try {
            FileInputStream fis = openFileInput("appLogs.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonBuilder.toString();
    }
}