package jhp.monitoring.api;

import com.github.f4b6a3.ulid.UlidCreator;
import jhp.monitoring.api.config.kafka.KafkaTopicConfig;
import jhp.monitoring.api.request.CreateRecordRequest;
import jhp.monitoring.api.request.PatchUpdateRecordRequest;
import jhp.monitoring.api.response.RecordOnlyIdResponse;
import jhp.monitoring.api.response.GetRecordResponse;
import jhp.monitoring.api.response.SearchRecordResponse;
import jhp.monitoring.api.service.RecordTimeReadService;
import jhp.monitoring.api.service.request.RecordSearchCond;
import jhp.monitoring.domain.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/records")
public class RecordsApiController {

    private final KafkaTemplate<String, Record> kafkaTemplate;
    private final RecordTimeReadService recordTimeReadReadService;

    @PostMapping
    public RecordOnlyIdResponse createRecord(@RequestBody CreateRecordRequest request) {
        String recordId = UlidCreator.getUlid().toLowerCase();

        kafkaTemplate.send(KafkaTopicConfig.TOPIC_CREATE_RECORD, recordId, request.toRecordWithId(recordId))
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Created Record Data: {}", result.getProducerRecord().value());
                    } else {
                        log.error("Fail Creating Record: {} \n error: {}", result.getProducerRecord().value(), ex.getMessage());
                    }});

        return new RecordOnlyIdResponse(recordId);
    }

    @GetMapping("/{id}")
    public GetRecordResponse getRecord(@PathVariable String id) {
        Record record = recordTimeReadReadService.getRecordWithTimeLogs(id);
        return GetRecordResponse.from(record);
    }

    @GetMapping
    public SearchRecordResponse getRecords(@ModelAttribute RecordSearchCond cond) {
        List<Record> searchedRecords = recordTimeReadReadService.getRecords(cond);
        return SearchRecordResponse.from(searchedRecords);
    }

    @PatchMapping("/{id}")
    public RecordOnlyIdResponse updateRecordByPatch(@PathVariable String id, @RequestBody PatchUpdateRecordRequest request) {

        Record updateRecordData = request.toRecord();
        updateRecordData.setId(id);

        kafkaTemplate.send(KafkaTopicConfig.TOPIC_PATCH_UPDATE_RECORD, id, updateRecordData)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Created Record Data: {}", result.getProducerRecord().value());
                    } else {
                        log.error("Fail Creating Record: {} \n error: {}", result.getProducerRecord().value(), ex.getMessage());
                    }});

        return new RecordOnlyIdResponse(id);
    }

}
