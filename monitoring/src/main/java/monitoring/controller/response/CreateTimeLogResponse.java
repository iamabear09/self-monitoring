package monitoring.controller.response;

import lombok.*;
import monitoring.domain.TimeLog;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class CreateTimeLogResponse {

    private Long timeId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;

    private CreateTimeLogResponse(Long timeId, LocalDate date, LocalTime startTime, LocalTime endTime, Integer durationMinutes) {
        this.timeId = timeId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
    }

    public static CreateTimeLogResponse from(TimeLog timeLog) {
        return new CreateTimeLogResponse(timeLog.getId(), timeLog.getDate(), timeLog.getStartTime(), timeLog.getEndTime(), (int) timeLog.getDurationMinutes().toMinutes());
    }
}
