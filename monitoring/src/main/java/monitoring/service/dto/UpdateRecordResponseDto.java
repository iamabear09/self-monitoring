package monitoring.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import monitoring.domain.Record;
import monitoring.domain.TimeLog;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class UpdateRecordResponseDto {

    private List<Record> deletedRecords;
    private List<Record> changedRecords;
    private Record updatedRecord;

    @Getter
    @AllArgsConstructor
    public static class TimeLogHistory {
        Long recordId;
        TimeLog deletedTimeLog;
        List<TimeLog> createdTimeLogs;
    }
}
