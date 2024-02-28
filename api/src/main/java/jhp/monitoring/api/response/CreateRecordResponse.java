package jhp.monitoring.api.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateRecordResponse {

    private String recordId;

    public CreateRecordResponse(String recordId) {
        this.recordId = recordId;
    }
}
