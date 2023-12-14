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

class Auto_Appliance implements Serializable {
    final String name;
    final double load;
    final int quantity;
    final int hrsDaily;


    Auto_Appliance(String name, double load, int quantity, int hrsDaily) {
        this.name = name;
        this.load = Math.round(load * 100.0) / 100.0;
        this.quantity = quantity;
        this.hrsDaily = hrsDaily;
    }

    double getComputedDaily() {
        return 10 * load / 1000 * quantity * hrsDaily;
    }

    double getComputedMonthly() {
        return getComputedDaily() * 29.531;
    }
}

class Auto_TotalManager {
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

public class automatic extends AppCompatActivity {
    private final LinkedList<Auto_Appliance> auto_appliancesList = new LinkedList<>();
    private final Auto_TotalManager totalManager = new Auto_TotalManager();
    private AlertDialog dialog;
    private LinearLayout layout;

    private View exampleSlotView;
    private int cardIndex = 0;
    boolean initialSlotReplaced = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic);

        Button summary = findViewById(R.id.summary);
        summary.setOnClickListener(v -> openActivity2());

        Button back = findViewById(R.id.btnGoBack1);
        back.setOnClickListener(v -> openMain());

        // For building dialog
        Button add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        // Build the dialog
        buildDialog();

        add.setOnClickListener(v -> {
            initialSlotReplaced = true;
            dialog.show();
        });
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, Auto_summary.class);
        intent.putExtra("totalDaily", totalManager.getTotalDaily());
        intent.putExtra("auto_appliancesList", new ArrayList<>(auto_appliancesList)); // Assuming Auto_Appliance implements Serializable
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
                Toast.makeText(automatic.this, "Please input details", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(hrsDailyStr)>24) {
                Toast.makeText(automatic.this, "Daily use can't be more than 24 Hours!", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(hrsDailyStr)==0 || Integer.parseInt(loadStr)==0 || Integer.parseInt(quantityStr)==0) {
                Toast.makeText(automatic.this, "'0' is not a valid value! ", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double l = Double.parseDouble(loadStr);
                    int q = Integer.parseInt(quantityStr);
                    int h = Integer.parseInt(hrsDailyStr);

                    if (powerSwitch.isChecked()) l *= 745.7;


                    Auto_Appliance auto_appliance = new Auto_Appliance(nameStr, l, q, h);
                    addCard(auto_appliance);
                } catch (NumberFormatException e) {
                    Toast.makeText(automatic.this, "Please input valid values", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });

        dialog = builder.create();
    }

    @SuppressLint("DefaultLocale")
    private void addCard(Auto_Appliance auto_appliance) {
        removeExampleSlot();

        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView loadView = view.findViewById(R.id.load);
        TextView quantityView = view.findViewById(R.id.quantity);
        TextView hrsDailyView = view.findViewById(R.id.hrsDaily);
        TextView viewD = view.findViewById(R.id.costDaily);
        TextView viewM = view.findViewById(R.id.costMonthly);
        Button delete = view.findViewById(R.id.delete);

        nameView.setText(auto_appliance.name);
        loadView.setText(String.valueOf(auto_appliance.load));
        quantityView.setText(String.valueOf(auto_appliance.quantity));
        hrsDailyView.setText(String.valueOf(auto_appliance.hrsDaily));

        final int index = cardIndex;
        ++cardIndex;
// Set the index as a tag
        view.setTag(index);

        double computedDaily = auto_appliance.getComputedDaily();
        double computedMonthly = auto_appliance.getComputedMonthly();

        totalManager.addCost(computedDaily);

        viewD.setText(String.format("%.2f", computedDaily));
        viewM.setText(String.format("%.2f", computedMonthly));

        delete.setOnClickListener(v -> {
            layout.removeView(view);
            int removedIndex = (int) view.getTag(); // Get the index from the tag

            // Ensure index is within valid bounds
            if (removedIndex >= 0 && removedIndex < auto_appliancesList.size()) {
                auto_appliancesList.remove(removedIndex);
                totalManager.subtractCost(computedDaily);

                // Update tags for the remaining views
                for (int i = removedIndex; i < layout.getChildCount(); i++) {
                    View childView = layout.getChildAt(i);
                    int currentIndex = (int) childView.getTag();
                    childView.setTag(currentIndex - 1);
                }
                if (auto_appliancesList.isEmpty()) {
                    // Reset count to 0 if all indices are deleted
                    cardIndex = 0;
                }
                writeTotal();
            } else {
                // Handle invalid index
                Toast.makeText(automatic.this, "Invalid index", Toast.LENGTH_SHORT).show();
                // You can also log the issue for debugging purposes
                Log.e("Automatic", "Invalid index: " + removedIndex);
            }
        });
        auto_appliancesList.add(auto_appliance);
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
            viewT_M.setText(String.format("₱ %.2f", totalDaily * 29.531));
        });
    }

    private void removeExampleSlot() {
        if (exampleSlotView != null) {
            layout.removeView(exampleSlotView);
            exampleSlotView = null;
        }
    }
}