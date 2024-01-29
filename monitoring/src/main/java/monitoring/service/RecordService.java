package monitoring.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import monitoring.repository.TimeSearchCond;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Getter
public class RecordService {

    private final RecordRepository recordRepository;
    private final TimeRepository timeRepository;

    @Transactional
    public Record create(Record record) {
        return recordRepository.save(record);
    }

    public Record get(Long id) {
        return recordRepository.findByIdWithTimes(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Record 입니다."));
    }

    @Transactional
    public List<Record> getList(TimeSearchCond cond) {
        List<Record.Time> times = timeRepository.search(cond);

        return times.stream()
                .map(Record.Time::getRecord)
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }

    @Transactional
    public Record update(Long id, Record updateRecordData) {
        Record record = recordRepository.findByIdWithTimes(id)
                .orElseThrow(() -> new IllegalArgumentException("존자히자 않는 Record 입니다."));

        record.updateContent(updateRecordData);

        record.deleteAllTimes();
        timeRepository.deleteAllInBatch(record.getTimeRecords());

        updateRecordData.getTimeRecords().forEach(t -> {
            record.addTime(t);
            timeRepository.save(t);
        });

        return record;
    }

    @Transactional
    public Record delete(Long id) {
        Record record = recordRepository.findByIdWithTimes(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Id 입니다."));
        timeRepository.deleteAllInBatch(record.getTimeRecords());
        recordRepository.delete(record);

        return record;
    }

}
