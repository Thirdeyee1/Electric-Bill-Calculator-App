package com.example.softdev;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
        displayComparisonText();


    }
    public void openManual() {
        Intent intent = new Intent(this, ManualActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

        //Shows the summary from Manual
    public void showSummary(){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        List<Appliance> appliancesList = (List<Appliance>) getIntent().getSerializableExtra("appliancesList");
        // Inside the onCreate method, retrieve the values from the intent
        double totalDaily = getIntent().getDoubleExtra("totalDaily", 0.0);
        double totalMonthly = totalDaily * 31;

        // Now you can use these values to update your UI, e.g., set them to TextViews
        TextView viewT_D = findViewById(R.id.total_daily_summary);
        TextView viewT_M = findViewById(R.id.total_monthly_summary);

        viewT_D.setText(" ₱ " + decimalFormat.format(totalDaily));
        viewT_M.setText(" ₱ " + decimalFormat.format(totalMonthly));

        // Initialize and populate the bar chart
        displayBarChart(appliancesList);

    }
    // Display BarChart
    private void displayBarChart(List<Appliance> appliancesList) {
        BarChart barChart = findViewById(R.id.barChart);

        // Create a list of BarEntry objects for each appliance
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>(); // List to store appliance names
        for (int i = 0; i < appliancesList.size(); i++) {
            Appliance appliance = appliancesList.get(i);
            entries.add(new BarEntry(i, (float) appliance.getComputedDaily()));

            // Add appliance name to the labels list
            labels.add(appliance.name);
        }

        // Create a BarDataSet with the entries
        BarDataSet dataSet = new BarDataSet(entries, "Daily Usage");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // Create a BarData object with the BarDataSet
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);

        // Customize the X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Set custom labels for the X-axis
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        // Customize the Y-axis (left)
        YAxis leftAxis = barChart.getAxisLeft();
        // Use a custom ValueFormatter to display peso sign and numbers
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return "₱" + value; // Add peso sign to the label
            }
        });

        leftAxis.setDrawGridLines(false);
        leftAxis.setTextSize(12f); // Adjust text size if needed
        leftAxis.setAxisMinimum(0f); // Set the minimum value for the Y-axis

        // Set the number of labels on the Y-axis
        leftAxis.setLabelCount(labels.size(), false); // Set to true if you want to force a specific number of labels

        // Set the BarData to the chart
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate(); // Refresh the chart
    }

    private void displayComparisonText() {
        LinearLayout summaryLayout = findViewById(R.id.summaryLayout);

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        //TextView for displaying the comparison text
        TextView comparisonText = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0); // Adjust margins as needed
        comparisonText.setLayoutParams(params);
        comparisonText.setTextColor(Color.WHITE); // Adjust text color
        comparisonText.setTextSize(20f); // Adjust text size

        // Get the user's daily consumption from the intent
        double userDailyConsumption = getIntent().getDoubleExtra("totalDaily", 0.0);

        // Define the average Filipino household electricity consumption rate
        double averageHouseholdRate = 9.7545; // PHP per kWh

        // Calculate the comparison
        double percentageDifference = ((averageHouseholdRate - userDailyConsumption) / averageHouseholdRate) * 100;

        // Build the comparison text
        String comparison = "Your daily consumption is ";
        if (percentageDifference > 0) {
            comparison += decimalFormat.format(percentageDifference) + "% lower";
        } else if (percentageDifference < 0) {
            comparison += decimalFormat.format(-percentageDifference) + "% higher";
        } else {
            comparison += "equal to";
        }
        comparison += " than the average Filipino household.";

        // Set the comparison text to the TextView
        comparisonText.setText(comparison);

        // Add the TextView to the layout
        summaryLayout.addView(comparisonText);
    }

}
