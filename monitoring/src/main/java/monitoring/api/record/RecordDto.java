package monitoring.api.record;

import lombok.*;
import monitoring.domain.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class RecordDto {

    private Long recordId;
    private String action;
    private String memo;

    private Integer timeRecordsNum;
    private List<Time> timeRecords;

    public RecordDto(Long recordId, String action, String memo, List<Time> timeRecords) {
        this.recordId = recordId;
        this.action = action;
        this.memo = memo;
        this.timeRecords = Optional.ofNullable(timeRecords)
                .orElse(new ArrayList<>());
        this.timeRecordsNum = this.timeRecords.size();
    }

    public static RecordDto from(Record record) {

        return new RecordDto(record.getRecordId(), record.getAction(), record.getMemo(), record.getTimeRecords()
                .stream()
                .map(Time::from)
                .toList());
    }


    // To save time records
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @ToString
    public static class Time {
        private Long timeId;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer durationMinutes;

        public Time(Long timeId, LocalDate date, LocalTime startTime, Integer durationMinutes) {
            this.timeId = timeId;
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = Optional
                    .ofNullable(durationMinutes)
                    .orElse(0);
            this.endTime = Optional.ofNullable(startTime)
                    .map(t -> t.plusMinutes(this.durationMinutes))
                    .orElse(null);
        }

        public Time(Long timeId, LocalDate date, LocalTime startTime, LocalTime endTime, Integer durationMinutes) {
            this.timeId = timeId;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.durationMinutes = durationMinutes;
        }

        public static Time from(Record.Time time) {
            return new Time(time.getTimeId(), time.getDate(), time.getStartTime(), time.getEndTime(), time.getDurationMinutes());
        }
    }
}
