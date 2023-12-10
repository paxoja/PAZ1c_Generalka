package org.generalka.storage;

import java.util.List;

public interface TestHistoryDao {
    void addTestHistory(TestHistory testHistory);
    TestHistory getTestHistoryById(int id);
    List<TestHistory> getAllTestHistories();
    void updateTestHistory(TestHistory testHistory);
    void deleteTestHistory(int id);
}

