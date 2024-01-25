package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;
    private final TimeRepository timeRepository;

    @Transactional
    public Record create(Record recordData) {
        Record savedRecord = recordRepository.save(recordData);
        timeRepository.saveAll(recordData.getTimeRecords());
        return savedRecord;
    }

    public Record get(Long id) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id 입니다."));
        List<Record.Time> times = timeRepository.findByRecordId(id);

        record.addTimes(times);
        return record;
    }


}
