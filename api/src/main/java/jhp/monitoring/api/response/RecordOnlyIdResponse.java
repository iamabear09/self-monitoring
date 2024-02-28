package jhp.monitoring.api.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordOnlyIdResponse {

    private String recordId;

    public RecordOnlyIdResponse(String recordId) {
        this.recordId = recordId;
    }
}
