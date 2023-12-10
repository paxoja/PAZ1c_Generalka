package org.generalka.storage;

import java.util.List;

public interface AnswerDao {
    void addAnswer(Answer answer);
    Answer getAnswerById(int id);
    List<Answer> getAllAnswers();
    void updateAnswer(Answer answer);
    void deleteAnswer(int id);
}

