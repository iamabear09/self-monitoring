package jhp.monitoring.consumer.service.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import jhp.monitoring.domain.Record;

@Getter
@Setter
@ToString
public class UpdateRecordResult {

    private final Set<String> deletedRecordIdSet = new HashSet<>();
    private final Set<String> changedRecordIdSet = new HashSet<>();
    private Record updatedRecord;

    public void removeDuplicatedIds() {
        changedRecordIdSet.removeAll(deletedRecordIdSet);
    }

}
