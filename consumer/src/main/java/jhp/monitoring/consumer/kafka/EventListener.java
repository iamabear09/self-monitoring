package jhp.monitoring.consumer.kafka;

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

    private final RecordTimeService recordTimeService;

    @KafkaListener(topics = "create-record", groupId = "record")
    public void createRecord(Record record) {
        log.info("호출은 되는 거야?");
        log.info("record = {}", record);
        Record saveRecord = recordTimeService.create(record);
        log.info("saved Record Id = {}",saveRecord.getId());
    }
}
