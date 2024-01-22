package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordsService {

    private final RecordsRepository recordsRepository;
    private final TimeRecordsRepository timeRecordsRepository;

    public Record create(Record record) {
        Record savedRecord = recordsRepository.save(record);
        savedRecord.addTimeRecords(record.getTimeRecords());

        record.getTimeRecords()
                .forEach(timeRecordsRepository::save);

        return savedRecord;
    }
}
