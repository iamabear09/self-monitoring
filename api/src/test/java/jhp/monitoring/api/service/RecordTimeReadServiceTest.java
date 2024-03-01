package jhp.monitoring.api.service;

import jhp.monitoring.domain.Record;
import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.service.RecordService;
import jhp.monitoring.service.TimeLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.will;

@ExtendWith(MockitoExtension.class)
class RecordTimeReadServiceTest {

    RecordService recordService = Mockito.mock(RecordService.class);
    TimeLogService timeLogService = Mockito.mock(TimeLogService.class);
    ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
    RecordTimeReadService recordTimeReadService = new RecordTimeReadService(recordService, timeLogService, threadPool);

    Record recordInput1;
    Record recordResult1;
    TimeLog timeInput1;
    TimeLog timeResult1;
    TimeLog timeInput2;
    TimeLog timeResult2;

    @BeforeEach
    void initThreadPool() {
        threadPool.setCorePoolSize(2);
        threadPool.setQueueCapacity(10);
        threadPool.setMaxPoolSize(10);
        threadPool.setThreadNamePrefix("test-");
        threadPool.initialize();
    }

    @BeforeEach
    void initGivenRecord() {
        //record1
        String recordId1 = "id1";
        String action1 = "공부";
        String memo1 = "Gradle 공부";
        recordInput1 = Record.builder()
                .id(recordId1)
                .action(action1)
                .memo(memo1)
                .build();
        recordResult1 = Record.builder()
                .id(recordId1)
                .action(action1)
                .memo(memo1)
                .build();
    }

    @BeforeEach
    void initGivenTimeLog() {
        /*
         * timeLogs1
         * 10:00 - 11:00
         */
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 0);
        LocalTime endTime1 = LocalTime.of(11, 0);
        Duration durationMinutes1 = Duration.ofMinutes(60L);
        timeInput1 = TimeLog.builder()
                .date(date1)
                .startTime(startTime1)
                .endTime(endTime1)
                .durationMinutes(durationMinutes1)
                .build();
        timeResult1 = TimeLog.builder()
                .id(1L)
                .date(date1)
                .startTime(startTime1)
                .endTime(endTime1)
                .durationMinutes(durationMinutes1)
                .build();

        /*
         * timeLogs2
         * 9:50 - 11:10
         */
        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(9, 50);
        LocalTime endTime2 = LocalTime.of(11, 10);
        Duration durationMinutes2 = Duration.ofMinutes(80L);
        timeInput2 = TimeLog.builder()
                .date(date2)
                .startTime(startTime2)
                .endTime(endTime2)
                .durationMinutes(durationMinutes2)
                .build();

        timeResult2 = TimeLog.builder()
                .id(2L)
                .date(date2)
                .startTime(startTime2)
                .endTime(endTime2)
                .durationMinutes(durationMinutes2)
                .build();
    }

    @Test
    @DisplayName("Record 를 조회할 수 있다.")
    void getRecordWithTimeLogs() {

        //given
        will(invocation -> {
            return recordResult1;
        }).given(recordService).get(recordInput1.getId());

        will(invocation -> {
            return List.of(timeResult1, timeResult2);
        }).given(timeLogService).getByRecordId(recordInput1.getId());

        //when
        Record findRecord = recordTimeReadService.getRecordWithTimeLogs(recordInput1.getId());

        //then
        assertThat(findRecord).isEqualTo(recordResult1);
        assertThat(findRecord.getTimeLogs())
                .hasSize(2)
                .contains(timeResult1, timeResult2);
    }
}
