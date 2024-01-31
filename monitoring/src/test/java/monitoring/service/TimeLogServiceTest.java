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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TimeLogServiceTest {

    @Mock
    TimeLogRepository timeLogRepository;

    @InjectMocks
    TimeLogService timeLogService;


    //given
    Record savedRecord;
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

        savedRecord = Record.builder()
                .id(1L)
                .action("공부")
                .memo("Gradle 공부")
                .build();

        timeInput1 = TimeLog.builder()
                .date(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(10, 10))
                .endTime(LocalTime.of(10, 30))
                .durationMinutes(Duration.ofMinutes(20L))
                .build();

        timeInput2 = TimeLog.builder()
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .endTime(LocalTime.of(11, 10))
                .durationMinutes(Duration.ofMinutes(30L))
                .build();


        timeStub1 = TimeLog.builder()
                .id(1L)
                .date(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(10, 10))
                .endTime(LocalTime.of(10, 30))
                .record(savedRecord)
                .build();

        timeStub2 = TimeLog.builder()
                .id(2L)
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(30L))
                .record(savedRecord)
                .build();


        timeInput3 = TimeLog.builder()
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .endTime(LocalTime.of(11, 10))
                .build();

        timeMockInput3 = TimeLog.builder()
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .endTime(LocalTime.of(11, 10))
                .durationMinutes(Duration.ofMinutes(30L))
                .build();

        timeInput4 = TimeLog.builder()
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(20L))
                .record(savedRecord)
                .build();

        timeMockInput4 = TimeLog.builder()
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(20L))
                .endTime(LocalTime.of(11,0))
                .record(savedRecord)
                .build();

        timeStub3 = TimeLog.builder()
                .id(3L)
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(20L))
                .record(savedRecord)
                .build();

        timeStub4 = TimeLog.builder()
                .id(4L)
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(20L))
                .endTime(LocalTime.of(11,0))
                .record(savedRecord)
                .build();
    }

    @Test
    @DisplayName("TimeLog List 를 저장할 수 있다.")
    void saveTimeLogs() {

        //given
        given(timeLogRepository.saveAll(eq(List.of(timeInput1, timeInput2, timeMockInput4, timeMockInput3))))
                .willReturn(List.of(timeStub1, timeStub2, timeStub3, timeStub4));

        //when
        List<TimeLog> savedTimeLogs = timeLogService.save(savedRecord, List.of(timeInput1, timeInput2, timeInput4, timeInput3));

        //then
        Assertions.assertThat(savedTimeLogs)
                .hasSize(4)
                .contains(timeStub1, timeStub2, timeStub3, timeStub4);
    }

}