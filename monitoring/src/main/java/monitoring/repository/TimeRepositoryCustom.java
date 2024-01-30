package monitoring.repository;

import monitoring.domain.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TimeRepositoryCustom {

    List<Record.Time> search(TimeSearchCond cond);

    List<Record.Time> searchOverlappingTimesWithRecord(LocalDate date, LocalTime start, LocalTime end);
}
