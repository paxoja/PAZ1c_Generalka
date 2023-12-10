package org.generalka.storage;

import java.util.List;

public interface TestDao {
    void addTest(Test test);
    Test getTestById(int id);
    List<Test> getAllTests();
    void updateTest(Test test);
    void deleteTest(int id);
}

