package monitoring.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@ToString
public class Record {

    private final Long recordId;
    private final String action;
    private final String memo;
    private final List<Time> timeRecords = new ArrayList<>();

    @Builder
    public Record(Long recordId, String action, String memo) {
        this.recordId = recordId;
        this.action = action;
        this.memo = memo;
    }

    public static Record from(Long id, Record recordWithoutId) {
        return Record.builder()
                .recordId(id)
                .action(recordWithoutId.getAction())
                .memo(recordWithoutId.getMemo())
                .build();
    }

    public List<Time> getTimeRecords() {
        return Collections.unmodifiableList(timeRecords);
    }

    @Getter
    @EqualsAndHashCode
    @ToString(exclude = "record")
    public static class Time {

        private final Long timeId;
        private final LocalDate date;
        private final LocalTime startTime;
        private final LocalTime endTime;
        private final Integer durationMinutes;

        private final Record record;

        @Builder
        public Time(Long timeId, LocalDate date, LocalTime startTime, Integer durationMinutes, Record record) {
            this.timeId = timeId;
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
            this.endTime = startTime.plusMinutes(durationMinutes);

            this.record = record;
            // 양방향 연관관계 설정
            Optional.ofNullable(record)
                    .ifPresent(r -> r.timeRecords.add(this));
        }


        public static Time from(Long id, Record record, Time timeWithoutIdAndRecord) {
            return Time.builder()
                    .timeId(id)
                    .date(timeWithoutIdAndRecord.getDate())
                    .startTime(timeWithoutIdAndRecord.getStartTime())
                    .durationMinutes(timeWithoutIdAndRecord.getDurationMinutes())
                    .record(record)
                    .build();
        }
    }
}
