package monitoring.service;

import lombok.extern.slf4j.Slf4j;
import monitoring.domain.Record;
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
        Record recordData = new Record(null, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time timeData1 = new Record.Time(null, date1, startTime1, durationMinutes1, recordData);

        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(5, 34);
        int durationMinutes2 = 120;
        Record.Time timeData2 = new Record.Time(null, date2, startTime2, durationMinutes2, recordData);
        // <<< create Record without id


        // >>> create Record for Mock Return
        Record mockRecord = new Record(1L, action, memo);
        Record.Time mockTime1 = new Record.Time(1L, date1, startTime1, durationMinutes1, mockRecord);
        Record.Time mockTime2 = new Record.Time(2L, date2, startTime2, durationMinutes2, mockRecord);
        // <<< create Record for Mock Return

        given(recordRepository.save(eq(recordData))).willReturn(mockRecord);

        //when
        Record savedRecord = recordService.create(recordData);

        //then
        verify(timeRepository).saveAll(List.of(timeData1, timeData2));
        assertThat(savedRecord).isSameAs(mockRecord);

    }

    @Test
    @DisplayName("Record 를 조회할 수 있다.")
    void getRecord() {

        //given
        // >>> create Record
        String action = "공부";
        String memo = "코딩테스트";
        long recordId = 1L;
        Record recordData = new Record(recordId, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time timeData1 = new Record.Time(1L, date1, startTime1, durationMinutes1,null);

        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(5, 34);
        int durationMinutes2 = 120;
        Record.Time timeData2 = new Record.Time(2L, date2, startTime2, durationMinutes2, null);
        // <<< create Record

        given(recordRepository.findById(eq(recordId))).willReturn(Optional.of(recordData));
        given(timeRepository.findByRecordId(eq(recordId))).willReturn(List.of(timeData1, timeData2));

        //when
        Record record = recordService.get(recordId);

        //then
        assertThat(record.getId()).isEqualTo(recordData.getId());
        assertThat(record.getAction()).isEqualTo(recordData.getAction());
        assertThat(record.getMemo()).isEqualTo(recordData.getMemo());
        assertThat(record.getTimeRecords())
                .hasSize(2)
                .containsExactly(timeData1, timeData2);

    }

}

