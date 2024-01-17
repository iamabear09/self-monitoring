package monitoring.api.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class PutUpdateRecordResponseDto {

    private RecordDto updatedRecord;

    private Integer deletedRecordsNum;
    private List<Long> deleteRecordsIds;
    private Integer affectedRecordsNum;
    private List<RecordDto> affectedRecords;


    public PutUpdateRecordResponseDto(RecordDto updatedRecord, List<Long> deleteRecordsIds, List<RecordDto> affectedRecords) {
        this.updatedRecord = updatedRecord;
        this.deleteRecordsIds = Optional
                .ofNullable(deleteRecordsIds)
                .orElse(new ArrayList<>());
        this.deletedRecordsNum = this.deleteRecordsIds.size();

        this.affectedRecords = Optional.ofNullable(affectedRecords)
                .orElse(new ArrayList<>());
        this.affectedRecordsNum = this.affectedRecords.size();
    }
}


