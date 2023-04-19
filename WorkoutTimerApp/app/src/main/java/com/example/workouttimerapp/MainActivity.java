package com.example.workouttimerapp;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText workoutDurationEditText;
    private EditText restDurationEditText;
    private TextView workoutTimerTextView;
    private TextView restTimerTextView;
    private ProgressBar progressBar;
    private Button startButton;
    private Button stopButton;

    private CountDownTimer workoutTimer;
    private CountDownTimer restTimer;
    private long workoutDurationInMillis;
    private long restDurationInMillis;
    private boolean isWorkoutPhase;
    private boolean isTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workoutDurationEditText = findViewById(R.id.workout_duration);
        restDurationEditText = findViewById(R.id.rest_duration);
        workoutTimerTextView = findViewById(R.id.workout_timer);
        restTimerTextView = findViewById(R.id.rest_timer);
        progressBar = findViewById(R.id.progress_bar);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimer();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });
    }

    private void startTimer() {
        workoutDurationInMillis = Long.parseLong(workoutDurationEditText.getText().toString()) * 1000;
        restDurationInMillis = Long.parseLong(restDurationEditText.getText().toString()) * 1000;

        workoutTimer = new CountDownTimer(workoutDurationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerTextView(workoutTimerTextView, millisUntilFinished);
                updateProgressBar(millisUntilFinished, workoutDurationInMillis);
            }

            @Override
            public void onFinish() {
                playSound();
                isWorkoutPhase = false;
                restTimer.start();
            }
        };

        restTimer = new CountDownTimer(restDurationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerTextView(restTimerTextView, millisUntilFinished);
                updateProgressBar(millisUntilFinished, restDurationInMillis);
            }

            @Override
            public void onFinish() {
                playSound();
                isWorkoutPhase = true;
                workoutTimer.start();
            }
        };

        isWorkoutPhase = true;
        isTimerRunning = true;
        startButton.setText("Pause");
        workoutTimer.start();
    }

    private void stopTimer() {
        workoutTimer.cancel();
        restTimer.cancel();
        progressBar.setProgress(0);
        workoutTimerTextView.setText("00:00");
        restTimerTextView.setText("00:00");
        startButton.setText("Start");
        isTimerRunning = false;
    }

    private void updateTimerTextView(TextView textView, long millisUntilFinished) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                TimeUnit.MINUTES.toSeconds(minutes);
        textView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    private void updateProgressBar(long millisUntilFinished, long durationInMillis) {
        int progress = (int) ((durationInMillis - millisUntilFinished) * 100 / durationInMillis);
        progressBar.setProgress(progress);
    }

    private void playSound() {
        // Play a sound
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.start();

        // Vibrate the device
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Deprecated in API 26
                vibrator.vibrate(1000);
            }
        }
    }
}