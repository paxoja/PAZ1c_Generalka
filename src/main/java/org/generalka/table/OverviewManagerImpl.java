package org.generalka.table;

import org.generalka.storage.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// used to fill the tables
public class OverviewManagerImpl implements OverviewManager {

    private TestDao testDao = DaoFactory.INSTANCE.getTestDao();
    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private TestHistoryDao testHistoryDao = DaoFactory.INSTANCE.getTestHistoryDao();

    @Override
    public List<TestOverview> getTestSummary() throws EntityNotFoundException {
        List<Test> tests = testDao.getAllTests();

        List<TestOverview> testOverviews = new ArrayList<>();
        for (Test test : tests) {
            TestOverview testOverview = new TestOverview(
                    test.getId(),
                    test.getTopic(),
                    test.getSubject(),
                    test.getSemester(),
                    test.getYearOfStudy(),
                    test.getIsWholeSemester()
            );

            testOverview.setTopic(test.getTopic());
            testOverview.setSubject(test.getSubject());
            testOverview.setSemester(test.getSemester());
            testOverview.setYearOfStudy(test.getYearOfStudy());
            testOverview.setWholeSemester(test.getIsWholeSemester());

            testOverviews.add(testOverview);
        }

        return testOverviews;
    }

    @Override
    public List<TestHistoryProfile> getHistorySummary() throws EntityNotFoundException {
        Optional<User> currentUser = userDao.getCurrentUser();
        if (currentUser.isPresent()) {
            Long userId = currentUser.get().getId();
            List<TestHistory> testHistories = testHistoryDao.getTestHistoryByUserId(userId);

            List<TestHistoryProfile> testHistoryProfiles = new ArrayList<>();
            for (TestHistory testHistory : testHistories) {
                Test test = testHistory.getTest();
                int totalQuestions = testDao.getNumberOfQuestions(test.getId());

                TestHistoryProfile testHistoryProfile = new TestHistoryProfile(
                        testHistory.getId(),
                        test.getTopic(),
                        test.getSubject(),
                        test.getSemester(),
                        test.getYearOfStudy(),
                        testHistory.getScore(),
                        totalQuestions,
                        testHistory.getDate()
                );

                testHistoryProfiles.add(testHistoryProfile);
            }

            return testHistoryProfiles;
        }
        return Collections.emptyList();
    }
}



