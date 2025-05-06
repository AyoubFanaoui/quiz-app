package org.app.quizeappculture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etpassword;
    Button login;
    TextView register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                                Intent in = new Intent(MainActivity.this, Quiz1.class);
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
}