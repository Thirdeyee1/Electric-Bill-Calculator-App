package com.example.softdev;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    HorizontalScrollView scrollView;
    int currentCardIndex = 0;
    final int totalCards = 4;
    final int scrollDelay = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goManual = findViewById(R.id.buttonA);
        Button goAutomatic = findViewById(R.id.buttonB);

        goManual.setOnClickListener(v -> openManual());
        goAutomatic.setOnClickListener(v -> openAutomatic());

        layout = findViewById(R.id.tip_box);
        scrollView = findViewById(R.id.horizontal_scroll_view);

        // Add cards with different tips
        for (int i = 1; i <= totalCards; i++) {
            addCard(i);
        }

        // Start scrolling handler
        startCardScrolling();
    }

    private void startCardScrolling() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollNextCard();
                startCardScrolling();
            }
        }, scrollDelay);
    }

    private void scrollNextCard() {
        currentCardIndex = (currentCardIndex + 1) % totalCards;
        int scrollX = currentCardIndex * scrollView.getWidth();
        scrollView.smoothScrollTo(scrollX, 0);
    }

    @SuppressLint("DefaultLocale")
    private void addCard(int tipNumber) {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.card_tips, null);
        TextView tipView = view.findViewById(R.id.tip_1); // Use the static ID
        tipView.setText(getString(getResources().getIdentifier("tip_" + tipNumber, "string", getPackageName())));
        layout.addView(view);
    }

    public void openManual() {
        Intent intent = new Intent(this, ManualActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void openAutomatic() {
        Intent intent = new Intent(this, automatic.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
