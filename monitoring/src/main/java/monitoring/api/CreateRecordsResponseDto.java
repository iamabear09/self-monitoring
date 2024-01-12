package monitoring.api;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateRecordsResponseDto {

    private Long id;

    @Builder
    public CreateRecordsResponseDto(Long id) {
        this.id = id;
    }
}
