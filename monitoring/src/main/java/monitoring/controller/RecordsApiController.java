package monitoring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.controller.request.CreateRecordRequest;
import monitoring.controller.request.PatchUpdateRecordRequest;
import monitoring.controller.request.PutUpdateRecordRequest;
import monitoring.controller.response.CreateRecordResponse;
import monitoring.controller.response.GetRecordResponse;
import monitoring.controller.response.PutUpdateRecordResponse;
import monitoring.controller.response.SearchRecordResponse;
import monitoring.domain.Record;
import monitoring.service.RecordTimeService;
import monitoring.service.request.RecordSearchCond;
import monitoring.service.response.UpdateRecordResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/records")
public class RecordsApiController {

    private final RecordTimeService recordTimeService;

    @PostMapping
    public CreateRecordResponse createRecord(@RequestBody CreateRecordRequest request) {
        Record createdRecord = recordTimeService.create(request.toRecord());
        return CreateRecordResponse.from(createdRecord);
    }

    @GetMapping("/{id}")
    public GetRecordResponse getRecord(@PathVariable Long id) {
        Record record = recordTimeService.getRecordWithTimeLogs(id);
        return GetRecordResponse.from(record);
    }

    @GetMapping
    public SearchRecordResponse getRecords(@ModelAttribute RecordSearchCond cond) {
        List<Record> searchedRecords = recordTimeService.getRecords(cond);
        return SearchRecordResponse.from(searchedRecords);
    }


    @PatchMapping("/{id}")
    public GetRecordResponse updateRecordByPatch(@PathVariable Long id, @RequestBody PatchUpdateRecordRequest request) {
        Record updateRecord = recordTimeService.update(id, request.toRecord());
        return GetRecordResponse.from(updateRecord);
    }

    @PutMapping("/{id}")
    public PutUpdateRecordResponse updateRecordByPut(@PathVariable Long id, @RequestBody PutUpdateRecordRequest request) {
        UpdateRecordResult updateRecordResult = recordTimeService.updateWithSideEffect(id, request.toRecord());
        return PutUpdateRecordResponse.from(updateRecordResult);
    }

    @DeleteMapping("/{id}")
    public GetRecordResponse deleteRecord(@PathVariable Long id) {
        Record record = recordTimeService.delete(id);
        return GetRecordResponse.from(record);
    }

}
