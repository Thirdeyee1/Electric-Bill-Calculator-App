package com.example.softdev;

import static android.text.TextUtils.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

class A_Node {
    final double a_data;
    A_Node next;

    A_Node(double a_data) {
        this.a_data = a_data;
        this.next = null;
    }
}

class A_LinkedList {
    private A_Node a_head;

    A_LinkedList() {
        this.a_head = null;
    }

    // Method to add a new node with given data at the end of the list
    public void add(double a_data) {
        A_Node newNode = new A_Node(a_data);

        if (a_head == null) {
            a_head = newNode;
        } else {
            A_Node temp = a_head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    // Method to delete a node at a specified index
    public void deleteNodeAt(int index) {
        if (a_head == null) {
            return;
        }

        if (index == 0) {
            a_head = a_head.next;
            return;
        }

        A_Node temp = a_head;
        A_Node prev = null;
        int count = 0;

        while (temp != null) {
            if (count == index) {
                prev.next = temp.next;
                return;
            }
            prev = temp;
            temp = temp.next;
            count++;
        }

    }

    // Method to calculate the sum of all values in the list
    public double sumOfValues() {
        A_Node temp = a_head;
        double sum = 0.0;

        while (temp != null) {
            sum += temp.a_data;
            temp = temp.next;
        }

        return sum;
    }
}

public class automatic extends AppCompatActivity {
    final A_LinkedList myList = new A_LinkedList();

    Button add;
    AlertDialog dialog;
    LinearLayout layout;

    private int cardIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic);


        //For the new window
        Button summary = findViewById(R.id.summary);
        summary.setOnClickListener(v -> openActivity2());

        Button back = findViewById(R.id.btnGoBack1);
        back.setOnClickListener(v -> openMain());

        //For building dialog
        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        // Build the dialog
        buildDialog();


        add.setOnClickListener(v -> dialog.show());
    }
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openActivity2(){
        Intent intent = new Intent(this, summary.class);
        startActivity(intent);
    }

    @SuppressLint("DefaultLocale")
    public void writeTotal(Double t_d){
        TextView viewT_D = findViewById(R.id.total_daily);
        TextView viewT_M = findViewById(R.id.total_monthly);

        myList.add(t_d);
        double sum = myList.sumOfValues();

        viewT_D.setText("₱ "+String.format("%.2f",sum));
        viewT_M.setText("₱ "+String.format("%.2f",sum*31));
    }

    @SuppressLint("DefaultLocale")
    public void writeTotal(){
        TextView viewT_D = findViewById(R.id.total_daily);
        TextView viewT_M = findViewById(R.id.total_monthly);

        double sum = myList.sumOfValues();

        viewT_D.setText("₱ "+String.format("%.2f",sum));
        viewT_M.setText("₱ "+String.format("%.2f",sum*31));
    }
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

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
                Toast.makeText(automatic.this, "Please input details", Toast.LENGTH_SHORT).show();}
            else if (isEmpty(nameStr)) {
                Toast.makeText(automatic.this, "Please input Device name", Toast.LENGTH_SHORT).show();
            } else if (isEmpty(loadStr) || isEmpty(quantityStr) || isEmpty(hrsDailyStr)){
                Toast.makeText(automatic.this, "Please input valid values", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // convert input to numbers
                    double l = Double.parseDouble(loadStr);
                    double q = Double.parseDouble(quantityStr);
                    double h = Double.parseDouble(hrsDailyStr);

                    // Adding card if conversion is successful
                    addCard(nameStr, l, q, h);
                } catch (NumberFormatException e) {
                    // Show a toast if conversion to numbers fails
                    Toast.makeText(automatic.this, "Please input valid values", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });

        dialog = builder.create();
    }
    @SuppressLint("DefaultLocale")
    private void addCard(String name, double l, double q, double h) {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView loadView = view.findViewById(R.id.load);
        TextView quantityView = view.findViewById(R.id.quantity);
        TextView hrsDailyView = view.findViewById(R.id.hrsDaily);
        TextView viewD = view.findViewById(R.id.costDaily);
        TextView viewM = view.findViewById(R.id.costMonthly);
        Button delete = view.findViewById(R.id.delete);

        String str_load = String.valueOf(l);
        String str_quantity = String.valueOf(q);
        String str_hrs = String.valueOf(h);

        nameView.setText(name);
        loadView.setText(str_load);
        quantityView.setText(str_quantity);
        hrsDailyView.setText(str_hrs);

        int currentIndex = cardIndex;
        ++cardIndex;

        double computedDaily = 10 * l / 1000 * q * h;
        double computedMonthly = computedDaily * 31;

        writeTotal(computedDaily);

        viewD.setText(String.format("%.2f",computedDaily));
        viewM.setText(String.format("%.2f",computedMonthly));

        delete.setOnClickListener(v -> {
            layout.removeView(view);
            myList.deleteNodeAt(currentIndex); // Pass the index of the deleted card
            --cardIndex;
            writeTotal();
        });

        layout.addView(view);
    }


}