package jhp.monitoring.api.request;

import lombok.*;
import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.domain.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class CreateRecordRequest {

    private String action;
    private String memo;
    private List<CreateTimeLogRequest> timeLogRequests;

    @Builder
    public CreateRecordRequest(String action, String memo, List<CreateTimeLogRequest> timeLogRequests) {
        this.action = action;
        this.memo = memo;
        this.timeLogRequests = Optional.ofNullable(timeLogRequests)
                .orElse(new ArrayList<>());
    }

    public Record toRecordWithId(String id) {
        Record record = Record.builder()
                .id(id)
                .action(action)
                .memo(memo)
                .build();

        List<TimeLog> timeLogs = timeLogRequests
                .stream()
                .map(t -> {
                    TimeLog timeLog = t.toTimeLog();
                    timeLog.setRecord(record);

                    return timeLog;
                })
                .toList();

        record.setTimeLogs(timeLogs);

        return record;
    }
}
