package monitoring.service;

import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import monitoring.repository.TimeLogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TimeLogServiceTest {

    @Mock
    TimeLogRepository timeLogRepository;

    @InjectMocks
    TimeLogService timeLogService;


    //given
    Record recordStub1;
    TimeLog timeInput1;
    TimeLog timeInput2;
    TimeLog timeInput3;
    TimeLog timeInput4;
    TimeLog timeMockInput3;
    TimeLog timeMockInput4;


    TimeLog timeStub1;
    TimeLog timeStub2;
    TimeLog timeStub3;
    TimeLog timeStub4;

    @BeforeEach
    void initGiven() {

        recordStub1 = Record.builder()
                .id(1L)
                .action("공부")
                .memo("Gradle 공부")
                .build();


        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        LocalTime endTime1 = LocalTime.of(10, 30);
        Duration durationMinutes1 = Duration.ofMinutes(20L);
        timeInput1 = TimeLog.builder()
                .date(date1)
                .startTime(startTime1)
                .endTime(endTime1)
                .durationMinutes(durationMinutes1)
                .build();
        timeStub1 = TimeLog.builder()
                .id(1L)
                .date(date1)
                .startTime(startTime1)
                .endTime(endTime1)
                .durationMinutes(durationMinutes1)
                .record(recordStub1)
                .build();


        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(10, 40);
        LocalTime endTime2 = LocalTime.of(11, 10);
        Duration durationMinutes2 = Duration.ofMinutes(30L);
        timeInput2 = TimeLog.builder()
                .date(date2)
                .startTime(startTime2)
                .endTime(endTime2)
                .durationMinutes(durationMinutes2)
                .build();

        timeStub2 = TimeLog.builder()
                .id(2L)
                .date(date2)
                .startTime(startTime2)
                .endTime(endTime2)
                .durationMinutes(durationMinutes2)
                .record(recordStub1)
                .build();


        LocalDate date3 = LocalDate.of(2024, 1, 2);
        LocalTime startTime3 = LocalTime.of(10, 40);
        LocalTime endTime3 = LocalTime.of(11, 10);
        timeInput3 = TimeLog.builder()
                .date(date3)
                .startTime(startTime3)
                .endTime(endTime3)
                .build();

        Duration durationMinutes3 = Duration.ofMinutes(30L);
        timeMockInput3 = TimeLog.builder()
                .date(date3)
                .startTime(startTime3)
                .endTime(endTime3)
                .durationMinutes(durationMinutes3)
                .build();

        timeStub3 = TimeLog.builder()
                .id(3L)
                .date(date3)
                .startTime(startTime3)
                .durationMinutes(durationMinutes3)
                .record(recordStub1)
                .build();

        LocalDate date4 = LocalDate.of(2024, 1, 2);
        LocalTime startTime4 = LocalTime.of(10, 40);
        Duration durationMinutes4 = Duration.ofMinutes(20L);
        timeInput4 = TimeLog.builder()
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .record(recordStub1)
                .build();

        LocalTime endTime4 = LocalTime.of(11, 0);
        timeMockInput4 = TimeLog.builder()
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .endTime(endTime4)
                .record(recordStub1)
                .build();

        timeStub4 = TimeLog.builder()
                .id(4L)
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .endTime(endTime4)
                .record(recordStub1)
                .build();
    }

    @Test
    @DisplayName("TimeLog List 를 저장할 수 있다.")
    void saveTimeLogs() {

        //given
        given(timeLogRepository.saveAll(eq(List.of(timeInput1, timeInput2, timeMockInput4, timeMockInput3))))
                .willReturn(List.of(timeStub1, timeStub2, timeStub3, timeStub4));

        //when
        List<TimeLog> savedTimeLogs = timeLogService.save(recordStub1, List.of(timeInput1, timeInput2, timeInput4, timeInput3));

        //then
        assertThat(savedTimeLogs)
                .hasSize(4)
                .contains(timeStub1, timeStub2, timeStub3, timeStub4);
    }

    @Test
    @DisplayName(" RecordId 를 통해 해당 Record 의 모든 TimeLogs 를 삭제할 수 있다.")
    void deleteTimeLog() {

        //given
        recordStub1.setTimeLogs(List.of(timeStub1, timeStub2, timeStub3));
        given(timeLogRepository.findByRecordId(eq(1L))).willReturn(List.of(timeStub1, timeStub2, timeStub3));

        //when
        List<TimeLog> deletedTimeLogs = timeLogService.deleteByRecordId(1L);

        //then
        verify(timeLogRepository).deleteAllInBatch(eq(List.of(timeStub1, timeStub2, timeStub3)));

        assertThat(deletedTimeLogs)
                .hasSize(3)
                .extracting("record")
                .contains(null, null, null);
    }

}

