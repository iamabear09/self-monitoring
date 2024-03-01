package jhp.monitoring.consumer.kafka;

import jhp.monitoring.common.KafkaTopicNames;
import jhp.monitoring.consumer.service.RecordTimeService;
import jhp.monitoring.consumer.service.response.UpdateRecordResult;
import jhp.monitoring.domain.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventListener {
    private static final String RECORD_CONSUMER_GROUP = "record";

    private final RecordTimeService recordTimeService;

    @KafkaListener(topics = KafkaTopicNames.TOPIC_CREATE_RECORD, groupId = RECORD_CONSUMER_GROUP)
    public void createRecord(Record recordData) {
        Record createdRecord = recordTimeService.create(recordData);
        log.debug("saved Record = {}", createdRecord);
    }

    @KafkaListener(topics = KafkaTopicNames.TOPIC_PATCH_UPDATE_RECORD, groupId = RECORD_CONSUMER_GROUP)
    public void updateRecord(Record recordData) {
        Record updatedRecord = recordTimeService.update(recordData.getId(), recordData);
        log.debug("patch updated Result = {}", updatedRecord);
    }

    @KafkaListener(topics = KafkaTopicNames.TOPIC_PUT_UPDATE_RECORD, groupId = RECORD_CONSUMER_GROUP)
    public void updateRecordByRemovingDuplicatedTimeLogs(Record recordData) {
        UpdateRecordResult updateRecordResult = recordTimeService.updateWithRemovingDuplicatedTimeLogs(recordData.getId(), recordData);
        log.debug("put updated Result = {}", updateRecordResult);
    }

    @KafkaListener(topics = KafkaTopicNames.TOPIC_DELETE_RECORD, groupId = RECORD_CONSUMER_GROUP)
    public void deleteRecord(Record recordData) {
        Record deleteRecord = recordTimeService.delete(recordData.getId());
        log.debug("delete Record Result = {}", deleteRecord);
    }
}
