package monitoring.api;

import lombok.extern.slf4j.Slf4j;
import monitoring.api.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/records")
public class RecordsApiController {

    @PostMapping
    public CreateRecordResponseDto createRecord(@RequestBody CreateRecordRequestDto request) {
        log.info(">>> createRecords 호출");

        log.info(">>> createRecords 종료");
        return CreateRecordResponseDto.builder()
                .recordId(1L)
                .build();
    }

    @GetMapping("/{id}")
    public RecordDto getRecord(@PathVariable Long id) {
        log.info(">>> getRecord 호출");

        //mock data
        Long recordId = 1L;
        String action = "운동";
        String meno = "헬스장";

        Long timeId = 1L;
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Long durationMinutes = 60L;

        List<RecordDto.Time> timeRecords = List.of(RecordDto.Time.builder()
                .timeId(timeId)
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build());

        log.info(">>> getRecord 종료");
        return RecordDto.builder()
                .recordId(recordId)
                .action(action)
                .memo(meno)
                .timeRecords(timeRecords)
                .build();
    }

    @GetMapping
    public SearchRecordsResponseDto getRecords(@ModelAttribute RecordsSearchCond cond) {
        log.info(">>> getRecords 호출");
        log.info(">>> cond = {}", cond);

        //mock data
        Long recordId1 = 1L;
        String action1 = "운동";
        String meno1 = "헬스장";

        Long timeId1 = 1L;
        LocalDate date1 = LocalDate.of(2024, 1, 13);
        LocalTime startTime1 = LocalTime.of(13, 10);
        Long durationMinutes1 = 60L;

        List<RecordDto.Time> timeRecords1 = List.of(RecordDto.Time.builder()
                .timeId(timeId1)
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build());

        RecordDto recordDto1 = RecordDto.builder()
                .recordId(recordId1)
                .action(action1)
                .memo(meno1)
                .timeRecords(timeRecords1)
                .build();

        //mock data
        Long recordId2 = 2L;
        String action2 = "공부";
        String meno2 = "API 설계";

        Long timeId2 = 2L;
        LocalDate date2 = LocalDate.of(2024, 1, 11);
        LocalTime startTime2 = LocalTime.of(13, 10);
        Long durationMinutes2 = 60L;

        List<RecordDto.Time> timeRecords2 = List.of(RecordDto.Time.builder()
                .timeId(timeId2)
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build());

        RecordDto recordDto2 = RecordDto.builder()
                .recordId(recordId2)
                .action(action2)
                .memo(meno2)
                .timeRecords(timeRecords2)
                .build();

        log.info(">>> getRecord 종료");

        return SearchRecordsResponseDto.builder()
                .records(List.of(recordDto1, recordDto2))
                .build();
    }


    @PatchMapping("/{id}")
    public RecordDto updateRecordByPatch(@PathVariable Long id,
                                                            @RequestBody PatchUpdateRecordRequestDto request) {
        log.info("updateRecordByPatch 호출");
        Long timeId = 1L;

        List<RecordDto.Time> timeRecords = null;
        if (request.getTimeRecords() != null) {
            timeRecords = request.getTimeRecords().stream()
                    .map(time -> RecordDto.Time.builder()
                            .timeId(timeId)
                            .date(time.getDate())
                            .startTime(time.getStartTime())
                            .durationMinutes(time.getDurationMinutes())
                            .build())
                    .toList();
        }

        log.info("updateRecordByPatch 종료");
        return RecordDto.builder()
                .recordId(id)
                .action(request.getAction())
                .memo(request.getMemo())
                .timeRecords(timeRecords)
                .build();
    }


    @PutMapping("/{id}")
    public PutUpdateRecordResponseDto updateRecordByPatch(@PathVariable Long id,
                                                            @RequestBody PutUpdateRecordRequestDto request) {
        log.info("updateRecordByPut 호출");

        log.info("updateRecordByPut 종료");

        //mock data
        Long timeId = 1L;
        List<RecordDto.Time> timeRecords = request.getTimeRecords().stream()
                .map(i -> RecordDto.Time.builder()
                        .timeId(timeId)
                        .date(i.getDate())
                        .startTime(i.getStartTime())
                        .durationMinutes(i.getDurationMinutes())
                        .build())
                .toList();

        RecordDto updatedRecords = RecordDto.builder()
                .recordId(id)
                .action(request.getAction())
                .memo(request.getMemo())
                .timeRecords(timeRecords)
                .build();


        //mock data
        RecordDto.Time time1 = RecordDto.Time.builder()
                .timeId(5L)
                .date(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(10, 10))
                .durationMinutes(30L)
                .build();

        RecordDto.Time time2 = RecordDto.Time.builder()
                .timeId(6L)
                .date(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(11, 10))
                .durationMinutes(30L)
                .build();

        RecordDto affectedRecord = RecordDto.builder()
                .recordId(id)
                .action("운동")
                .memo("벤치프레스")
                .timeRecords(List.of(time1, time2))
                .build();

        List<Long> deleteRecordsIds = List.of(10L, 11L, 12L);

        return PutUpdateRecordResponseDto.builder()
                .deleteRecordsIds(deleteRecordsIds)
                .affectedRecords(List.of(affectedRecord))
                .updatedRecord(updatedRecords)
                .build();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecord(@PathVariable Long id) {

    }
}
