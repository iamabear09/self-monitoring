package monitoring.controller.request;

import lombok.*;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatchUpdateRecordRequest {

    private String action;
    private String memo;
    private List<PatchUpdateTimeLogRequest> timeLogRequests;

    @Builder
    public PatchUpdateRecordRequest(String action, String memo, List<PatchUpdateTimeLogRequest> timeLogRequests) {
        this.action = action;
        this.memo = memo;
        this.timeLogRequests = timeLogRequests;

        /*
        * 생성자를 호출해서 PatchUpdateRecordRequest 를 생성하지 않는다.
        * Reflection 을 사용하는 것으로 알고 있다.
        * 따라서, 아래 코드는 동작하지 않는다.
        * */
        // this.timeLogRequests = Optional.ofNullable(timeLogRequests).orElse(new ArrayList<>());

    }

    public Record toRecord() {
        Record record = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        List<TimeLog> timeLogs = Optional
                .ofNullable(timeLogRequests)
                .orElse(new ArrayList<>())
                .stream()
                .map(t -> t.toTimeLog())
                .toList();

        record.setTimeLogs(timeLogs);

        return record;
    }
}

