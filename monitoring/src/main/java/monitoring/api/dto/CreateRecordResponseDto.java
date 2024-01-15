package monitoring.api.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateRecordResponseDto {

    private Long recordId;

    @Builder
    private CreateRecordResponseDto(Long recordId) {
        this.recordId = recordId;
    }
}
