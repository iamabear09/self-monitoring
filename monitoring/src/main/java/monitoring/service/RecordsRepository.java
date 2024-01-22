package monitoring.service;

import monitoring.domain.Record;

import java.util.Optional;


public interface RecordsRepository {

    Record save(Record record);

    Optional<Record> findById(Long id);

}

