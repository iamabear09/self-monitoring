package monitoring.service;

import monitoring.domain.Record;

import java.util.Optional;
import java.util.Set;


public interface RecordsRepository {

    Record save(Record record);

    Set<Record> findAll(RecordsSearchCond cond);

    Optional<Record> findById(Long id);

}

