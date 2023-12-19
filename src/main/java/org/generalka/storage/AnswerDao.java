package org.generalka.storage;

import java.util.List;

public interface AnswerDao {

    // Save a new answer
    void saveAnswer(Answer answer) throws EntityNotFoundException;

    // Delete an answer by its ID
    void deleteAnswer(Long answerId) throws EntityNotFoundException;

    // Get an answer by its ID
    Answer getAnswerById(Long answerId) throws EntityNotFoundException;

    // Get all answers
    List<Answer> getAllAnswers() throws EntityNotFoundException;

    List<Answer> getAnswersByQuestionId(Long questionId) throws EntityNotFoundException;

    Answer getCorrectAnswerByQuestionId(Long questionId) throws EntityNotFoundException;
}

