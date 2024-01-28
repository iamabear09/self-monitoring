package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.domain.Time;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;
    private final TimeRepository timeRepository;

    @Transactional
    public Record create(Record recordData) {

        Record record = Record.builder()
                .action(recordData.getAction())
                .memo(recordData.getMemo())
                .build();
        Record savedRecord = recordRepository.save(record);

        List<Time> timeList = recordData.getTimeRecords()
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

    public Record get(Long id) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id 입니다."));
        //Lazy loading 하지 않고 Get 조회
        List<Time> times = timeRepository.findByRecordId(id);

        record.setTimeRecords(times);
        times.forEach(t -> {
            t.setRecord(record);
        });

        return record;
    }

    @Transactional
    public Record update(Long id, Record recordData) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id 입니다."));

        updateContent(record, recordData);
        Record savedRecord = recordRepository.save(record);

        updateTime(savedRecord, recordData);
        return savedRecord;
    }


    private void updateTime(Record record, Record updateData) {
        if (updateData.getTimeRecords() == null || updateData.getTimeRecords().isEmpty()) return;

        //delete origin times first
        List<Time> originTimes = timeRepository.findByRecordId(record.getId());

        timeRepository.deleteAllInBatch(originTimes);

        //update
        List<Time> updatedTimes = updateData.getTimeRecords()
                .stream()
                .map(t -> {
                    t.setRecord(record);
                    return timeRepository.save(t);
                })
                .toList();
        record.setTimeRecords(updatedTimes);

    }

    private void updateContent(Record record, Record updateData) {
        if (StringUtils.hasText(updateData.getAction())) {
            record.setAction(updateData.getAction());
        }
        if (StringUtils.hasText(updateData.getMemo())) {
            record.setMemo(updateData.getMemo());
        }
    }

}
