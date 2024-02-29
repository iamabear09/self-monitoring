package jhp.monitoring.api.controller.response;

import lombok.*;
import jhp.monitoring.domain.Record;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class SearchRecordResponse {
    private Integer recordsNum;
    private List<GetRecordResponse> records;

    public SearchRecordResponse(List<GetRecordResponse> records) {
        this.records = records;
        this.recordsNum = this.records.size();
    }

    public static SearchRecordResponse from(List<Record> records) {
        return new SearchRecordResponse(records.stream()
                .map(GetRecordResponse::from)
                .toList());
    }
}

