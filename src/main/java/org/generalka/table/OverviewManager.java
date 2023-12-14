package org.generalka.table;

import org.generalka.storage.EntityNotFoundException;
import org.generalka.storage.Test;

import java.util.List;

public interface OverviewManager {

    List<TestOverview> getTestSummary() throws EntityNotFoundException;

}
