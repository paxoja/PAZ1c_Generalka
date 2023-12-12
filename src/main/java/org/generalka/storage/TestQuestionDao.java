package org.generalka.storage;

import java.util.List;

public interface TestQuestionDao {
    void saveTestQuestion(TestQuestion testQuestion);

    List<TestQuestion> getTestQuestionsByTestId(int testId);
}

