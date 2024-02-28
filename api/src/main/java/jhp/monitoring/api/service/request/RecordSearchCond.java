package jhp.monitoring.api.service.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class RecordSearchCond {

    private String action;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;


    //public for ModelAttribute binding
    @Builder
    public RecordSearchCond(String action, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.action = Optional.ofNullable(action)
                .orElse("");
        this.date = Optional.ofNullable(date)
                .orElse(LocalDate.now());
        this.startTime = Optional.ofNullable(startTime)
                .orElse(LocalTime.of(0, 0));
        this.endTime = Optional.ofNullable(endTime)
                .orElse(LocalTime.of(23,59));
    }
}
