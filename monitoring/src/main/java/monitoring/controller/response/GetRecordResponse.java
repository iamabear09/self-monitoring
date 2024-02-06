package monitoring.controller.response;

import lombok.*;
import monitoring.domain.Record;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class GetRecordResponse {

    private Long recordId;
    private String action;
    private String memo;
    private Integer timeLogsNum;
    private List<GetTimeLogResponse> timeLogs;

    private GetRecordResponse(Long recordId, String action, String memo, Integer timeLogsNum, List<GetTimeLogResponse> timeLogs) {
        this.recordId = recordId;
        this.action = action;
        this.memo = memo;
        this.timeLogsNum = timeLogsNum;
        this.timeLogs = timeLogs;
    }

    public static GetRecordResponse from(Record record) {
        return new GetRecordResponse(
                record.getId(),
                record.getAction(),
                record.getMemo(),
                record.getTimeLogs().size(),
                record.getTimeLogs().stream()
                        .map(GetTimeLogResponse::from)
                        .toList()
        );
    }

}
