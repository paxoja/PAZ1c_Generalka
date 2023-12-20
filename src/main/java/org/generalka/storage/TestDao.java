package org.generalka.storage;

import java.util.List;

public interface TestDao {

    void saveTest(Test test) throws EntityNotFoundException;

    // delete a test based on its id
    void deleteTest(Long testId) throws EntityNotFoundException;

    // get a test bsed on id
    Test getTestById(Long testId) throws EntityNotFoundException;

    // get all tests made so far
    List<Test> getAllTests() throws EntityNotFoundException;

    // used in results table - how many points are there for test
    int getNumberOfQuestions(Long testId) throws EntityNotFoundException;

}