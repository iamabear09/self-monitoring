package monitoring.service;

import lombok.extern.slf4j.Slf4j;
import monitoring.domain.Record;
import monitoring.domain.Time;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    RecordRepository recordRepository;

    @Mock
    TimeRepository timeRepository;

    @InjectMocks
    RecordService recordService;

    @Test
    @DisplayName("Record 를 생성할 수 있다.")
    void createRecord() {

        //given
        // >>> create Record without id
        String action = "공부";
        String memo = "코딩테스트";
        Record recordData = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Time timeData1 = Time.builder()
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .endTime(startTime1.plusMinutes(durationMinutes1))
                .build();

        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(5, 34);
        int durationMinutes2 = 120;
        Time timeData2 = Time.builder()
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .endTime(startTime2.plusMinutes(durationMinutes2))
                .build();
        // <<< create Record without id


        // >>> create Record for Mock Return
        Record mockRecord = Record.builder()
                .id(1L)
                .action(action)
                .memo(memo)
                .build();

        Time mockTime1 = Time.builder()
                .id(1L)
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .endTime(startTime1.plusMinutes(durationMinutes1))
                .build();
        Time mockTime2 = Time.builder()
                .id(2L)
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .endTime(startTime2.plusMinutes(durationMinutes2))
                .build();
        // <<< create Record for Mock Return

        // >>> stub
        given(recordRepository.save(eq(recordData))).willReturn(mockRecord);
        given(timeRepository.save(eq(timeData1))).willReturn(mockTime1);
        given(timeRepository.save(eq(timeData2))).willReturn(mockTime2);
        // <<< stub

        //when
        Record savedRecord = recordService.create(recordData, List.of(timeData1, timeData2));

        //then
        assertThat(savedRecord).isSameAs(mockRecord);
        assertThat(savedRecord.getTimeRecords())
                .hasSize(2)
                .containsExactly(mockTime1, mockTime2);

    }

}

