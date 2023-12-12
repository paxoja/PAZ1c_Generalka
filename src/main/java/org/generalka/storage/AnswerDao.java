package org.generalka.storage;

import java.util.List;

public interface AnswerDao {

    // Save a new answer
    void saveAnswer(Answer answer) throws EntityNotFoundException;;

    // Update an existing answer
    void updateAnswer(Answer answer) throws EntityNotFoundException;;

    // Delete an answer by its ID
    void deleteAnswer(int answerId) throws EntityNotFoundException;;

    // Get an answer by its ID
    Answer getAnswerById(int answerId) throws EntityNotFoundException;;

    // Get all answers
    List<Answer> getAllAnswers() throws EntityNotFoundException;;

    List<Answer> getAnswersByQuestionId(int questionId) throws EntityNotFoundException;;
}

