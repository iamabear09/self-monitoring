package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordTimeService {

    private final RecordService recordService;
    private final TimeLogService timeLogService;

    public Record save(Record recordData) {
        Record savedRecord = recordService.save(recordData);
        List<TimeLog> savedTimeLogs = timeLogService.save(savedRecord, recordData.getTimeLogs());

        savedRecord.setTimeLogs(savedTimeLogs);

        return savedRecord;
    }
}
