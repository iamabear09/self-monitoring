package monitoring.service;

import monitoring.domain.Record;

import java.util.Optional;


public interface TimeRecordsRepository {
    Record.Time save(Record.Time time);

    Optional<Record.Time> findById(Long id);

    Record.Time update(Record.Time time);
}

