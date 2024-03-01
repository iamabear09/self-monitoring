package jhp.monitoring.consumer.service;

import jhp.monitoring.consumer.service.response.UpdateRecordResult;
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
    public Record update(String id, Record recordData) {
        Record updatedRecord = recordService.update(id, recordData);

        if (recordData.getTimeLogs() != null && !recordData.getTimeLogs().isEmpty()) {
            timeLogService.deleteByRecordId(id);
            List<TimeLog> savedTimeLogs = timeLogService.create(updatedRecord, recordData.getTimeLogs());
            connectRecordAndTimeLogList(updatedRecord, savedTimeLogs);
        } else {
            List<TimeLog> notUpdatedTimeLogs = timeLogService.getByRecordId(id);
            connectRecordAndTimeLogList(updatedRecord, notUpdatedTimeLogs);
        }

        return updatedRecord;
    }

    /*
    * Record 의 TimeLog 가 변경되는 경우,
    * 변경된 TimeLog 의 구간이 다른 Record 의 TimeLog 와 겹칠 수 있다.
    * 이때, 다른 Record 의 TimeLog 에서 변경된 TimeLog 와 겹치는 구간을 제거한다.
    */
    @Transactional
    public UpdateRecordResult updateWithRemovingDuplicatedTimeLogs(String id, Record recordData) {

        UpdateRecordResult updateRecordResult = new UpdateRecordResult();

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
                                List<TimeLog> createdTimeLogs = timeLogService.splitByRemovingOverlapTimeRange(overlapTime, timeData.getStartTime(), timeData.getEndTime());

                                // update TimeLogs in Record - 영속성 컨텍스트 때문에 객체의 상태도 변경
                                overlapRecord.getTimeLogs().remove(overlapTime);
                                overlapRecord.getTimeLogs().addAll(createdTimeLogs);

                                //it means record has no timeLogs. So let's delete record.
                                if (createdTimeLogs.isEmpty()) {
                                    updateRecordResult
                                            .getDeletedRecordIdSet()
                                            .add(overlapRecord.getId());
                                    recordService.delete(overlapRecord.getId());
                                } else {
                                    updateRecordResult
                                            .getChangedRecordIdSet()
                                            .add(overlapRecord.getId());
                                }
                            });
                });

        updateRecordResult.removeDuplicatedIds();

        Record updateRecord = update(id, recordData);
        updateRecordResult.setUpdatedRecord(updateRecord);

        return updateRecordResult;
    }

    Record getRecordWithTimeLogs(String recordId) {
        Record record = recordService.get(recordId);
        List<TimeLog> timeLogs = timeLogService.getByRecordId(recordId);
        connectRecordAndTimeLogList(record, timeLogs);
        return record;
    }
}



