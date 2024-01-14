package monitoring.api;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateRecordsResponseDto {

    private Long recordId;

    @Builder
    private CreateRecordsResponseDto(Long recordId) {
        this.recordId = recordId;
    }
}
