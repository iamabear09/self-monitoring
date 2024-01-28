package monitoring.repository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TimeSearchCond {

    private String action;

    private LocalDate date;
    private LocalTime time;

    @Builder
    public TimeSearchCond(String action, LocalDate date, LocalTime time) {
        this.action = action;
        this.date = date;
        this.time = time;
    }
}
