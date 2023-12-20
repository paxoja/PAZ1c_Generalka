package org.generalka.storage;

import java.util.List;

public interface TestHistoryDao {
    // save a new test history entry
    void saveTestHistory(TestHistory testHistory) throws EntityNotFoundException;

    // used for displaying done tests in profile
    List<TestHistory> getTestHistoryByUserId(Long userId) throws EntityNotFoundException;

}

