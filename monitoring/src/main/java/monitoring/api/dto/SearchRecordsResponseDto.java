package monitoring.api.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode  //for test
@ToString   //for test
public class SearchRecordsResponseDto {
    private Integer recordsNum;
    private List<RecordDto> records;

    @Builder
    private SearchRecordsResponseDto(List<RecordDto> records) {
        this.records = Optional
                .ofNullable(records)
                .orElse(new ArrayList<>());

        this.recordsNum = this.records.size();
    }
}

