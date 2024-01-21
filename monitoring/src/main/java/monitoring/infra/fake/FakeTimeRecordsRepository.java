package monitoring.infra.fake;

import monitoring.domain.Record;
import monitoring.service.TimeRecordsRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeTimeRecordsRepository implements TimeRecordsRepository {

    private final Map<Long, Record.Time> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0L);

    @Override
    public Record.Time save(Record.Time time) {

        if (time.getTimeId() != null && time.getTimeId() > 0L) {
            throw new IllegalArgumentException("이미 존재하는 데이터입니다.");}

        time.getRecord().delete(time);

        Record.Time savedTime = Record.Time.builder()
                .timeId(idGenerator.incrementAndGet())
                .date(time.getDate())
                .startTime(time.getStartTime())
                .durationMinutes(time.getDurationMinutes())
                .build();

        time.getRecord().addTimeRecord(savedTime);
        storage.put(savedTime.getTimeId(), savedTime);
        return savedTime;
    }

    @Override
    public Optional<Record.Time> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Record.Time update(Record.Time time) {

        if (time.getTimeId() == null || time.getTimeId() == 0L) { throw new IllegalArgumentException("존재하지 않는 데이터 입니다."); }

        if (time.getRecord() == null) { throw new IllegalArgumentException("time 의 record 값은 필수입니다.");}

        //기존 Record 에서 삭제될 Time Record 삭제
        Record.Time oldTime = storage.get(time.getTimeId());
        oldTime.getRecord().delete(oldTime);

        time.getRecord().delete(time);

        Record.Time updatedTimeRecord = Record.Time.builder()
                .timeId(time.getTimeId())
                .date(time.getDate())
                .startTime(time.getStartTime())
                .durationMinutes(time.getDurationMinutes())
                .build();

        time.getRecord().addTimeRecord(updatedTimeRecord);

        storage.put(updatedTimeRecord.getTimeId(), updatedTimeRecord);
        return updatedTimeRecord;
    }

}

