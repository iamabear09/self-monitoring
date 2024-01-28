package monitoring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "timeRecords") @ToString
@Getter
@Entity
public class Record {

    @Id
    @GeneratedValue
    @Column(name = "record_id")
    private Long id;

    private String action;
    private String memo;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    private List<Time> timeRecords = new ArrayList<>();

    @Builder
    public Record(Long id, String action, String memo, List<Time> times) {
        this.id = id;
        this.action = action;
        this.memo = memo;
    }

    public void addTime(Time time) {
        timeRecords.add(time);
        time.record = this;
    }

    public void deleteTime(Time time) {
        time.record = null;
        timeRecords.remove(time);
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode(exclude = "record") @ToString(exclude = "record")
    @Getter
    @Entity(name = "TIME")
    public static class Time {

        @Id
        @GeneratedValue
        @Column(name = "time_id")
        private Long id;

        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer durationMinutes;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "record_id")
        private Record record;

        @Builder
        public Time(Long id, LocalDate date, LocalTime startTime, Integer durationMinutes) {
            this.id = id;
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
            this.endTime = startTime.plusMinutes(durationMinutes);
        }

    }
}
