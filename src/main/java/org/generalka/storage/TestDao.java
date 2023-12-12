package org.generalka.storage;

import java.util.List;

public interface TestDao {
    void saveTest(Test test);
    List<Test> getAllTests();
}

