package com.example.softdev;

import static android.text.TextUtils.isEmpty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Appliance implements Serializable {
    final String name;
    final double load;
    final int quantity;
    final int hrsDaily;


    Appliance(String name, double load, int quantity, int hrsDaily) {
        this.name = name;
        this.load = load;
        this.quantity = quantity;
        this.hrsDaily = hrsDaily;
    }

    double getComputedDaily() {
        return 10 * load / 1000 * quantity * hrsDaily;
    }

    double getComputedMonthly() {
        return getComputedDaily() * 31;
    }
}

class TotalManager {
    private double totalDaily = 0.0;

    void addCost(double cost) {
        totalDaily += cost;
    }

    void subtractCost(double cost) {
        totalDaily -= cost;
    }

    double getTotalDaily() {
        return totalDaily;
    }
}

public class ManualActivity extends AppCompatActivity {
    private final LinkedList<Appliance> appliancesList = new LinkedList<>();
    private final TotalManager totalManager = new TotalManager();
    private AlertDialog dialog;
    private LinearLayout layout;

    private View exampleSlotView;
    private int cardIndex = 0;
    boolean initialSlotReplaced = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        Button summary = findViewById(R.id.summary);
        summary.setOnClickListener(v -> openActivity2());

        Button back = findViewById(R.id.btnGoBack1);
        back.setOnClickListener(v -> openMain());

        // For building dialog
        Button add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        // Build the dialog
        buildDialog();

        // Check if the initial slot has been replaced
        if (!initialSlotReplaced) {
            addExampleSlot();
        }

        add.setOnClickListener(v -> {
            initialSlotReplaced = true;
            dialog.show();
        });
    }

    public List<Appliance> getAppliancesList() {
        return appliancesList;
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, summary.class);
        intent.putExtra("totalDaily", totalManager.getTotalDaily());
        intent.putExtra("appliancesList", new ArrayList<>(appliancesList)); // Assuming Appliance implements Serializable
        startActivity(intent);
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);
        Switch powerSwitch = view.findViewById(R.id.power_type);

        final EditText name = view.findViewById(R.id.nameEdit);
        final EditText load = view.findViewById(R.id.loadEdit);
        final EditText quantity = view.findViewById(R.id.quantityEdit);
        final EditText hrsDaily = view.findViewById(R.id.hrsDailyEdit);

        builder.setView(view);
        builder.setTitle("Enter Details");
        builder.setPositiveButton("OK", (dialog, which) -> {
            String nameStr = name.getText().toString();
            String loadStr = load.getText().toString();
            String quantityStr = quantity.getText().toString();
            String hrsDailyStr = hrsDaily.getText().toString();

            if (isEmpty(nameStr) || isEmpty(loadStr) || isEmpty(quantityStr) || isEmpty(hrsDailyStr)) {
                Toast.makeText(ManualActivity.this, "Please input details", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double l = Double.parseDouble(loadStr);
                    int q = Integer.parseInt(quantityStr);
                    int h = Integer.parseInt(hrsDailyStr);

                    if (powerSwitch.isChecked()) l *= 745.7;

                    Appliance appliance = new Appliance(nameStr, l, q, h);
                    addCard(appliance);
                } catch (NumberFormatException e) {
                    Toast.makeText(ManualActivity.this, "Please input valid values", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });

        dialog = builder.create();
    }

    @SuppressLint("DefaultLocale")
    private void addCard(Appliance appliance) {
        removeExampleSlot();

        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView loadView = view.findViewById(R.id.load);
        TextView quantityView = view.findViewById(R.id.quantity);
        TextView hrsDailyView = view.findViewById(R.id.hrsDaily);
        TextView viewD = view.findViewById(R.id.costDaily);
        TextView viewM = view.findViewById(R.id.costMonthly);
        Button delete = view.findViewById(R.id.delete);

        nameView.setText(appliance.name);
        loadView.setText(String.valueOf(appliance.load));
        quantityView.setText(String.valueOf(appliance.quantity));
        hrsDailyView.setText(String.valueOf(appliance.hrsDaily));

        final int index = cardIndex;
        ++cardIndex;
// Set the index as a tag
        view.setTag(index);

        double computedDaily = appliance.getComputedDaily();
        double computedMonthly = appliance.getComputedMonthly();

        totalManager.addCost(computedDaily);

        viewD.setText(String.format("%.2f", computedDaily));
        viewM.setText(String.format("%.2f", computedMonthly));

        delete.setOnClickListener(v -> {
            layout.removeView(view);
            int removedIndex = (int) view.getTag(); // Get the index from the tag

            // Ensure index is within valid bounds
            if (removedIndex >= 0 && removedIndex < appliancesList.size()) {
                appliancesList.remove(removedIndex);
                totalManager.subtractCost(computedDaily);

                // Update tags for the remaining views
                for (int i = removedIndex; i < layout.getChildCount(); i++) {
                    View childView = layout.getChildAt(i);
                    int currentIndex = (int) childView.getTag();
                    childView.setTag(currentIndex - 1);
                }
                if (appliancesList.isEmpty()) {
                    // Reset count to 0 if all indices are deleted
                    cardIndex = 0;}
                writeTotal();
            } else {
                // Handle invalid index
                Toast.makeText(ManualActivity.this, "Invalid index", Toast.LENGTH_SHORT).show();
                // You can also log the issue for debugging purposes
                Log.e("ManualActivity", "Invalid index: " + removedIndex);
            }
        });
        appliancesList.add(appliance);
        layout.addView(view);
        writeTotal();


    }


        @SuppressLint("DefaultLocale")
    public void writeTotal() {
        runOnUiThread(() -> {
            TextView viewT_D = findViewById(R.id.total_daily);
            TextView viewT_M = findViewById(R.id.total_monthly);

            double totalDaily = totalManager.getTotalDaily();
            System.out.println("Updated total daily: " + totalDaily);

            viewT_D.setText(String.format("₱ %.2f", totalDaily));
            viewT_M.setText(String.format("₱ %.2f", totalDaily * 31));
        });
    }

    private void removeExampleSlot() {
        if (exampleSlotView != null) {
            layout.removeView(exampleSlotView);
            exampleSlotView = null;
        }
    }

    @SuppressLint("DefaultLocale")
    private View createAndAddSlot(String name, String load, String quantity, String hrsDaily) {
        @SuppressLint("InflateParams") View exampleSlotView = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = exampleSlotView.findViewById(R.id.name);
        TextView loadView = exampleSlotView.findViewById(R.id.load);
        TextView quantityView = exampleSlotView.findViewById(R.id.quantity);
        TextView hrsDailyView = exampleSlotView.findViewById(R.id.hrsDaily);
        TextView viewD = exampleSlotView.findViewById(R.id.costDaily);
        TextView viewM = exampleSlotView.findViewById(R.id.costMonthly);

        nameView.setText(name);
        loadView.setText(load);
        quantityView.setText(quantity);
        hrsDailyView.setText(hrsDaily);

        double l = Double.parseDouble(load);
        double q = Double.parseDouble(quantity);
        double h = Double.parseDouble(hrsDaily);
        double computedDaily = 11.85 * l / 1000 * q * h;
        double computedMonthly = computedDaily * 31;

        viewD.setText(String.format("%.2f", computedDaily));
        viewM.setText(String.format("%.2f", computedMonthly));

        layout.addView(exampleSlotView);
        return exampleSlotView;
    }

    private void addExampleSlot() {
        String exampleName = "Example Device";
        String exampleLoad = "100";
        String exampleQuantity = "2";
        String exampleHrsDaily = "4";

        exampleSlotView = createAndAddSlot(exampleName, exampleLoad, exampleQuantity, exampleHrsDaily);
    }
}
