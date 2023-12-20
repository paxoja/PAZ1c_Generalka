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
    public TestQuestion getTestQuestionById(Long testQuestionId) {
        for (TestQuestion question : testQuestions) {
            // check if the current questions id matches the specified testQuestionId
            if (question.getId().equals(testQuestionId)) {
                return question;
            }
        }
        // we return null if no TestQuestion with the specified ID is found
        return null;
    }



    @Override
    public List<TestQuestion> getTestQuestionsByTestId(Long testId) {
        List<TestQuestion> questionsByTestId = new ArrayList<>();
        for (TestQuestion question : testQuestions) {
            // we check if the current question is associated with the specific test id
            if (question.getTest().getId().equals(testId)) {
                questionsByTestId.add(question);
            }
        }

        // return the list of TestQuestions of specific test
        return questionsByTestId;
    }
}
