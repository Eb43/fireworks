package com.example.fireworks;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fireworksContainer;
    private Button launchButton;
    private Handler handler = new Handler();
    private Runnable fireworksRunnable;
    private final int duration = 20000; // 20 seconds
    private final int frequency = 200; // every 200ms => 5 times per second
    private Random random = new Random();
    private int fireworksCount = 0; // To keep track of total fireworks displayed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireworksContainer = findViewById(R.id.fireworks_container);
        launchButton = findViewById(R.id.launch_button);

        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFireworks();
            }
        });
    }

    private void startFireworks() {
        launchButton.setEnabled(false);
        fireworksCount = 0;

        fireworksRunnable = new Runnable() {
            @Override
            public void run() {
                showFirework();
                fireworksCount++;

                if (fireworksCount < (duration / frequency)) {
                    handler.postDelayed(this, frequency);
                }
            }
        };

        handler.post(fireworksRunnable);

        // Ensure fireworks stop after the specified duration
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopFireworks();
            }
        }, duration);
    }

    private void showFirework() {
        int containerWidth = fireworksContainer.getWidth();
        int containerHeight = fireworksContainer.getHeight();

        if (containerWidth == 0 || containerHeight == 0) {
            // If dimensions are not yet available, try again shortly
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showFirework();
                }
            }, 50);
            return;
        }

        TextView firework = new TextView(this);
        firework.setText(" * ");
        firework.setTextSize(TypedValue.COMPLEX_UNIT_SP, 64);
        firework.setTextColor(getRandomColor());
        firework.setGravity(Gravity.CENTER);

        // Ensure the firework is fully within the container
        int x = random.nextInt(containerWidth - 50); // Adjust 50 based on expected size of the firework
        int y = random.nextInt(containerHeight - 50);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = x;
        params.topMargin = y;
        firework.setLayoutParams(params);

        fireworksContainer.addView(firework);
    }

    private void stopFireworks() {
        handler.removeCallbacks(fireworksRunnable);
        fireworksContainer.removeAllViews();
        launchButton.setEnabled(true);
    }

    private int getRandomColor() {
        // Generate random bright colors for fireworks
        return 0xff000000 | random.nextInt(0x00ffffff);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
