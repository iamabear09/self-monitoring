package monitoring.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RestController
@RequestMapping("/api/records")
public class RecordsApiController {

    @PostMapping
    public CreateRecordsResponseDto createRecords(@RequestBody CreateRecordsRequestDto request) {
        log.info(">>> createRecords 호출");
        log.info(">>> request: {}", request.toString());

        log.info(">>> createRecords 종료");

        return CreateRecordsResponseDto.builder()
                .recordId(1L)
                .build();
    }

    @PatchMapping("/{id}")
    public UpdateRecordResponseDto updateRecords(@PathVariable Long id,
                                                 @RequestBody UpdateRecordRequestDto request) {

        log.info(">>> request :: {}", request.toString());

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
