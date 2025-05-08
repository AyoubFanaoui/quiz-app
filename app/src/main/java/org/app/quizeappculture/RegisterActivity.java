package org.app.quizeappculture;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.app.quizeappculture.entites.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, passwordConfirmEditText;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation de Firebase App
        FirebaseApp.initializeApp(this);  // Ajoutez ceci ici

        setContentView(R.layout.activity_register);

        // Initialisation des vues
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        passwordConfirmEditText = findViewById(R.id.passwordConfirm);
        registerButton = findViewById(R.id.register);

        // Initialisation de Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Gestion de l'enregistrement des utilisateurs
        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = passwordConfirmEditText.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Créer un utilisateur avec Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // L'utilisateur est créé avec succès
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                User newUser = new User(name, email);
                                db.collection("users")
                                        .document(user.getUid())
                                        .set(newUser)
                                        .addOnSuccessListener(aVoid ->{
                                            Toast.makeText(RegisterActivity.this, "User added to Firestore", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }).addOnFailureListener(e->{
                                            Toast.makeText(RegisterActivity.this, "Failed to add user to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }

                        } else {
                            // L'enregistrement a échoué
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
