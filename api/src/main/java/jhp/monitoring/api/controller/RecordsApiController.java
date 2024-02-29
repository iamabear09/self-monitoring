package jhp.monitoring.api.controller;

import com.github.f4b6a3.ulid.UlidCreator;
import jhp.monitoring.api.controller.request.CreateRecordRequest;
import jhp.monitoring.api.controller.request.PatchUpdateRecordRequest;
import jhp.monitoring.api.controller.request.PutUpdateRecordRequest;
import jhp.monitoring.api.controller.response.RecordIdResponse;
import jhp.monitoring.api.controller.response.GetRecordResponse;
import jhp.monitoring.api.controller.response.SearchRecordResponse;
import jhp.monitoring.api.service.RecordTimeReadService;
import jhp.monitoring.api.service.request.RecordSearchCond;
import jhp.monitoring.common.KafkaTopicNames;
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
    public RecordIdResponse createRecord(@RequestBody CreateRecordRequest request) {
        String recordId = UlidCreator.getUlid().toLowerCase();

        kafkaTemplate.send(KafkaTopicNames.TOPIC_CREATE_RECORD, recordId, request.toRecordWithId(recordId))
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Created Record Data: {}", result.getProducerRecord().value());
                    } else {
                        log.error("Fail Creating Record: {} \n error: {}", result.getProducerRecord().value(), ex.getMessage());
                        // error 를 던져주고, 에러 응답이 나가도록 설정해야 한다.
                    }});

        return new RecordIdResponse(recordId);
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
    public RecordIdResponse updateRecordByPatch(@PathVariable String id, @RequestBody PatchUpdateRecordRequest request) {

        recordTimeReadReadService.validateRecordId(id);

        Record updateRecordData = request.toRecord();
        updateRecordData.setId(id);

        kafkaTemplate.send(KafkaTopicNames.TOPIC_PATCH_UPDATE_RECORD, id, updateRecordData)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Update Record Data: {}", result.getProducerRecord().value());
                    } else {
                        log.error("Fail Update Record: {} \n error: {}", result.getProducerRecord().value(), ex.getMessage());
                        // error 를 던져주고, 에러 응답이 나가도록 설정해야 한다.
                    }});

        //update 확인은 어떻게 처리해야 하지?
        //우선 DB를 통해 확인해야 하므로, DB의 field 에 무언가 추가가 되어야 한다.
        return new RecordIdResponse(id);
    }


    @PutMapping("/{id}")
    public RecordIdResponse updateRecordByPut(@PathVariable String id, @RequestBody PutUpdateRecordRequest request) {

        recordTimeReadReadService.validateRecordId(id);

        Record updateRecordData = request.toRecord();
        updateRecordData.setId(id);

        kafkaTemplate.send(KafkaTopicNames.TOPIC_PUT_UPDATE_RECORD, id, updateRecordData)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Update Record Data: {}", result.getProducerRecord().value());
                    } else {
                        log.error("Fail Update Record: {} \n error: {}", result.getProducerRecord().value(), ex.getMessage());
                        // error 를 던져주고, 에러 응답이 나가도록 설정해야 한다.
                    }});
        return new RecordIdResponse(id);
    }
}
