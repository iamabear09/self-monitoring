package monitoring.infra.fake;

import monitoring.service.RecordsSearchCond;
import monitoring.domain.Record;
import monitoring.service.TimeRecordsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class FakeTimeRecordsRepository implements TimeRecordsRepository {

    private final Map<Long, Record.Time> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0L);

    @Override
    public Record.Time save(Record.Time timeData) {

        if (timeData.getTimeId() == null || timeData.getTimeId() == 0L) {
            Record.Time savedTime = timeData.toTimeWith(idGenerator.incrementAndGet());
            storage.put(savedTime.getTimeId(), savedTime);
            return savedTime;
        }

        Record.Time updatedTime = timeData.toTimeWith(timeData.getTimeId());
        storage.put(updatedTime.getTimeId(), updatedTime);
        return updatedTime;
    }

    @Override
    public Set<Record.Time> saveAll(Set<Record.Time> times) {

        return Set.copyOf(times).stream()
                .map(this::save)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Record.Time> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Set<Record.Time> deleteAll(Set<Record.Time> times) {
        List<Long> ids = times.stream()
                .map(Record.Time::getTimeId)
                .toList();

        return ids.stream()
                .map(this::delete)
                .collect(Collectors.toSet());
    }

    @Override
    public Record.Time delete(Long id) {
        if (id == null || id == 0L) { throw new IllegalArgumentException("존재하지 않는 데이터 입니다."); }

        return storage.remove(id);
    }

    @Override
    public Set<Record.Time> findAll(RecordsSearchCond cond) {

        return storage.values().stream()
                .filter(time -> time.isOnTimeline(cond.getDate(), cond.getTime()))
                .filter(time -> time.hasSameContentAs(cond.getAction(), cond.getMemo()))
                .collect(Collectors.toSet());
    }

}

