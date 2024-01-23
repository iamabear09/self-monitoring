package monitoring.api.record;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monitoring.domain.Record;
import monitoring.service.RecordsSearchCond;
import monitoring.service.RecordsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordsApiController {

    private final RecordsService recordsService;

    @PostMapping
    public RecordDto createRecord(@RequestBody CreateRecordRequestDto request) {

        Record savedRecord = recordsService.create(request.toRecord());
        return RecordDto.from(savedRecord);
    }


    @GetMapping("/{id}")
    public RecordDto getRecord(@PathVariable Long id) {

        return RecordDto.from(recordsService.get(id));
    }

    @GetMapping
    public SearchRecordsResponseDto getRecords(@ModelAttribute RecordsSearchCond cond) {

        Set<Record> records = recordsService.getList(cond);

        return new SearchRecordsResponseDto(records.stream()
                .map(RecordDto::from)
                .toList());
    }


    @PatchMapping("/{id}")
    public RecordDto updateRecordByPatch(@PathVariable Long id, @RequestBody PatchUpdateRecordRequestDto request) {

        Record record = recordsService.get(id);
        Record updateRecord = request.toUpdateRecordWith(record);

        return RecordDto.from(recordsService.updateByPatch(updateRecord));
    }


    @PutMapping("/{id}")
    public PutUpdateRecordResponseDto updateRecordByPatch(@PathVariable Long id, @RequestBody PutUpdateRecordRequestDto request) {

        //mock data
        Long timeId = 1L;
        List<RecordDto.Time> timeRecords = request.getTimeRecords().stream()
                .map(i -> new RecordDto.Time(timeId, i.getDate(), i.getStartTime(), i.getDurationMinutes()))
                .toList();

        RecordDto updatedRecords = new RecordDto(id, request.getAction(), request.getMemo(), timeRecords);


        //mock data
        RecordDto.Time time1 = new RecordDto.Time(5L, LocalDate.of(2024, 1, 1), LocalTime.of(10, 10), 30);
        RecordDto.Time time2 = new RecordDto.Time(6L, LocalDate.of(2024, 1, 1), LocalTime.of(11, 10), 60);

        RecordDto affectedRecord = new RecordDto(11L, "운동", "벤치프레스", List.of(time1, time2));
        List<Long> deleteRecordsIds = List.of(10L, 11L, 12L);

        return new PutUpdateRecordResponseDto(updatedRecords, deleteRecordsIds, List.of(affectedRecord));
    }


    @DeleteMapping("/{id}")
    public RecordDto deleteRecord(@PathVariable Long id) {

        //mock data
        String action = "운동";
        String meno = "헬스장";

        Long timeId = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Integer durationMinutes = 60;

        return new RecordDto(id, action, meno, List.of(new RecordDto.Time(timeId, date, startTime, durationMinutes)));
    }
}
