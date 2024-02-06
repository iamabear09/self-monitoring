package monitoring.controller.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PutUpdateRecordRequest {

    private String action;
    private String memo;
    private List<PutUpdateTimeLogRequest> timeLogRequests;

    @Builder
    public PutUpdateRecordRequest(String action, String memo, List<PutUpdateTimeLogRequest> timeLogRequests) {
        this.action = action;
        this.memo = memo;
        this.timeLogRequests = Optional.ofNullable(timeLogRequests)
                .orElse(new ArrayList<>());
    }

    public Record toRecord() {
        Record record = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        List<TimeLog> timeLogs = timeLogRequests.stream()
                .map(t -> t.toTimeLog())
                .toList();

        record.setTimeLogs(timeLogs);

        return record;
    }
}

