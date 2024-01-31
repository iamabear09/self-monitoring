package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional
    public Record save(Record recordData) {
        Record record = Record.builder()
                .action(recordData.getAction())
                .memo(recordData.getMemo())
                .build();

        return recordRepository.save(record);
    }

    public Record get(Long id) {

        return recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Record 입니다."));
    }
}

