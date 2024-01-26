package monitoring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@EqualsAndHashCode(exclude = "record") @ToString(exclude = "record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Time {

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
    public Time(Long id, LocalDate date, LocalTime startTime, LocalTime endTime, Integer durationMinutes, Record record) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.record = record;
    }
}