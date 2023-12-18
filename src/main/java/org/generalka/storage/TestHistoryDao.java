package org.generalka.storage;

import java.util.List;

public interface TestHistoryDao {
    TestHistory getTestHistoryById(Long id) throws EntityNotFoundException;
    List<TestHistory> getAllTestHistories() throws EntityNotFoundException;
    void deleteTestHistory(Long id) throws EntityNotFoundException;
    // Save a new test history entry
    void saveTestHistory(TestHistory testHistory) throws EntityNotFoundException;

}

