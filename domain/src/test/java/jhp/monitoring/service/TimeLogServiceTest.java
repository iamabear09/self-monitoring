package jhp.monitoring.service;

import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.domain.Record;
import jhp.monitoring.repository.TimeLogRepository;
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
import static org.mockito.BDDMockito.will;
import static org.mockito.Mockito.times;
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
    TimeLog timeStub5;
    TimeLog timeStub6;

    @BeforeEach
    void initGiven() {

        recordStub1 = Record.builder()
                .id(1L)
                .action("공부")
                .memo("Gradle 공부")
                .build();


        //10:50 - 12:10
        // <--- time --->
        //   <-range->
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 50);
        LocalTime endTime1 = LocalTime.of(12, 10);
        Duration durationMinutes1 = Duration.ofMinutes(80L);
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


        //11:00 - 12:00
        // <-----time----->
        // <-----range---->
        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(11, 0);
        LocalTime endTime2 = LocalTime.of(12, 0);
        Duration durationMinutes2 = Duration.ofMinutes(60L);
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


        //10:30 - 11:00
        // <--time-->
        //           <-----range---->
        LocalDate date3 = LocalDate.of(2024, 1, 1);
        LocalTime startTime3 = LocalTime.of(10, 30);
        LocalTime endTime3 = LocalTime.of(11, 0);
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
                .endTime(endTime3)
                .durationMinutes(durationMinutes3)
                .record(recordStub1)
                .build();

        //12:00 - 12:30
        //                <--time-->
        //<-----range---->
        LocalDate date4 = LocalDate.of(2024, 1, 1);
        LocalTime startTime4 = LocalTime.of(12, 0);
        Duration durationMinutes4 = Duration.ofMinutes(30L);
        timeInput4 = TimeLog.builder()
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .record(recordStub1)
                .build();

        LocalTime endTime4 = LocalTime.of(12, 30);
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

        //11:30 - 12:30
        //           <--time-->
        //<-----range---->
        LocalDate date5 = LocalDate.of(2024, 1, 1);
        LocalTime startTime5 = LocalTime.of(11, 30);
        LocalTime endTime5 = LocalTime.of(12, 30);
        Duration durationMinutes5 = Duration.ofMinutes(60L);
        timeStub5 = TimeLog.builder()
                .id(5L)
                .date(date5)
                .startTime(startTime5)
                .endTime(endTime5)
                .durationMinutes(durationMinutes5)
                .record(recordStub1)
                .build();

        //10:30 - 11:30
        //<--time-->
        //      <-----range---->
        LocalDate date6 = LocalDate.of(2024, 1, 1);
        LocalTime startTime6 = LocalTime.of(10, 30);
        LocalTime endTime6 = LocalTime.of(11, 30);
        Duration durationMinutes6 = Duration.ofMinutes(60L);
        timeStub6 = TimeLog.builder()
                .id(6L)
                .date(date6)
                .startTime(startTime6)
                .endTime(endTime6)
                .durationMinutes(durationMinutes6)
                .record(recordStub1)
                .build();
    }

    @Test
    @DisplayName("TimeLog List 를 저장할 수 있다.")
    void saveTimeLogs() {

        //given
        given(timeLogRepository.save(timeInput1)).willReturn(timeStub1);
        given(timeLogRepository.save(timeInput2)).willReturn(timeStub2);
        given(timeLogRepository.save(timeMockInput3)).willReturn(timeStub3);
        given(timeLogRepository.save(timeMockInput4)).willReturn(timeStub4);

        //when
        List<TimeLog> savedTimeLogs = timeLogService.create(recordStub1, List.of(timeInput1, timeInput2, timeInput4, timeInput3));

        //then
        assertThat(savedTimeLogs)
                .hasSize(4)
                .contains(timeStub1, timeStub2, timeStub3, timeStub4);
    }

    @Test
    @DisplayName("주어진 Time Line 과 겹치는 구간이 있는 Time Log 를 찾을 수 있다.")
    void searchOverlappingTimeLogs() {

        //given
        given(timeLogRepository.findByDate(eq(LocalDate.of(2024, 1, 1))))
                .willReturn(List.of(timeStub1, timeStub2, timeStub3, timeStub4, timeStub5, timeStub6));

        LocalTime from = LocalTime.of(11, 0);
        LocalTime to = LocalTime.of(12, 0);

        //when
        List<TimeLog> filteredTimeLogs = timeLogService.searchOverlappingTimeLogs(LocalDate.of(2024, 1, 1), from, to);

        //then
        Assertions.assertThat(filteredTimeLogs)
                .hasSize(4)
                .contains(timeStub1, timeStub2, timeStub5, timeStub6);
    }

    @Test
    @DisplayName(" TimeLog 에서 주어진 Time 범위와 겹치는 시간을 제거한 Time Log 들로 분리할 수 있다.")
    void split() {
        //given

        TimeLog includeSplitTimeFront = copyTimeLogFrom(timeInput1);
        includeSplitTimeFront.setStartTime(LocalTime.of(10, 50));
        includeSplitTimeFront.setEndTime(LocalTime.of(11, 0));
        includeSplitTimeFront.setDurationMinutes(Duration.ofMinutes(10L));

        TimeLog includeSplitTimeRear = copyTimeLogFrom(timeInput1);
        includeSplitTimeRear.setStartTime(LocalTime.of(12, 0));
        includeSplitTimeRear.setEndTime(LocalTime.of(12, 10));
        includeSplitTimeRear.setDurationMinutes(Duration.ofMinutes(10L));

        TimeLog includeSplitTimeFrontResult = copyTimeLogFrom(includeSplitTimeFront);
        TimeLog includeSplitTimeRearResult = copyTimeLogFrom(includeSplitTimeRear);

        will(invocation -> {
            includeSplitTimeFrontResult.setId(10L);
            includeSplitTimeRearResult.setId(11L);

            return List.of(includeSplitTimeFrontResult, includeSplitTimeRearResult);
        }).given(timeLogRepository).saveAll(List.of(includeSplitTimeFront, includeSplitTimeRear));

        will(invocation -> {
            return List.of();
        }).given(timeLogRepository).saveAll(List.of());


        TimeLog partSplitTimeInput = copyTimeLogFrom(timeStub5);
        partSplitTimeInput.setId(null);
        partSplitTimeInput.setStartTime(LocalTime.of(12, 0));
        partSplitTimeInput.setEndTime(LocalTime.of(12, 30));
        partSplitTimeInput.setDurationMinutes(Duration.ofMinutes(30L));

        TimeLog partSplitTimeResult = copyTimeLogFrom(partSplitTimeInput);

        will(invocation -> {
            partSplitTimeResult.setId(12L);

            return List.of(partSplitTimeResult);
        }).given(timeLogRepository).saveAll(List.of(partSplitTimeInput));

        LocalTime startTime = LocalTime.of(11, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        //when
        List<TimeLog> includeEntireRange = timeLogService.splitByRemovingOverlapTimeRange(timeStub1, startTime, endTime);
        List<TimeLog> insideOfRange = timeLogService.splitByRemovingOverlapTimeRange(timeStub2, startTime, endTime);
        List<TimeLog> insidePartOfRange = timeLogService.splitByRemovingOverlapTimeRange(timeStub5, startTime, endTime);

        //then
        verify(timeLogRepository, times(1)).deleteById(1L);
        verify(timeLogRepository, times(1)).deleteById(2L);
        verify(timeLogRepository, times(1)).deleteById(5L);

        assertThat(includeEntireRange)
                .hasSize(2)
                .contains(includeSplitTimeFrontResult, includeSplitTimeRearResult);

        assertThat(insideOfRange.isEmpty()).isTrue();

        assertThat(insidePartOfRange)
                .hasSize(1)
                .contains(partSplitTimeResult);

    }


    private TimeLog copyTimeLogFrom(TimeLog timeLog) {
        if (timeLog == null) {
            return null;
        }

        return TimeLog.builder()
                .id(timeLog.getId())
                .date(timeLog.getDate())
                .startTime(timeLog.getStartTime())
                .endTime(timeLog.getEndTime())
                .durationMinutes(timeLog.getDurationMinutes())
                .record(timeLog.getRecord())
                .build();
    }

}

