package monitoring.api;

import lombok.extern.slf4j.Slf4j;
import monitoring.api.dto.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/records")
public class RecordsApiController {

    @PostMapping
    public RecordDto createRecord(@RequestBody CreateRecordRequestDto request) {

        Long timeId = 1L;
        List<RecordDto.Time> timeRecords = request.getTimeRecords().stream()
                .map(r -> new RecordDto.Time(timeId, r.getDate(), r.getStartTime(), r.getDurationMinutes()))
                .toList();

        return new RecordDto(1L, request.getAction(), request.getMemo(), timeRecords);
    }

    @GetMapping("/{id}")
    public RecordDto getRecord(@PathVariable Long id) {

        //mock data
        String action = "운동";
        String meno = "헬스장";

        Long timeId = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Integer durationMinutes = 60;

        return new RecordDto(id, action, meno, List.of(new RecordDto.Time(timeId, date, startTime, durationMinutes)));
    }

    @GetMapping
    public SearchRecordsResponseDto getRecords(@ModelAttribute RecordsSearchCond cond) {
        log.info(">>> cond = {}", cond);

        //mock data
        Long recordId1 = 1L;
        String action1 = "운동";
        String meno1 = "헬스장";

        Long timeId1 = 1L;
        LocalDate date1 = LocalDate.of(2024, 1, 13);
        LocalTime startTime1 = LocalTime.of(13, 10);
        Integer durationMinutes1 = 60;

        List<RecordDto.Time> timeRecords1 = List.of(new RecordDto.Time(timeId1, date1, startTime1, durationMinutes1));
        RecordDto recordDto1 = new RecordDto(recordId1, action1, meno1, timeRecords1);

        //mock data
        Long recordId2 = 2L;
        String action2 = "공부";
        String meno2 = "API 설계";

        Long timeId2 = 2L;
        LocalDate date2 = LocalDate.of(2024, 1, 11);
        LocalTime startTime2 = LocalTime.of(13, 10);
        Integer durationMinutes2 = 60;

        List<RecordDto.Time> timeRecords2 = List.of(new RecordDto.Time(timeId2, date2, startTime2, durationMinutes2));
        RecordDto recordDto2 = new RecordDto(recordId2, action2, meno2, timeRecords2);

        return new SearchRecordsResponseDto(List.of(recordDto1, recordDto2));
    }


    @PatchMapping("/{id}")
    public RecordDto updateRecordByPatch(@PathVariable Long id, @RequestBody PatchUpdateRecordRequestDto request) {

        Long timeId = 1L;
        List<RecordDto.Time> timeRecords = null;
        if (request.getTimeRecords() != null) {
            timeRecords = request.getTimeRecords().stream()
                    .map(i -> new RecordDto.Time(timeId, i.getDate(),i.getStartTime(),i.getDurationMinutes()))
                    .toList();
        }
        return new RecordDto(id, request.getAction(), request.getMemo(), timeRecords);
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
