package monitoring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter @EqualsAndHashCode @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Record {

    @Id @GeneratedValue
    @Column(name = "record_id")
    private Long id;

    private String action;
    private String memo;

    @OneToMany(mappedBy = "record")
    private List<Time> timeRecords = new ArrayList<>();

    public Record(Long id, String action, String memo) {
        this.id = id;
        this.action = action;
        this.memo = memo;
    }

    public void addTimes(List<Time> times) {
        timeRecords.addAll(times);
        times.forEach(t -> {
            t.record = this;
        });
    }



    @Getter @EqualsAndHashCode(exclude = "record") @ToString(exclude = "record")
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Entity(name = "TIME")
    public static class Time {

        @Id @GeneratedValue
        @Column(name = "time_id")
        private Long id;

        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer durationMinutes;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "record_id")
        private Record record;

        public Time(Long id, LocalDate date, LocalTime startTime, Integer durationMinutes, Record record) {
            this.id = id;
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
            this.endTime = startTime.plusMinutes(durationMinutes);

            //양방향 연관관계 설정
            if (record != null) {
                this.record = record;
                record.timeRecords.add(this);
            }
        }
    }

}

