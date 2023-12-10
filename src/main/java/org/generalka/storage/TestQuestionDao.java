package org.generalka.storage;

import java.util.List;

public interface TestQuestionDao {
    void addTestQuestion(TestQuestion testQuestion);
    TestQuestion getTestQuestionById(int id);
    List<TestQuestion> getAllTestQuestions();
    void updateTestQuestion(TestQuestion testQuestion);
    void deleteTestQuestion(int id);
}

