package monitoring.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import monitoring.domain.Record;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class UpdateRecordResult {

    private List<Record> deletedRecords;
    private List<Record> changedRecords;
    private Record updatedRecord;

}
