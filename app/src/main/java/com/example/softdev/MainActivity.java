package com.example.softdev;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goManual = findViewById(R.id.buttonA);
        Button goAutomatic = findViewById(R.id.buttonB);

        goManual.setOnClickListener(v -> openManual());
        goAutomatic.setOnClickListener(v -> openAutomatic());


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
