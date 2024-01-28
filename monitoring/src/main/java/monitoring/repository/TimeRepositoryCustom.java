package monitoring.repository;

import monitoring.domain.Record;

import java.util.List;

public interface TimeRepositoryCustom {

    List<Record.Time> search(TimeSearchCond cond);
}
