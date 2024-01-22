package monitoring.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter
@EqualsAndHashCode
@ToString
public class Record {

    private Long recordId;
    private String action;
    private String memo;
    private final Set<Time> timeRecords = new HashSet<>();

    @Builder
    public Record(Long recordId, String action, String memo) {
        this.recordId = recordId;
        this.action = action;
        this.memo = memo;
    }

    public void addTimeRecord(Record.Time time) {
        if (time.record != null) {
            time.record.delete(time);
        }

        //양방향 연관관계 설정
        timeRecords.add(time);
        time.record = this;
    }

    /**
     * If timeRecord has Record, before add timeRecord,
     * it first disconnect a link between timeRecord and Record.
     * And then timeRecord is linked to new Record.
     */
    public void addTimeRecords(Set<Record.Time> timeRecords) {
        //잘못 장석하면 Concurrent Modification Error
        Set<Time> forConcurrentModification = Set.copyOf(timeRecords);
        forConcurrentModification.forEach(this::addTimeRecord);
    }

    public Set<Record.Time> getTimeRecords() {
        return Collections.unmodifiableSet(timeRecords);
    }

    public void delete(Record.Time time) {
        timeRecords.remove(time);
    }

    public void updateContents(String action, String memo) {
        this.action = action;
        this.memo = memo;
    }

    @Getter
    @EqualsAndHashCode(exclude = "record")
    @ToString(exclude = "record")
    public static class Time {

        private Long timeId;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer durationMinutes;

        private Record record;

        @Builder
        public Time(Long timeId, LocalDate date, LocalTime startTime, Integer durationMinutes) {
            this.timeId = timeId;
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
            this.endTime = startTime.plusMinutes(durationMinutes);
        }
    }
}
