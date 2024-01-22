package monitoring.infra.fake;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.service.RecordsRepository;
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
    public Record save(Record record) {
        if (record.getRecordId() != null && record.getRecordId() > 0L) {
            throw new IllegalArgumentException("이미 존재하는 데이터입니다.");
        }

        Record savedRecord = Record.builder()
                .recordId(idGenerator.incrementAndGet())
                .action(record.getAction())
                .memo(record.getMemo())
                .build();

        storage.put(savedRecord.getRecordId(), savedRecord);

        //저장을 먼저 한 후에
        Set<Record.Time> times = fakeTimeRecordsRepository.saveAll(record.getTimeRecords());

        //연관관계 설정
        savedRecord.addTimeRecords(times);

        return savedRecord;
    }

    @Override
    public Optional<Record> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Record update(Record record) {
        if (record.getRecordId() == null || record.getRecordId() == 0L) {
            throw new IllegalArgumentException("존재하지 않는 데이터입니다.");
        }

        Record oldRecord = storage.get(record.getRecordId());

        Record updatedRecord = Record.builder()
                .recordId(record.getRecordId())
                .action(record.getAction())
                .memo(record.getMemo())
                .build();

        updatedRecord.addTimeRecords(oldRecord.getTimeRecords());

        storage.replace(updatedRecord.getRecordId(), updatedRecord);
        return updatedRecord;
    }


    //for test isolation
    public void clear() {
        storage.clear();
    }
}

