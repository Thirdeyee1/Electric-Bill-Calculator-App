package com.example.softdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class summary extends AppCompatActivity {
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        back = findViewById(R.id.btnGoBack);
        back.setOnClickListener(v -> openManual());

        //Shows the summary from manual
        showSummary();




    }
    public void openManual() {
        Intent intent = new Intent(this, ManualActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

        //Shows the summary from Manual
    public void showSummary(){
        // Inside the onCreate method, retrieve the values from the intent
        double totalDaily = getIntent().getDoubleExtra("totalDaily", 0.0);
        double totalMonthly = totalDaily * 31;

// Now you can use these values to update your UI, e.g., set them to TextViews
        TextView viewT_D = findViewById(R.id.total_daily_summary);
        TextView viewT_M = findViewById(R.id.total_monthly_summary);

        viewT_D.setText(String.format("₱ %.2f", totalDaily));
        viewT_M.setText(String.format("₱ %.2f", totalMonthly));

    }
}