package monitoring.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatchUpdateRecordRequestDto {

    private LocalDate date;
    private LocalTime startTime;
    private Long durationMinutes;
    private String action;
    private String memo;

    @Builder
    private PatchUpdateRecordRequestDto(LocalDate date, LocalTime startTime, Long durationMinutes, String action, String memo) {
        this.date = date;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.action = action;
        this.memo = memo;
    }
}
