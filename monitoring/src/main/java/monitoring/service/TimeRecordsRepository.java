package monitoring.service;

import monitoring.domain.Record;

import java.util.Optional;
import java.util.Set;


public interface TimeRecordsRepository {
    Record.Time save(Record.Time time);

    Set<Record.Time> saveAll(Set<Record.Time> timeRecords);

    Optional<Record.Time> findById(Long id);

    Record.Time delete(Long id);

    Set<Record.Time> deleteAll(Set<Record.Time> times);
}

