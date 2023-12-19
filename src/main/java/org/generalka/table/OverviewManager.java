package org.generalka.table;

import org.generalka.storage.EntityNotFoundException;
import org.generalka.storage.Test;
import org.generalka.storage.TestHistoryDao;

import java.util.List;

public interface OverviewManager {

    List<TestOverview> getTestSummary() throws EntityNotFoundException;

    List<TestHistoryProfile> getHistorySummary() throws  EntityNotFoundException;
}
