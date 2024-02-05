package monitoring.controller.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monitoring.domain.TimeLog;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatchUpdateTimeLogRequest {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Builder
    public PatchUpdateTimeLogRequest(LocalDate date, LocalTime startTime, LocalTime endTime) {
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

