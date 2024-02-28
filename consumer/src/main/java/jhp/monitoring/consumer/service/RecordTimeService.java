package jhp.monitoring.consumer.service;

import jhp.monitoring.domain.Record;
import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.service.RecordService;
import jhp.monitoring.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecordTimeService {

    private final RecordService recordService;
    private final TimeLogService timeLogService;

    @Transactional
    public Record create(Record recordData) {
        Record savedRecord = recordService.create(recordData);
        List<TimeLog> savedTimeLogs = timeLogService.create(savedRecord, recordData.getTimeLogs());
        connectRecordAndTimeLogList(savedRecord, savedTimeLogs);

        return savedRecord;
    }

    private void connectRecordAndTimeLogList(Record record, List<TimeLog> timeLogs) {
        record.setTimeLogs(new ArrayList<>(timeLogs));
        timeLogs.forEach(timeLog -> {
            if (timeLog.getRecord() == record) return;
            timeLog.setRecord(record);
        });
    }

}



