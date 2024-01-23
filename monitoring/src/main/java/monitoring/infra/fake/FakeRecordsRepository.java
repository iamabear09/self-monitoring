package monitoring.infra.fake;

import monitoring.domain.Record;
import monitoring.service.RecordsRepository;
import monitoring.service.RecordsSearchCond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class FakeRecordsRepository implements RecordsRepository {
    private final Map<Long, Record> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0L);
    private final FakeTimeRecordsRepository fakeTimeRecordsRepository;


    @Autowired
    public FakeRecordsRepository(FakeTimeRecordsRepository fakeTimeRecordsRepository) {
        this.fakeTimeRecordsRepository = fakeTimeRecordsRepository;
    }

    @Override
    public Optional<Record> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Record save(Record recordData) {

        if (recordData.getRecordId() == null || recordData.getRecordId() == 0L) {
            Record saveRecord = recordData.toRecordWith(idGenerator.incrementAndGet());
            storage.put(saveRecord.getRecordId(), saveRecord);

            fakeTimeRecordsRepository.saveAll(saveRecord.getTimeRecords());
            return saveRecord;
        }

        //기존 Time Record 삭제
        Record oldRecord = storage.get(recordData.getRecordId());
        fakeTimeRecordsRepository.deleteAll(oldRecord.getTimeRecords());

        Record updateRecord = recordData.toRecordWith(recordData.getRecordId());
        storage.put(updateRecord.getRecordId(), updateRecord);

        fakeTimeRecordsRepository.saveAll(updateRecord.getTimeRecords());
        return updateRecord;
    }

    @Override
    public Set<Record> findAll(RecordsSearchCond cond) {
        Set<Record.Time> times = fakeTimeRecordsRepository.findAll(cond);

        return times.stream()
                .map(Record.Time::getRecord)
                .collect(Collectors.toSet());
    }

    //for test isolation
    public void clear() {
        storage.clear();
    }
}

