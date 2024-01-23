package monitoring.api.record;

import lombok.*;
import monitoring.domain.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatchUpdateRecordRequestDto {

    private String action;
    private String memo;
    private List<Time> timeRecords;

    @Builder(access = AccessLevel.PACKAGE)
    public PatchUpdateRecordRequestDto(String action, String memo, List<Time> timeRecords) {
        this.action = action;
        this.memo = memo;
        this.timeRecords = timeRecords;
    }

    public Record toUpdateRecordWith(Record originRecord) {

        Record updateRecord = new Record(originRecord.getRecordId(), Optional.ofNullable(action).orElse(originRecord.getAction()), Optional.ofNullable(memo).orElse(memo));

        if (timeRecords.isEmpty()) {
            //기존 Time 값을 채운다.
            originRecord.getTimeRecords().forEach(t -> {
                new Record.Time(t.getTimeId(), t.getDate(), t.getStartTime(), t.getDurationMinutes(), updateRecord);
            });
        } else {
            //전달받은 Time 값이 있으면 해당 값으로 채운다.
            timeRecords.forEach(t -> {
                new Record.Time(null, t.getDate(), t.getStartTime(), t.getDurationMinutes(), updateRecord);
            });
        }

        return updateRecord;
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Time {

        private LocalDate date;
        private LocalTime startTime;
        private Integer durationMinutes;

        @Builder(access = AccessLevel.PACKAGE)
        public Time(LocalDate date, LocalTime startTime, Integer durationMinutes) {
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
        }
    }

}

