package monitoring.api.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode  //for test
@ToString   //for test
public class RecordDto {

    private Long recordId;
    private String action;
    private String memo;

    private Integer timeRecordsNum;
    private List<Time> timeRecords;

    @Builder
    private RecordDto(Long recordId, String action, String memo, List<Time> timeRecords) {
        this.recordId = recordId;
        this.action = action;
        this.memo = memo;
        this.timeRecords = timeRecords;
        if (timeRecords != null) {
            this.timeRecordsNum = timeRecords.size();
        }
    }


    // To save time records
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode  //for test
    @ToString   //for test
    public static class Time {
        private Long timeId;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private Long durationMinutes;

        @Builder
        private Time(Long timeId, LocalDate date, LocalTime startTime, Long durationMinutes) {
            this.timeId = timeId;
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = Optional.ofNullable(durationMinutes).orElse(0L);
            if (startTime != null) {
                this.endTime = startTime.plusMinutes(this.durationMinutes);
            }
        }
    }
}
