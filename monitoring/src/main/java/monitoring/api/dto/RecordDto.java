package monitoring.api.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@EqualsAndHashCode //for test
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordDto {

    private Long recordId;
    private LocalDate date;
    private LocalTime startTime;
    private Long durationMinutes;
    private String action;
    private String memo;

    @Builder
    private RecordDto(Long recordId, LocalDate date, LocalTime startTime, Long durationMinutes, String action, String memo) {
        this.recordId = recordId;
        this.date = date;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.action = action;
        this.memo = memo;
    }
}
