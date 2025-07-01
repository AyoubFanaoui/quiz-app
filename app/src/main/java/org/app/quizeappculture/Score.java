package org.app.quizeappculture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Score extends AppCompatActivity {

    Button tryAgain, logout;
    TextView scoreView;
    ProgressBar progress;
    int score;




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        tryAgain = findViewById(R.id.try_again);
        logout = findViewById(R.id.logout);
        scoreView = findViewById(R.id.scoreView);

        progress = findViewById(R.id.progressBar);
        Intent prevIntent = getIntent();
        score = prevIntent.getIntExtra("score", 0);
        scoreView.setText((score*100)/5+"%");
        progress.setProgress(score*100/5);

        tryAgain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Score.this, Quiz1.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Score.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }
}