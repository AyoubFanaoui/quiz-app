package org.app.quizeappculture;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.app.quizeappculture.database.AppDatabase;
import org.app.quizeappculture.entites.Question;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;

    EditText etEmail, etpassword;
    Button login;
    TextView register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //deleteDatabase("quiz_database");

        db = AppDatabase.getDatabase(this);
        insertAndPrintTestQuestions();

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // Récupérer les vues
        etEmail = findViewById(R.id.email);
        etpassword = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        // Gérer le clic sur le bouton login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  etEmail.getText().toString().trim();
                String password = etpassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Connexion avec Firebase
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                // Rediriger vers l'activité principale
                                //Intent in = new Intent(MainActivity.this, Quiz1.class);
                                Intent in = new Intent(MainActivity.this, QuizActivity.class);
                                startActivity(in);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }



        });



    }


    private void insertAndPrintTestQuestions(){
        new Thread(()->{
            // Créer une instance de la DAO
            List<Question> existing = db.questionDao().getAllQuestions();

            // On évite de réinsérer à chaque fois (utile pour les tests)
            if (existing.isEmpty()) {
                // Insertion de quelques questions
                db.questionDao().insert(new Question(
                        "Quelle est la capitale du Maroc ?", "Rabat", "Casablanca", "Marrakech", "Rabat","rabat"));
                db.questionDao().insert(new Question(
                        "Quelle est la capitale du Canada ?", "Toronto", "Ottawa", "Vancouver","Ottawa" ,"canadamap"));
                db.questionDao().insert(new Question(
                        "Quel est le plus grand océan du monde ?", "Océan Atlantique", "Océan Indien", "Océan Pacifique","Océan Pacifique" ,"ocean"));
                db.questionDao().insert(new Question(
                        "Lequel des éléments suivants est reconnu comme un symbole culturel fort de la Palestine ?", "Le kimono", "Le keffieh", "Le sombrero","Le keffieh" ,"cultureplastine"));
                db.questionDao().insert(new Question(
                        "Quels sont les deux principaux territoires revendiqués par la Palestine comme faisant partie de son futur État ?", "Le Golan et le Néguev", "La Cisjordanie et la bande de Gaza", " Le Sinaï et la Galilée", "La Cisjordanie et la bande de Gaza", "mapofpalastin"));
            }

            // Récupérer toutes les questions
            List<Question> questions = db.questionDao().getAllQuestions();


            // Afficher dans la console (Logcat)
            for (Question q : questions) {
                Log.d("TEST_DB", "ID: " + q.getId() +
                        ", Question: " + q.getQuestionText() +
                        ", Réponse1: " + q.getAnswer1() +
                        ", Réponse2: " + q.getAnswer2() +
                        ", Réponse3: " + q.getAnswer3() +
                        ", Image: " + q.getImageName());
            }

        }).start();

    }
}