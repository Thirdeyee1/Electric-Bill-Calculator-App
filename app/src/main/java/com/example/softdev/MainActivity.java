package com.example.softdev;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goManual = findViewById(R.id.buttonA);
        Button goAutomatic = findViewById(R.id.buttonB);

        goManual.setOnClickListener(v -> openManual());
        goAutomatic.setOnClickListener(v -> openAutomatic());

        layout = findViewById(R.id.tip_box);
        for (int i = 0; i < 4; i++) {
            addCard();
        }

    }


    @SuppressLint("DefaultLocale") private void addCard() {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.card_tips, null);
       // TextView tipView = view.findViewById(R.id.tip);
       // tipView.setText();
        layout.addView(view);
    }
    public void openManual() {
        Intent intent = new Intent(this, manual.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void openAutomatic() {
        Intent intent = new Intent(this, automatic.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
