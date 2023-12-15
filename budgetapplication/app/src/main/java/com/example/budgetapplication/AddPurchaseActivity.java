package com.example.budgetapplication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AddPurchaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);

        EditText amountEditText = findViewById(R.id.entAmount);
        Spinner categorySpinner = findViewById(R.id.spinnerCategory); // Initiating spinner (calling array/Strings.xml)
        Button addButton = findViewById(R.id.btnAdd); // Button confirms adding to Json file
        Button backButton = findViewById(R.id.btnBack); // Back button incase user wants to go back to MainActivity

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_array, // Refer to Strings.xml to see category_array
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter); // Layout of spinner list (use of array/Strings.xml)

        addButton.setOnClickListener(view -> {
            String amountText = amountEditText.getText().toString(); // Convert amount text to String
            Double amount = null;
            try {
                amount = Double.parseDouble(amountText); // Set amount to amountText but parsed as a Double
            } catch (NumberFormatException e) {
                logEvent("input_error", -99.00); // Catch error for input
            }

            String category = categorySpinner.getSelectedItem().toString();
            logEvent(category, amount); // Log event with category & amount
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent); // After logging category and amount, go back to MainActivity
        });

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent); // Return to main menu if user clicks backButton
        });
    }
    private void logEvent(String category, Double amount) {
        String existingContent = readJsonData(); // Read JSON file
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(existingContent); // Check if JSON file already has content/info so we don't overwrite instead of adding content
        } catch (JSONException e) {
            jsonArray = new JSONArray(); // If the JSON file is empty then we just fill the JSON file
        }

        JSONObject logObject = new JSONObject(); // Create JSONObject for logEvent
        try {
            logObject.put("amount", amount);
            logObject.put("category", category);

            jsonArray.put(logObject); // Add now object to the array

            String jsonString = jsonArray.toString(); // Convert object to String

            FileOutputStream fos = openFileOutput("appLogs.json", MODE_PRIVATE); // Add data to appLogs.json
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(jsonString);
            osw.close(); // Writing ends here
        } catch (JSONException | IOException e) {
            e.printStackTrace(); // Error handling
        }
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
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonBuilder.toString();
    }
}