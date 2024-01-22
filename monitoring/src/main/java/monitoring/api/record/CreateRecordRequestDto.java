package monitoring.api.record;

import lombok.*;
import monitoring.domain.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class CreateRecordRequestDto {

    private String action;
    private String memo;
    private List<Time> timeRecords;

    @Builder(access = AccessLevel.PACKAGE)
    public CreateRecordRequestDto(String action, String memo, List<Time> timeRecords) {
        this.action = action;
        this.memo = memo;
        this.timeRecords = timeRecords;
    }

    public Record toRecord() {
        Record record = new Record(null, action, memo);

        //새로운 Record 에 Time 연결
        timeRecords.forEach(t -> {
            new Record.Time(null, t.getDate(), t.getStartTime(), t.getDurationMinutes(), record);
        });

        return record;
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
