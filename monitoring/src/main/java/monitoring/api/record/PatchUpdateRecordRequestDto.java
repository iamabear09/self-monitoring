package monitoring.api.record;

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

    @Builder(access = AccessLevel.PACKAGE)
    public PatchUpdateRecordRequestDto(String action, String memo, List<Time> timeRecords) {
        this.action = action;
        this.memo = memo;
        this.timeRecords = timeRecords;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Time {

        private LocalDate date;
        private LocalTime startTime;
        private Integer durationMinutes;

        @Builder(access = AccessLevel.PACKAGE)
        public Time(LocalDate date, LocalTime startTime, Integer durationMinutes) {
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
        }
    }
}

