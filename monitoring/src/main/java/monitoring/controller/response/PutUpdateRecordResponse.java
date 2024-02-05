package monitoring.controller.response;

import lombok.*;
import monitoring.service.response.UpdateRecordResult;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class PutUpdateRecordResponse {

    private GetRecordResponse updatedRecord;

    private Integer deletedRecordsNum;
    private List<GetRecordResponse> deleteRecords;
    private Integer changedRecordsNum;
    private List<GetRecordResponse> changedRecords;

    private PutUpdateRecordResponse(GetRecordResponse updatedRecord, Integer deletedRecordsNum, List<GetRecordResponse> deleteRecords, Integer changedRecordsNum, List<GetRecordResponse> changedRecords) {
        this.updatedRecord = updatedRecord;
        this.deletedRecordsNum = deletedRecordsNum;
        this.deleteRecords = deleteRecords;
        this.changedRecordsNum = changedRecordsNum;
        this.changedRecords = changedRecords;
    }

    public static PutUpdateRecordResponse from(UpdateRecordResult updateRecordResult) {
        List<GetRecordResponse> deletedRecordResponse = updateRecordResult.getDeletedRecords()
                .stream()
                .map(GetRecordResponse::from)
                .toList();

        List<GetRecordResponse> changedRecordResponse = updateRecordResult.getChangedRecords()
                .stream()
                .map(GetRecordResponse::from)
                .toList();

        GetRecordResponse updatedRecord = GetRecordResponse.from(updateRecordResult.getUpdatedRecord());
        return new PutUpdateRecordResponse(updatedRecord, deletedRecordResponse.size(), deletedRecordResponse, changedRecordResponse.size(), changedRecordResponse);
    }
}

