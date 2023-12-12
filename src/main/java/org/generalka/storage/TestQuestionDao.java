package org.generalka.storage;

import java.util.List;

public interface TestQuestionDao {
    void saveTestQuestion(TestQuestion testQuestion) throws EntityNotFoundException;;

    // Update an existing test question
    void updateTestQuestion(TestQuestion testQuestion) throws EntityNotFoundException;;

    // Delete a test question by its ID
    void deleteTestQuestion(int testQuestionId) throws EntityNotFoundException;;

    // Get a test question by its ID
    TestQuestion getTestQuestionById(int testQuestionId) throws EntityNotFoundException;;

    // Get all test questions
    List<TestQuestion> getAllTestQuestions() throws EntityNotFoundException;;

    List<TestQuestion> getTestQuestionsByTestId(int testId) throws EntityNotFoundException;;
}

