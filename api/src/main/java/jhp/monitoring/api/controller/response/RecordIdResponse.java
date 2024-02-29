package jhp.monitoring.api.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordIdResponse {

    private String recordId;

    public RecordIdResponse(String recordId) {
        this.recordId = recordId;
    }
}
