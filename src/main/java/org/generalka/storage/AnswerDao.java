package org.generalka.storage;

import java.util.List;

public interface AnswerDao {
    void saveAnswer(Answer answer);

    List<Answer> getAnswersByQuestionId(int questionId);
}

