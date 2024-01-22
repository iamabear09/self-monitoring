package monitoring.service;

import lombok.extern.slf4j.Slf4j;
import monitoring.domain.Record;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;


@Slf4j
@ExtendWith(MockitoExtension.class)
class RecordsServiceTest {

    @Mock
    RecordsRepository recordsRepository;

    @InjectMocks
    RecordsService recordsService;

    @Test
    @DisplayName("Record 를 생성할 수 있다.")
    void createRecord() {

        //given
        // >>> create Record without id
        String action = "공부";
        String memo = "코딩테스트";
        Record recordData = new Record(null, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time timeWithoutId1 = new Record.Time(null, date1, startTime1, durationMinutes1, recordData);

        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(5, 34);
        int durationMinutes2 = 120;
        Record.Time timeWithoutId2 = new Record.Time(null, date2, startTime2, durationMinutes2, recordData);
        // <<< create Record without id

        // >>> create Record for Mock Return
        Record record = recordData.toRecordWith(1L);

        AtomicLong idGenerator = new AtomicLong(0L);
        Set.copyOf(record.getTimeRecords())
                .forEach(t -> t.toTimeWith(idGenerator.incrementAndGet()));
        // <<< create Record for Mock Return

        given(recordsRepository.save(eq(recordData))).willReturn(record);

        //when
        Record createRecord = recordsService.create(recordData);

        //then
        assertThat(createRecord).isSameAs(record);
    }

}

