package jhp.monitoring.consumer.service;

import jhp.monitoring.domain.Record;
import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.service.RecordService;
import jhp.monitoring.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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


    @Transactional
    public void update(String id, Record recordData) {
        Record updatedRecord = recordService.update(id, recordData);

        if (recordData.getTimeLogs() != null && !recordData.getTimeLogs().isEmpty()) {
            timeLogService.deleteByRecordId(id);
            List<TimeLog> savedTimeLogs = timeLogService.create(updatedRecord, recordData.getTimeLogs());
            connectRecordAndTimeLogList(updatedRecord, savedTimeLogs);
        } else {
            List<TimeLog> notUpdatedTimeLogs = timeLogService.getByRecordId(id);
            connectRecordAndTimeLogList(updatedRecord, notUpdatedTimeLogs);
        }
    }

    /*
    * Record 의 TimeLog 가 변경되는 경우,
    * 변경된 TimeLog 의 구간이 다른 Record 의 TimeLog 와 겹칠 수 있다.
    * 이때, 다른 Record 의 TimeLog 에서 변경된 TimeLog 와 겹치는 구간을 제거한다.
    */
    @Transactional
    public void updateWithRemovingDuplicatedTimeLogs(String id, Record recordData) {

        recordData.getTimeLogs()
                .forEach(timeData -> {
                    // 변경 하는 TimeLog 와 중복된 Time Log 를 찾아서 중복 구간을 제거한다.
                    timeLogService.searchOverlappingTimeLogs(timeData.getDate(), timeData.getStartTime(), timeData.getEndTime())
                            .forEach(overlapTime -> {
                                // 현재 업데이트 하고 있는 Record 의 TimeLog 는 어차피 나중에 업데이트 되므로 중복을 제거할 필요가 없다.
                                if (Objects.equals(overlapTime.getRecord().getId(), id)) {
                                    return;
                                }
                                Record overlapRecord = getRecordWithTimeLogs(overlapTime.getRecord().getId());
                                timeLogService.splitByRemovingOverlapTimeRange(overlapTime, timeData.getStartTime(), timeData.getEndTime());
                            });
                });
        update(id, recordData);
    }

    private Record getRecordWithTimeLogs(String recordId) {
        Record record = recordService.get(recordId);
        List<TimeLog> timeLogs = timeLogService.getByRecordId(recordId);
        connectRecordAndTimeLogList(record, timeLogs);
        return record;
    }
}



