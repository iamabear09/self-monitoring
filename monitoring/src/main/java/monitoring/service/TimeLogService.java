package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import monitoring.repository.TimeLogRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeLogService {

    private TimeLogRepository timeLogRepository;

    public List<TimeLog> save(Record record, List<TimeLog> timeLogDataList) {

        List<TimeLog> timeLogs = timeLogDataList
                .stream()
                .map(t -> TimeLog.builder()
                        .date(t.getDate())
                        .startTime(t.getStartTime())
                        .endTime(getEndTimeFrom(t))
                        .durationMinutes(getDurationFrom(t))
                        .record(record)
                        .build())
                .toList();

        return timeLogRepository.saveAll(timeLogs);

    }
    private Duration getDurationFrom(TimeLog timeLog) {
        if (timeLog.getDurationMinutes() != null) {
            return timeLog.getDurationMinutes();
        }
        return Duration.between(timeLog.getStartTime(), timeLog.getEndTime());
    }

    private LocalTime getEndTimeFrom(TimeLog timeLog) {

        if (timeLog.getEndTime() != null) {
            return timeLog.getEndTime();
        }
        return timeLog.getStartTime().plusMinutes(timeLog.getDurationMinutes().toMinutes());
    }


    public List<TimeLog> getTimeLogsByRecordId(Long recordId) {
        return timeLogRepository.findByRecordId(recordId);
    }
}

