package org.app.quizeappculture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Score extends AppCompatActivity {

    Button tryAgain, logout;
    TextView scoreView;
    TextView leaderboard ;
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
        leaderboard = findViewById(R.id.textLeaderboard);

        progress = findViewById(R.id.progressBar);
        Intent prevIntent = getIntent();
        score = prevIntent.getIntExtra("score", 0);
        scoreView.setText((score*100)/5+"%");
        progress.setProgress(score*100/5);

        topFiveScore();

        tryAgain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Score.this, QuizActivity.class);
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

    private void topFiveScore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get top five scores
        db.collection("Score_record")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> topPlayers = new ArrayList<>();
                    int[] count = {0};  // Counter to track the number of users fetched

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String userId = doc.getString("userId");
                        long score = doc.getLong("score");
                        long time = doc.getLong("durationInSeconds");

                        // Use userId to get the user's name
                        db.collection("users")
                                .document(userId)
                                .get()
                                .addOnSuccessListener(userDoc -> {
                                    String userName = userDoc.getString("name");
                                    topPlayers.add(userName + " - Score: " + score + " - Time: " + time + "s");

                                    // After the last user is fetched, show the leaderboard
                                    count[0]++;
                                    if (count[0] == queryDocumentSnapshots.size()) {
                                        // Update the leaderboard display

                                        leaderboard.setText(TextUtils.join("\n", topPlayers));
                                    }
                                })
                                .addOnFailureListener(e -> Log.e("ScoreActivity", "Error fetching user details", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("ScoreActivity", "Error getting top scores", e));
    }


}
