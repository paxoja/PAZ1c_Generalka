package org.generalka.storage;



public class DaoTests {

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private TestDao testDao = DaoFactory.INSTANCE.getTestDao();
    private TestQuestionDao testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
    private AnswerDao answerDao = DaoFactory.INSTANCE.getAnswerDao();
    private TestHistoryDao testHistoryDao = DaoFactory.INSTANCE.getTestHistoryDao();

}