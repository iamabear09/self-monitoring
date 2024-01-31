package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordTimeService {

    private final RecordService recordService;
    private final TimeLogService timeLogService;

    @Transactional
    public Record save(Record recordData) {
        Record savedRecord = recordService.save(recordData);
        List<TimeLog> savedTimeLogs = timeLogService.save(savedRecord, recordData.getTimeLogs());

        savedRecord.setTimeLogs(savedTimeLogs);

        return savedRecord;
    }

    public Record get(Long id) {
        Record record = recordService.get(id);
        List<TimeLog> timeLogs = timeLogService.getTimeLogsByRecordId(id);

        record.setTimeLogs(timeLogs);
        return record;
    }

    @Transactional
    public Record updateAllowingOverlap(Long id, Record recordData) {

        timeLogService.deleteByRecordId(id);

        Record updatedRecord = recordService.update(id, recordData);

        List<TimeLog> savedTimeLogs = timeLogService.save(updatedRecord, recordData.getTimeLogs());
        updatedRecord.setTimeLogs(savedTimeLogs);

        return updatedRecord;
    }

}

