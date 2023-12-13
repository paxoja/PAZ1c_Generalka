package org.generalka.storage;

import java.util.List;

public interface  TestDao {

    void saveTest(Test test) throws EntityNotFoundException;;

    // Delete a test by its ID
    void deleteTest(Long testId) throws EntityNotFoundException;;

    // Get a test by its ID
    Test getTestById(Long testId) throws EntityNotFoundException;;

    // Get all tests
    List<Test> getAllTests() throws EntityNotFoundException;;


}