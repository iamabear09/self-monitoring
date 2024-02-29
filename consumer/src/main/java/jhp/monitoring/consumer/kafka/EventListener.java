package jhp.monitoring.consumer.kafka;

import jhp.monitoring.common.KafkaTopicNames;
import jhp.monitoring.consumer.service.RecordTimeService;
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
    public void createRecord(Record record) {
        Record saveRecord = recordTimeService.create(record);
        log.debug("saved Record Id = {}",saveRecord.getId());
    }

    @KafkaListener(topics = KafkaTopicNames.TOPIC_PATCH_UPDATE_RECORD, groupId = RECORD_CONSUMER_GROUP)
    public void updateRecord(Record recordData) {
        recordTimeService.update(recordData.getId(), recordData);
        log.debug("updated Record Id = {}", recordData.getId());
    }

    @KafkaListener(topics = KafkaTopicNames.TOPIC_PUT_UPDATE_RECORD, groupId = RECORD_CONSUMER_GROUP)
    public void updateRecordByRemovingDuplicatedTimeLogs(Record recordData) {
        recordTimeService.updateWithRemovingDuplicatedTimeLogs(recordData.getId(), recordData);
        log.debug("updated Record Id = {}", recordData.getId());
    }
}
