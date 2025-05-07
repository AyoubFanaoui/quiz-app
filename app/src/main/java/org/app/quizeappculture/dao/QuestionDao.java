package org.app.quizeappculture.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.app.quizeappculture.entites.Question;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert
    void insert(Question question);

    @Query("SELECT * FROM questions")
    List<Question> getAllQuestions();
}
