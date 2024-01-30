package monitoring.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import monitoring.repository.TimeSearchCond;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Getter
public class RecordService {

    private final RecordRepository recordRepository;
    private final TimeRepository timeRepository;

    @Transactional
    public Record create(Record record) {
        return recordRepository.save(record);
    }

    public Record get(Long id) {
        return recordRepository.findByIdWithTimes(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Record 입니다."));
    }

    public List<Record> getList(TimeSearchCond cond) {
        List<Record.Time> times = timeRepository.search(cond);

        return times.stream()
                .map(Record.Time::getRecord)
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }

    @Transactional
    public Record updateAllowingTimeOverlap(Long id, Record updateRecordData) {
        Record record = recordRepository.findByIdWithTimes(id)
                .orElseThrow(() -> new IllegalArgumentException("존자히자 않는 Record 입니다."));

        record.updateContent(updateRecordData);

        record.deleteAllTimes();
        timeRepository.deleteAllInBatch(record.getTimeRecords());

        updateRecordData.getTimeRecords().forEach(t -> {
            record.addTime(t);
            timeRepository.save(t);
        });

        return record;
    }

    @Transactional
    public Record updateNotAllowingTimeOverlap(Long id, Record updateRecordData) {


        //handle overlapping part
        updateRecordData.getTimeRecords().forEach(updateTimeData -> {
            timeRepository.searchOverlappingTimesWithRecord(updateTimeData.getDate(), updateTimeData.getStartTime(), updateTimeData.getEndTime()).forEach(time -> {

                Record affectedRecord = time.getRecord();

                if (Objects.equals(affectedRecord.getId(), id)) {
                    return;
                }
                if (!hasOverlappingTimePart(time, updateTimeData)) {
                    return;
                }

                if (isInside(time, updateTimeData)) {

                    affectedRecord.deleteTime(time);
                    timeRepository.delete(time);

                    if (affectedRecord.getTimeRecords().isEmpty()) {
                        recordRepository.delete(affectedRecord);
                    }
                    return;
                }

                if (doseContainAll(time, updateTimeData)) {
                    Duration frontPartDuration = Duration.between(time.getStartTime(), updateTimeData.getStartTime());
                    Record.Time frontPart = Record.Time.builder()
                            .date(time.getDate())
                            .startTime(time.getStartTime())
                            .durationMinutes((int) frontPartDuration.toMinutes())
                            .build();

                    Duration rearPartDuration = Duration.between(updateTimeData.getEndTime(), time.getEndTime());
                    Record.Time rearPart = Record.Time.builder()
                            .date(time.getDate())
                            .startTime(updateTimeData.getEndTime())
                            .durationMinutes((int) rearPartDuration.toMinutes())
                            .build();

                    affectedRecord.deleteTime(time);
                    affectedRecord.addTime(frontPart);
                    affectedRecord.addTime(rearPart);

                    timeRepository.delete(time);
                    timeRepository.saveAll(List.of(frontPart, rearPart));

                    return;
                }

                if (time.getStartTime().isBefore(updateTimeData.getStartTime())) {
                    time.updateTimeRange(time.getStartTime(), updateTimeData.getStartTime());
                }
                if (time.getStartTime().isAfter(updateTimeData.getStartTime())) {
                    time.updateTimeRange(updateTimeData.getEndTime(), time.getEndTime());
                }

                timeRepository.save(time);
            });

        });

        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 record 입니다."));
        record.updateContent(updateRecordData);

        record.deleteAllTimes();
        timeRepository.deleteAllInBatch(record.getTimeRecords());

        updateRecordData.getTimeRecords().forEach(time -> {
            record.addTime(time);
            timeRepository.save(time);
        });

        return record;
    }

    private boolean doseContainAll(Record.Time time, Record.Time range) {
        // <--------time--------->
        //     <----range----->
        return time.getStartTime().isBefore(range.getStartTime()) && time.getEndTime().isAfter(range.getEndTime());
    }

    private boolean isInside(Record.Time time, Record.Time range) {
        // <--------range-------->
        //     <----time----->
        return !time.getStartTime().isBefore(range.getStartTime()) && !time.getEndTime().isAfter(range.getEndTime());
    }

    private boolean hasOverlappingTimePart(Record.Time time, Record.Time range) {
        // <----range----> <----time----->
        if (time.getStartTime().equals(range.getEndTime()) || time.getStartTime().isAfter(range.getEndTime())) {
            return false;
        }
        // <----time-----> <----range---->
        if (time.getEndTime().equals(range.getStartTime()) || time.getEndTime().isBefore(range.getStartTime())) {
            return false;
        }

        return true;
    }

    @Transactional
    public Record delete(Long id) {
        Record record = recordRepository.findByIdWithTimes(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Id 입니다."));
        timeRepository.deleteAllInBatch(record.getTimeRecords());
        recordRepository.delete(record);

        return record;
    }

}

