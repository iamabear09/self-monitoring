package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import monitoring.service.request.RecordSearchCond;
import monitoring.service.response.UpdateRecordResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    public Record getRecordWithTimeLogs(Long recordId) {
        Record record = recordService.get(recordId);
        List<TimeLog> timeLogs = timeLogService.getByRecordId(recordId);

        /*
         * Proxy 제거하고 실제 값을 사용한다.
         */
        connectRecordAndTimeLogList(record, timeLogs);
        return record;
    }

    public List<Record> getRecords(RecordSearchCond cond) {

        List<TimeLog> timeLogs = timeLogService.searchOverlappingTimeLogs(cond.getDate(), cond.getStartTime(), cond.getEndTime());
        Set<Record> searchRecordSet = timeLogs.stream()
                .map(t -> this.getRecordWithTimeLogs(t.getRecord().getId()))
                .filter(record -> {
                    if (!StringUtils.hasText(cond.getAction())) {
                        return true;
                    }
                    return record.getAction()
                            .toLowerCase()
                            .contains(cond.getAction().toLowerCase());
                })
                .collect(Collectors.toSet());

        return searchRecordSet.stream().toList();
    }


    @Transactional
    public Record update(Long id, Record recordData) {
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

    @Transactional
    public UpdateRecordResult updateWithSideEffect(Long id, Record recordData) {

        Set<Record> deletedRecordSet = new HashSet<>();
        Set<Record> changedRecordSet = new HashSet<>();

        recordData.getTimeLogs().forEach(timeData -> {
            timeLogService.searchOverlappingTimeLogs(timeData.getDate(), timeData.getStartTime(), timeData.getEndTime()).forEach(overlapTime -> {

                // 현재 업데이트 하려는 Record 의 TimeLog 인 경우, 어차피 다 지워질 것이므로 PASS
                if (Objects.equals(overlapTime.getRecord().getId(), id)) {
                    return;
                }

                Record overlapRecord = getRecordWithTimeLogs(overlapTime.getRecord().getId());

                List<TimeLog> createdTimeLogs = timeLogService.splitByRemovingOverlapTimeRange(overlapTime, timeData.getStartTime(), timeData.getEndTime());
                disconnectRecordAndTimeLogList(overlapRecord, List.of(overlapTime));
                overlapRecord.getTimeLogs().addAll(createdTimeLogs);

                if (createdTimeLogs.isEmpty() && overlapRecord.getTimeLogs().isEmpty()) {
                    recordService.delete(overlapRecord.getId());
                    // TimeLog 가 2개 이상였을 경우, 앞에서 changedRecordSet 에 들어가 있을 수 있다.
                    changedRecordSet.remove(overlapRecord);
                    deletedRecordSet.add(overlapRecord);
                    return;
                }
                changedRecordSet.add(overlapRecord);
            });
        });

        Record updatedRecord = update(id, recordData);
        return new UpdateRecordResult(deletedRecordSet.stream().toList(), changedRecordSet.stream().toList(), updatedRecord);
    }

    @Transactional
    public Record delete(Long id) {
        List<TimeLog> deletedTimeLogs = timeLogService.deleteByRecordId(id);
        Record deleteRecord = recordService.delete(id);

        deleteRecord.setTimeLogs(deletedTimeLogs);
        return deleteRecord;
    }

    private void connectRecordAndTimeLogList(Record record, List<TimeLog> timeLogs) {
        record.setTimeLogs(new ArrayList<>(timeLogs));
        timeLogs.forEach(timeLog -> {
            if (timeLog.getRecord() == record) return;
            timeLog.setRecord(record);
        });
    }

    private void disconnectRecordAndTimeLogList(Record record, List<TimeLog> deletedTimeLogs) {
        List.copyOf(deletedTimeLogs).forEach(deletedTimeLog -> {
            if (record.getTimeLogs() != null && !record.getTimeLogs().isEmpty()) {
                record.getTimeLogs().remove(deletedTimeLog);
            }
            deletedTimeLog.setRecord(null);
        });
    }

}

