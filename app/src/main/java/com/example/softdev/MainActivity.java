package com.example.softdev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goManual = (Button) findViewById(R.id.buttonA);
        goManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManual();

            }
        });


    }
    public void openManual() {
        Intent intent = new Intent(this, manual.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
