package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import monitoring.repository.TimeSearchCond;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private RecordRepository recordRepository;
    private TimeRepository timeRepository;

    public Record create(Record record) {
        return recordRepository.save(record);
    }

    public Record get(Long id) {
        return recordRepository.findByIdWithTimes(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Record 입니다."));
    }

    public List<Record> getList(TimeSearchCond cond) {
        List<Record.Time> times = timeRepository.search(cond);

        return times.stream()
                .map(Record.Time::getRecord)
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }

}
