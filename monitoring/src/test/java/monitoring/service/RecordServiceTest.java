package monitoring.service;

import lombok.extern.slf4j.Slf4j;
import monitoring.domain.Record;
import monitoring.domain.Time;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;

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
        Record savedRecord = recordService.create(recordData);

        //then
        assertThat(savedRecord).isSameAs(mockRecord);
        assertThat(savedRecord.getTimeRecords())
                .hasSize(2)
                .containsExactly(mockTime1, mockTime2);

    }

    @Test
    @DisplayName("Record 를 Id를 통해 조회할 수 있다.")
    void get() {

        //given
        // >>> create Record for Mock Return
        String action = "공부";
        String memo = "코딩테스트";
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);

        int durationMinutes1 = 30;
        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(5, 34);
        int durationMinutes2 = 120;

        long recordId = 1L;
        Record mockRecord = Record.builder()
                .id(recordId)
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
        given(recordRepository.findById(eq(recordId))).willReturn(Optional.ofNullable(mockRecord));
        given(timeRepository.findByRecordId(eq(recordId))).willReturn(List.of(mockTime1, mockTime2));
        // <<< stub

        //when
        Record record = recordService.get(recordId);

        //then
        assertThat(record).isEqualTo(mockRecord);
        assertThat(record.getTimeRecords())
                .hasSize(2)
                .containsExactly(mockTime1, mockTime2);

    }


    @Test
    @DisplayName("Record 를 수정할 수 있다.")
    void update() {

        //given
        // >>> create Record for Mock Return
        String action = "공부";
        String memo = "코딩테스트";
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);

        int durationMinutes1 = 30;
        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(5, 34);
        int durationMinutes2 = 120;

        long recordId = 1L;
        Record mockRecord = Record.builder()
                .id(recordId)
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

        // >>> create Record for Mock Return
        String updateAction = "공부";
        String updateMemo = "코딩테스트";

        LocalDate updateDate1 = LocalDate.of(2024, 10, 10);
        LocalTime updateStartTime1 = LocalTime.of(11, 5);
        int updateDurationMinutes1 = 200;

        Record updateRecord = Record.builder()
                .id(recordId)
                .action(updateAction)
                .memo(updateMemo)
                .build();

        Time updateTime = Time.builder()
                .id(1L)
                .date(updateDate1)
                .startTime(updateStartTime1)
                .durationMinutes(updateDurationMinutes1)
                .endTime(updateStartTime1.plusMinutes(updateDurationMinutes1))
                .record(updateRecord)
                .build();

        updateRecord.setTimeRecords(List.of(updateTime));
        // <<< create Record for Mock Return

        // >>> stub
        given(recordRepository.findById(eq(recordId))).willReturn(Optional.ofNullable(mockRecord));
        given(recordRepository.save(eq(mockRecord))).willReturn(mockRecord);
        given(timeRepository.findByRecordId(eq(recordId))).willReturn(List.of(mockTime1, mockTime2));
        given(timeRepository.save(eq(updateTime))).willReturn(updateTime);
        // <<< stub

        //when
        Record updatedRecord = recordService.update(recordId, updateRecord);

        //then
        assertThat(updatedRecord).isEqualTo(mockRecord);
        assertThat(updatedRecord.getTimeRecords())
                .hasSize(1)
                .containsExactly(updateTime);

    }

}

