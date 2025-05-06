package org.app.quizeappculture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Quiz1 extends AppCompatActivity {

    RadioGroup ga;
    RadioButton selected;
    Button next;
    int score;

    String correcteResponse = "Paris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        ga = findViewById(R.id.ga);
        next = findViewById(R.id.nextBtn);
        score = 0;
         next.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View v) {
                 if(ga.getCheckedRadioButtonId() == -1){
                     Toast.makeText(getApplicationContext(), "Select a response", Toast.LENGTH_LONG).show();

                 }else{
                     selected = findViewById(ga.getCheckedRadioButtonId());
                     if(selected.getText().toString().equals(correcteResponse)){
                         score+=1;
                     }
                     Intent intent = new Intent(Quiz1.this, Quiz2.class);
                     intent.putExtra("score", score);
                     startActivity(intent);


                 }
             }
         });

    }
}