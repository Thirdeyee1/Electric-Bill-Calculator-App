package com.example.softdev;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.List;

public class Auto_summary extends AppCompatActivity {
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        back = findViewById(R.id.btnGoBack);
        back.setOnClickListener(v -> openAutomatic());

        //Shows the summary from manual
        showSummary();


    }
    public void openAutomatic() {
        Intent intent = new Intent(this, automatic.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //Shows the summary from Manual
    public void showSummary(){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        List<Auto_Appliance> auto_appliancesList = (List<Auto_Appliance>) getIntent().getSerializableExtra("auto_appliancesList");
        // Inside the onCreate method, retrieve the values from the intent
        double totalDaily = getIntent().getDoubleExtra("totalDaily", 0.0);
        double totalMonthly = totalDaily * 29.531;

// Now you can use these values to update your UI, e.g., set them to TextViews
        TextView viewT_D = findViewById(R.id.total_daily_summary);
        TextView viewT_M = findViewById(R.id.total_monthly_summary);

        viewT_D.setText(" ₱ " + decimalFormat.format(totalDaily));
        viewT_M.setText(" ₱ " + decimalFormat.format(totalMonthly));

        // Initialize and populate the bar chart
        displayBarChart(auto_appliancesList);

    }
    // Display BarChart
    private void displayBarChart(List<Auto_Appliance> auto_appliancesList) {
        BarChart barChart = findViewById(R.id.barChart);

        // Create a list of BarEntry objects for each auto_appliance
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>(); // List to store auto_appliance names
        for (int i = 0; i < auto_appliancesList.size(); i++) {
            Auto_Appliance auto_appliance = auto_appliancesList.get(i);
            entries.add(new BarEntry(i, (float) auto_appliance.getComputedDaily()));

            // Add auto_appliance name to the labels list
            labels.add(auto_appliance.name);
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

        // Set the BarData to the chart
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate(); // Refresh the chart
    }
}
