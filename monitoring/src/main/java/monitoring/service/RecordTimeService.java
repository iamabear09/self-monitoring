package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
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

        connectRecordAndTimeLogs(savedRecord, savedTimeLogs);

        return savedRecord;
    }

    public Record get(Long id) {
        Record record = recordService.get(id);
        List<TimeLog> timeLogs = timeLogService.getTimeLogsByRecordId(id);

        connectRecordAndTimeLogs(record, timeLogs);
        return record;
    }

    private void connectRecordAndTimeLogs(Record record, List<TimeLog> timeLogs) {
        timeLogs.forEach(timeLog -> {
            if (!record.getTimeLogs().contains(timeLog)) {
                record.getTimeLogs().add(timeLog);
            }
            if (timeLog.getRecord() == null || timeLog.getRecord() != record) {
                timeLog.setRecord(record);
            }
        });
    }

    private void disconnectRecordAndTimeLogs(List<TimeLog> deletedTimeLogs, Record record) {
        deletedTimeLogs.forEach(deletedTimeLog -> {
            if (record.getTimeLogs() != null && !record.getTimeLogs().isEmpty()) {
                record.getTimeLogs().remove(deletedTimeLog);
            }
            deletedTimeLog.setRecord(null);
        });
    }


    @Transactional
    public Record updateAllowingOverlap(Long id, Record recordData) {

        Record record = recordService.get(id);
        List<TimeLog> deletedTimeLogs = timeLogService.deleteByRecordId(id);
        disconnectRecordAndTimeLogs(deletedTimeLogs, record);

        Record updatedRecord = recordService.update(id, recordData);
        List<TimeLog> savedTimeLogs = timeLogService.save(updatedRecord, recordData.getTimeLogs());
        connectRecordAndTimeLogs(updatedRecord, savedTimeLogs);

        return updatedRecord;
    }



    @Transactional
    public Record updateNotAllowingOverlap(Long id, Record recordData) {


        recordData.getTimeLogs().forEach(timeData -> {

            List<TimeLog> overlapTimeLogs = timeLogService
                    .searchOverlappingTimeLogs(timeData.getDate(), timeData.getStartTime(), timeData.getEndTime());

            overlapTimeLogs.forEach(timeLog -> {

                if (isInsideOfRange(timeLog, timeData.getStartTime(), timeData.getEndTime())) {
                    Long recordId = timeLog.getRecord().getId();

                    timeLogService.delete(timeLog.getId());

                    if (timeLogService.getTimeLogsByRecordId(recordId).isEmpty()) {
                        Record deletedRecord = recordService.delete(recordId);
                        //deleteRecord 정보를 따로 모아놔야한다.
                    }
                    return;
                }
/*
                if (timeLogService.isIncludeRange(t.getId(), timeData.getStartTime(), timeData.getEndTime())) {
                    List<TimeLog> splitTimeLogs = timeLogService.splitTimeByRemovingOverrplingPart();
                    return;
                }

                if () {
                    //timeLog update
                }
                if () {
                    //timeLog update
                }

            });*/
            });
        });

        return updateAllowingOverlap(id, recordData);
    }



    private boolean isInsideOfRange(TimeLog timeLog, LocalTime startTime, LocalTime endTime) {

        // <----range---->
        //   <--time-->    양 끝이 겹치는 것 까지 포함한다.
        return !timeLog.getStartTime().isBefore(startTime) && !timeLog.getEndTime().isAfter(endTime);
    }

}

