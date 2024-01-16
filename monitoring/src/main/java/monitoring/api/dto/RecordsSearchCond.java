package monitoring.api.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class RecordsSearchCond {

    private String action;
    private String memo;
    private LocalDate date;
    private LocalTime time;
    // start - end 검색은 report 에서 처리하는게 맞다고 생각한다.


    //public for ModelAttribute binding
    @Builder
    public RecordsSearchCond(String action, String memo, LocalDate date, LocalTime time) {
        this.action = action;
        this.memo = memo;
        this.date = date;
        this.time = time;
    }
}
