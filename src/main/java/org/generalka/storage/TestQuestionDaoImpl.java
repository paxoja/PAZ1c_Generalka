package org.generalka.storage;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionDaoImpl implements TestQuestionDao {
    private List<TestQuestion> testQuestions = new ArrayList<>();

    @Override
    public void saveTestQuestion(TestQuestion testQuestion) {
        testQuestions.add(testQuestion);
    }

    @Override
    public void deleteTestQuestion(Long testQuestionId) {
        testQuestions.removeIf(question -> question.getId().equals(testQuestionId));
    }

    @Override
    public TestQuestion getTestQuestionById(Long testQuestionId) {
        return testQuestions.stream()
                .filter(question -> question.getId().equals(testQuestionId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<TestQuestion> getAllTestQuestions() {
        return new ArrayList<>(testQuestions);
    }

    @Override
    public List<TestQuestion> getTestQuestionsByTestId(Long testId) {
        List<TestQuestion> questionsByTestId = new ArrayList<>();
        for (TestQuestion question : testQuestions) {
            if (question.getTest().getId().equals(testId)) {
                questionsByTestId.add(question);
            }
        }
        return questionsByTestId;
    }
}
