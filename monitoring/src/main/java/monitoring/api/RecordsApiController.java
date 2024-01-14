package monitoring.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/records")
public class RecordsApiController {

    @PostMapping
    public CreateRecordResponseDto createRecords(@RequestBody CreateRecordRequestDto request) {
        log.info(">>> createRecords 호출");

        log.info(">>> createRecords 종료");
        return CreateRecordResponseDto.builder()
                .recordId(1L)
                .build();
    }

    @PatchMapping("/{id}")
    public UpdateRecordResponseDto updateRecords(@PathVariable Long id,
                                                 @RequestBody UpdateRecordRequestDto request) {
        log.info("updateRecords 호출");

        log.info("updateRecords 종료");
        return UpdateRecordResponseDto.builder()
                .recordId(id)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .durationMinutes(request.getDurationMinutes())
                .action(request.getAction())
                .memo(request.getMemo())
                .build();
    }
}
