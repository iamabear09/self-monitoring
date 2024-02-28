package jhp.monitoring.api.response;

import lombok.*;
import jhp.monitoring.domain.TimeLog;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class GetTimeLogResponse {

    private Long timeId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;

    private GetTimeLogResponse(Long timeId, LocalDate date, LocalTime startTime, LocalTime endTime, Integer durationMinutes) {
        this.timeId = timeId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
    }

    public static GetTimeLogResponse from(TimeLog timeLog) {
        return new GetTimeLogResponse(timeLog.getId(), timeLog.getDate(), timeLog.getStartTime(), timeLog.getEndTime(), (int) timeLog.getDurationMinutes().toMinutes());
    }
}
