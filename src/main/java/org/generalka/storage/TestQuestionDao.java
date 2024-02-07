package org.generalka.storage;

import java.util.List;

public interface TestQuestionDao {

    // save test question
    void saveTestQuestion(TestQuestion testQuestion) throws EntityNotFoundException;


    // get a test question by its id
    TestQuestion getTestQuestionById(Long testQuestionId) throws EntityNotFoundException;

    // get questions which belong to specific test
    List<TestQuestion> getTestQuestionsByTestId(Long testId) throws EntityNotFoundException;

    void deleteTestQuestion(Long testQuestionId) throws EntityNotFoundException;

    // update a test question
    void updateTestQuestion(TestQuestion testQuestion) throws EntityNotFoundException;


}
