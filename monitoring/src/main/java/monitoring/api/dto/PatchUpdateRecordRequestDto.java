package monitoring.api.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatchUpdateRecordRequestDto {

    private String action;
    private String memo;
    private List<Time> timeRecords;

    @Builder
    private PatchUpdateRecordRequestDto(String action, String memo, List<Time> timeRecords) {
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
        private Time(LocalDate date, LocalTime startTime, Long durationMinutes) {
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
        }
    }
}

