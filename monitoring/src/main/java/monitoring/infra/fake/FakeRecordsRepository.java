package monitoring.infra.fake;

import monitoring.domain.Record;
import monitoring.service.RecordsRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeRecordsRepository implements RecordsRepository {
    private final Map<Long, Record> recordData = new ConcurrentHashMap<>();
    private final AtomicLong recordIdGenerator = new AtomicLong(0L);

    @Override
    public Record save(Record record) {
        if (record.getRecordId() == null || record.getRecordId() == 0L) {
            Record savedRecord = Record.from(recordIdGenerator.incrementAndGet(), record);
            recordData.put(savedRecord.getRecordId(), savedRecord);
            return savedRecord;
        }
        Record updatedRecord = Record.from(record.getRecordId(), record);
        recordData.replace(updatedRecord.getRecordId(), updatedRecord);
        return updatedRecord;
    }

    @Override
    public Optional<Record> findById(Long id) {
        return Optional.ofNullable(recordData.get(id));
    }
}

