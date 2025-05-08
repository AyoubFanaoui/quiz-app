package org.app.quizeappculture;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.app.quizeappculture.database.AppDatabase;
import org.app.quizeappculture.entites.Question;
import org.app.quizeappculture.entites.ScoreRecord;

import java.util.Date;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private AppDatabase db;
    private List<Question> questionList;
    private int currentIndex = 0;
    private int score = 0;
    private CountDownTimer quizCountDownTimer;
    private long totalTimeInMillis = 30*1000;

    private long startTimeInMillis;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;


    private TextView questionTextView, quiz_number, timerTextView;
    private ImageView questionImage;
    private RadioGroup radioGroup;
    private RadioButton a1, a2, a3;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz); // Assure-toi que le fichier XML s'appelle bien activity_quiz.xml

        // Initialisation des vues

        questionTextView = findViewById(R.id.question_text);
        quiz_number = findViewById(R.id.quiz_num);
        questionImage = findViewById(R.id.question_image);
        radioGroup = findViewById(R.id.ga);
        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        nextBtn = findViewById(R.id.nextBtn);

        startTimeInMillis = System.currentTimeMillis();  // Début du quiz
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        timerTextView = findViewById(R.id.timerTextView);
        startTimer();
        db = AppDatabase.getDatabase(this);

        // Charger les questions dans un thread
        new Thread(() -> {
            questionList = db.questionDao().getAllQuestions();

            if (questionList == null || questionList.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "Aucune question trouvée", Toast.LENGTH_LONG).show());
            } else {
                runOnUiThread(this::loadQuestion);
            }
        }).start();

        nextBtn.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Veuillez choisir une réponse", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadio = findViewById(selectedId);
            Question currentQ = questionList.get(currentIndex);

            if (selectedRadio.getText().toString().equalsIgnoreCase(currentQ.getCorrectAnswer())) {
                score++;
                Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT).show();
            }

            currentIndex++;

            if (currentIndex < questionList.size()) {
                loadQuestion();
            } else {
                Toast.makeText(this, "Quiz terminé ! Score : " + score + "/" + questionList.size(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(QuizActivity.this, Score.class);
                intent.putExtra("score", score);
                startActivity(intent);

                //finish();
            }
        });
    }

    private void loadQuestion() {
        String quizNumber = (currentIndex+1)+"/5";

        Question q = questionList.get(currentIndex);

        questionTextView.setText(q.getQuestionText());
        a1.setText(q.getAnswer1());
        a2.setText(q.getAnswer2());
        a3.setText(q.getAnswer3());

        quiz_number.setText(quizNumber);

        radioGroup.clearCheck();

        // Charger l'image
        int imageResId = getResources().getIdentifier(q.getImageName(), "drawable", getPackageName());
        if (imageResId != 0) {
            questionImage.setImageResource(imageResId);
        } else {
            questionImage.setImageResource(R.drawable.img); // image par défaut si introuvable
        }
    }


    private void startTimer() {
        quizCountDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                timerTextView.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00");
                // Terminer automatiquement le quiz
                endQuiz(); // appelle une fonction pour afficher les résultats ou finir l'activité
            }
        }.start();
    }
    private void endQuiz() {
        long endTime = System.currentTimeMillis();
        long durationInSeconds = (endTime - startTimeInMillis) / 1000;

        FirebaseUser user = firebaseAuth.getCurrentUser();


        if (user != null) {
            String userId = user.getUid();
            ScoreRecord record = new ScoreRecord(
                    userId,
                    score,
                    durationInSeconds,
                    new Date()
            );

            firestore.collection("Score_record")
                    .add(record)
                    .addOnSuccessListener(documentReference -> {
                      Toast.makeText(this, "Time's up! and record saved", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(QuizActivity.this, Score.class);
                        in.putExtra("score", score);
                        in.putExtra("duration", durationInSeconds);
                        startActivity(in);
                    }).addOnFailureListener(e->{
                        Toast.makeText(this, "Time's up! but Failed to save attempt: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Continue quand même
                        Intent in = new Intent(QuizActivity.this, Score.class);
                        in.putExtra("score", score);
                        in.putExtra("duration", durationInSeconds);
                        startActivity(in);
                    });

        }



    }
}
