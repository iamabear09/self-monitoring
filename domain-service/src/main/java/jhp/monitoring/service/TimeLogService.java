package jhp.monitoring.service;

import lombok.RequiredArgsConstructor;
import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.domain.Record;
import jhp.monitoring.repository.TimeLogRepository;
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
    public List<TimeLog> create(Record record, List<TimeLog> timeLogDataList) {
        if (timeLogDataList == null || timeLogDataList.isEmpty()) {
            return new ArrayList<>();
        }

        return timeLogDataList
                .stream()
                .map(timeLogData -> timeLogRepository.save(createTimeLogFromData(record, timeLogData)))
                .toList();
    }

    public List<TimeLog> getByRecordId(String recordId) {
        return timeLogRepository.findByRecordId(recordId);
    }

    @Transactional
    public List<TimeLog> deleteByRecordId(String recordId) {
        List<TimeLog> timeLogs = timeLogRepository.findByRecordId(recordId);
        timeLogRepository.deleteAllInBatch(timeLogs);
        return timeLogs;
    }

    public List<TimeLog> searchOverlappingTimeLogs(LocalDate date, LocalTime start, LocalTime end) {
        if (date == null || start == null || end == null) {
            throw new IllegalArgumentException("모든 값을 필수 입니다.");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start 는 end 보다 작아야 합니다.");
        }

        List<TimeLog> timeLogs = timeLogRepository.findByDate(date);

        return timeLogs
                .stream()
                .filter(timeLog -> isOverlapped(timeLog, start, end))
                .toList();
    }

    @Transactional
    public List<TimeLog> splitByRemovingOverlapTimeRange(TimeLog timeLog, LocalTime startTime, LocalTime endTime) {

        if (!isOverlapped(timeLog, startTime, endTime)) {
            throw new IllegalArgumentException("겹치는 구간이 없습니다.");
        }


        List<TimeLog> createdTimeLogs = new ArrayList<>();

        if (timeLog.getStartTime().isBefore(startTime)) {
            TimeLog frontPart = TimeLog.builder()
                    .date(timeLog.getDate())
                    .startTime(timeLog.getStartTime())
                    .endTime(startTime)
                    .durationMinutes(Duration.between(timeLog.getStartTime(), startTime))
                    .record(timeLog.getRecord())
                    .build();

            createdTimeLogs.add(frontPart);
        }

        if (timeLog.getEndTime().isAfter(endTime)) {
            TimeLog rearPart = TimeLog.builder()
                    .date(timeLog.getDate())
                    .startTime(endTime)
                    .endTime(timeLog.getEndTime())
                    .durationMinutes(Duration.between(endTime, timeLog.getEndTime()))
                    .record(timeLog.getRecord())
                    .build();
            createdTimeLogs.add(rearPart);
        }

        timeLogRepository.deleteById(timeLog.getId());
        return timeLogRepository.saveAll(createdTimeLogs);
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
}

