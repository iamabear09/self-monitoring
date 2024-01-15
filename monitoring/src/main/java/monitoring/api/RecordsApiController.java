package monitoring.api;

import lombok.extern.slf4j.Slf4j;
import monitoring.api.dto.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
        LocalDate date = LocalDate.of(2024, 1, 13);
        LocalTime startTime = LocalTime.of(13, 10);
        Long durationMinutes = 60L;
        String action = "운동";
        String memo = "헬스장";

        log.info(">>> getRecord 종료");
        return RecordDto.builder()
                .recordId(id)
                .action(action)
                .memo(memo)
                .build();

    }

    @PatchMapping("/{id}")
    public PatchUpdateRecordResponseDto updateRecordByPatch(@PathVariable Long id,
                                                            @RequestBody PatchUpdateRecordRequestDto request) {
        log.info("updateRecordByPatch 호출");

        log.info("updateRecordByPatch 종료");
        return PatchUpdateRecordResponseDto.builder()
                .recordId(id)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .durationMinutes(request.getDurationMinutes())
                .action(request.getAction())
                .memo(request.getMemo())
                .build();
    }

    @PutMapping("/{id}")
    public PutUpdateRecordResponseDto updateRecordByPatch(@PathVariable Long id,
                                                            @RequestBody PutUpdateRecordRequestDto request) {
        log.info("updateRecordByPut 호출");

        log.info("updateRecordByPut 종료");
        return PutUpdateRecordResponseDto.builder()
                .recordId(id)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .durationMinutes(request.getDurationMinutes())
                .action(request.getAction())
                .memo(request.getMemo())
                .build();
    }
}
