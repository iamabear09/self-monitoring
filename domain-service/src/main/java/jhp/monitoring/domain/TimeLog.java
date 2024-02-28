package jhp.monitoring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "record") @ToString(exclude = "record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TimeLog {

    @Id
    @GeneratedValue
    @Column(name = "timelog_id")
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration durationMinutes;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @Builder
    public TimeLog(Long id, LocalDate date, LocalTime startTime, LocalTime endTime, Duration durationMinutes, Record record) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.record = record;
    }
}
