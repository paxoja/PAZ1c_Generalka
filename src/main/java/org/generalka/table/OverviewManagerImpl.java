package org.generalka.table;

import org.generalka.storage.EntityNotFoundException;
import org.generalka.storage.Test;
import org.generalka.storage.TestDao;
import org.generalka.storage.DaoFactory;

import java.util.ArrayList;
import java.util.List;

public class OverviewManagerImpl implements OverviewManager {

    private TestDao testDao = DaoFactory.INSTANCE.getTestDao();

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
}
