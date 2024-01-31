package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public Record save(Record recordData) {
        Record record = Record.builder()
                .action(recordData.getAction())
                .memo(recordData.getMemo())
                .build();

        return recordRepository.save(record);
    }
}

