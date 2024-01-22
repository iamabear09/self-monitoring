package monitoring.service;

import lombok.extern.slf4j.Slf4j;
import monitoring.domain.Record;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;


@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RecordsServiceTest {

    @Mock
    RecordsRepository recordsRepository;
    @Mock
    TimeRecordsRepository timeRecordsRepository;
    @InjectMocks
    RecordsService recordsService;

    @Test
    @DisplayName("Record 정보를 생성 할 수 있다.")
    void createRecord() {

        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record inputRecord = Record.builder()
                .action(action)
                .memo(memo)
                .build();
        
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time inputTime1 = Record.Time.builder()
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build();

        LocalDate date2 = LocalDate.of(2024, 5, 5);
        LocalTime startTime2 = LocalTime.of(5, 30);
        int durationMinutes2 = 45;
        Record.Time inputTime2 = Record.Time.builder()
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build();

        inputRecord.addTimeRecords(Set.of(inputTime1, inputTime2));

        long recordId = 1L;
        Record record = Record.builder()
                .recordId(recordId)
                .action(action)
                .memo(memo)
                .build();

        Record.Time time1 = Record.Time.builder()
                .timeId(1L)
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .record(record)
                .build();

        Record.Time time2 = Record.Time.builder()
                .timeId(2L)
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .record(record)
                .build();

        given(recordsRepository.save(eq(inputRecord))).willReturn(record);
        given(timeRecordsRepository.save(eq(inputTime1))).willReturn(time1);
        given(timeRecordsRepository.save(eq(inputTime2))).willReturn(time2);

        //when
        Record createdRecord = recordsService.create(inputRecord);

        //then
        assertSoftly(softly -> {
            softly.assertThat(createdRecord.getRecordId()).isEqualTo(recordId);
            softly.assertThat(createdRecord.getAction()).isEqualTo(action);
            softly.assertThat(createdRecord.getMemo()).isEqualTo(memo);
        });

        assertThat(createdRecord.getTimeRecords())
                .hasSize(2)
                .extracting("date", "startTime", "durationMinutes", "record")
                .contains(
                        tuple(date1, startTime1, durationMinutes1, record),
                        tuple(date2, startTime2, durationMinutes2, record)
                );
    }
}

