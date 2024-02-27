package jhp.monitoring.api;

import com.github.f4b6a3.ulid.UlidCreator;
import jhp.monitoring.api.request.CreateRecordRequest;
import jhp.monitoring.api.response.CreateRecordResponse;
import jhp.monitoring.domain.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/records")
public class RecordsApiController {

    private final KafkaTemplate<String, Record> kafkaTemplate;

    @PostMapping
    public CreateRecordResponse createRecord(@RequestBody CreateRecordRequest request) {
        String recordId = UlidCreator.getUlid().toLowerCase();

        kafkaTemplate.send("spring-topic", "testKey", request.toRecordWithId(recordId))
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Created Record Data: {}", result.getProducerRecord().value());
                    } else {
                        log.error("Fail Creating Record: {} \n error: {}", result.getProducerRecord().value(), ex.getMessage());
                    }});

        return new CreateRecordResponse(recordId);
    }
}
