package monitoring.controller.response;

import lombok.*;
import monitoring.domain.Record;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class CreateRecordResponse {

    private Long recordId;
    private String action;
    private String memo;
    private Integer timeLogsNum;
    private List<CreateTimeLogResponse> timeLogs;

    private CreateRecordResponse(Long recordId, String action, String memo, Integer timeLogsNum, List<CreateTimeLogResponse> timeLogs) {
        this.recordId = recordId;
        this.action = action;
        this.memo = memo;
        this.timeLogsNum = timeLogsNum;
        this.timeLogs = timeLogs;
    }

    public static CreateRecordResponse from(Record record) {
        return new CreateRecordResponse(
                record.getId(),
                record.getAction(),
                record.getMemo(),
                record.getTimeLogs().size(),
                record.getTimeLogs().stream()
                        .map(CreateTimeLogResponse::from)
                        .toList()
        );
    }
}
