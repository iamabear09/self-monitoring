package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.domain.Time;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final TimeRepository timeRepository;

    @Transactional
    public Record create(Record recordData, List<Time> timeData) {

        Record record = Record.builder()
                .action(recordData.getAction())
                .memo(recordData.getMemo())
                .build();
        Record savedRecord = recordRepository.save(record);

        List<Time> timeList = timeData
                .stream()
                .map(t -> {
                    Time time = Time.builder()
                            .date(t.getDate())
                            .startTime(t.getStartTime())
                            .durationMinutes(t.getDurationMinutes())
                            .endTime(calculateEndTime(t))
                            .record(savedRecord)
                            .build();

                    return timeRepository.save(time);
                })
                .toList();

        savedRecord.setTimeRecords(timeList);

        return savedRecord;
    }
    private LocalTime calculateEndTime(Time t) {
        return Optional.ofNullable(t.getEndTime())
                .orElse(t.getStartTime().plusMinutes(t.getDurationMinutes()));
    }

}
