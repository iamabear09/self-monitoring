package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.common.ErrorMessage;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import monitoring.repository.TimeLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;

    @Transactional
    public List<TimeLog> save(Record record, List<TimeLog> timeLogDataList) {
        if (timeLogDataList == null || timeLogDataList.isEmpty()) {
            return new ArrayList<>();
        }

        return timeLogDataList
                .stream()
                .map(timeLogData -> timeLogRepository.save(createTimeLogFromData(record, timeLogData)))
                .toList();
    }
    private TimeLog createTimeLogFromData(Record record, TimeLog timeLogData) {
        return TimeLog.builder()
                .date(timeLogData.getDate())
                .startTime(timeLogData.getStartTime())
                .endTime(getEndTimeFrom(timeLogData))
                .durationMinutes(getDurationFrom(timeLogData))
                .record(record)
                .build();
    }
    private Duration getDurationFrom(TimeLog timeLog) {
        if (timeLog.getEndTime() != null) {
            return Duration.between(timeLog.getStartTime(), timeLog.getEndTime());
        }
        return timeLog.getDurationMinutes();
    }
    private LocalTime getEndTimeFrom(TimeLog timeLog) {
        if (timeLog.getDurationMinutes() != null) {
            return timeLog.getStartTime().plusMinutes(timeLog.getDurationMinutes().toMinutes());
        }
        return timeLog.getEndTime();
    }


    public List<TimeLog> getTimeLogsByRecordId(Long recordId) {
        return timeLogRepository.findByRecordId(recordId);
    }

    @Transactional
    public List<TimeLog> deleteByRecordId(Long recordId) {
        List<TimeLog> timeLogs = timeLogRepository.findByRecordId(recordId);
        timeLogRepository.deleteAllInBatch(timeLogs);
        return timeLogs;
    }

    public List<TimeLog> searchOverlappingTimeLogs(LocalDate date, LocalTime start, LocalTime end) {
        if (date == null || start == null || end == null) {
            throw new IllegalArgumentException("모든 값을 필수 입니다.");
        }

        List<TimeLog> timeLogs = timeLogRepository.findByDate(date);

        return timeLogs
                .stream()
                .filter(timeLog -> isOverlapped(timeLog, start, end))
                .toList();
    }
    
    private boolean isOverlapped(TimeLog timeLog, LocalTime from, LocalTime to) {

        // 끝점만 겹치는 것은 중복이 아닌것으로 판단한다.
        // <----time Log----><from ----- to>
        if (!timeLog.getEndTime().isAfter(from)) return false;
        // <from ----- to><----time Log---->
        if (!timeLog.getStartTime().isBefore(to)) return false;

        // <from ----- to>
        //      <----time Log----> ... 등등
        return true;
    }
    
    @Transactional
    public TimeLog delete(Long id) {
        TimeLog timeLog = timeLogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ENTITY_NOT_FOUND.getMessage()));
        timeLogRepository.delete(timeLog);

        return timeLog;
        
    }
}

