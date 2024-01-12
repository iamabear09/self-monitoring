package monitoring.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/records")
public class RecordsApiController {

    @PostMapping
    public CreateRecordsResponseDto createRecords(@RequestBody CreateRecordsRequestDto request) {
        log.info(">>> createRecords 호출");
        log.info(">>> request: {}", request.toString());

        log.info(">>> createRecords 종료");

        return CreateRecordsResponseDto.builder()
                .id(1L)
                .build();

    }

}
