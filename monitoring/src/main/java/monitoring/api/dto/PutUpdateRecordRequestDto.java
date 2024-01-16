package monitoring.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PutUpdateRecordRequestDto {

    private String action;
    private String memo;
    private List<Time> timeRecords;

    @Builder
    public PutUpdateRecordRequestDto(String action, String memo, List<Time> timeRecords) {
        this.action = action;
        this.memo = memo;
        this.timeRecords = timeRecords;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Time {

        private LocalDate date;
        private LocalTime startTime;
        private Long durationMinutes;

        @Builder
        public Time(LocalDate date, LocalTime startTime, Long durationMinutes) {
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
        }
    }
}

