package jhp.monitoring.api.controller.request;

import lombok.*;


import java.time.LocalDate;
import java.time.LocalTime;

import jhp.monitoring.domain.TimeLog;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateTimeLogRequest {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Builder
    public CreateTimeLogRequest(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeLog toTimeLog() {
        return TimeLog.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
