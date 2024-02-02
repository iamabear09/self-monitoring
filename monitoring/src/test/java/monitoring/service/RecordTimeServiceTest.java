package monitoring.service;

import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecordTimeServiceTest {


    @Mock
    TimeLogService timeLogService;
    @Mock
    RecordService recordService;
    @InjectMocks
    RecordTimeService recordTimeService;


    //given
    Record recordInput1;
    Record recordStub1;
    Record recordInput2;
    Record recordStub2;
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

        String action1 = "공부";
        String memo1 = "Gradle 공부";
        recordInput1 = Record.builder()
                .action(action1)
                .memo(memo1)
                .build();

        recordStub1 = Record.builder()
                .id(1L)
                .action(action1)
                .memo(memo1)
                .build();



        String action2 = "헬스";
        String memo2 = "벤치프레스";
        recordInput2 = Record.builder()
                .action(action2)
                .memo(memo2)
                .build();

        recordStub2 = Record.builder()
                .id(2L)
                .action(action2)
                .memo(memo2)
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
    @DisplayName(" Record 와 TimeLog 를 저장 할 수 있다.")
    void save() {

        //given
        List<TimeLog> timeInputList = List.of(timeInput1, timeInput2);
        recordInput1.getTimeLogs().addAll(timeInputList);
        List<TimeLog> timeStubList = List.of(timeStub1, timeStub2);

        given(recordService.save(eq(recordInput1))).willReturn(recordStub1);
        given(timeLogService.save(eq(recordStub1), eq(timeInputList))).willReturn(timeStubList);

        //when
        Record savedRecord = recordTimeService.save(recordInput1);

        //then
        assertThat(savedRecord).isEqualTo(recordStub1);
        assertThat(savedRecord.getTimeLogs())
                .hasSize(2)
                .contains(timeStub1, timeStub2);
    }

    @Test
    @DisplayName("Record 를 업데이트 할 수 있다. 이 때, TimeLog 는 모두 삭제되었다가 새로 추가 된다. 기존에 존재하던 Time 과 중복을 허용한다.")
    void updateAllowingOverlap() {
        //given
        List<TimeLog> deleteTimeStubList = List.of(timeStub1, timeStub2);

        given(recordService.get(eq(1L))).willReturn(recordStub1);
        given(timeLogService.deleteByRecordId(eq(1L))).willReturn(deleteTimeStubList);

        List<TimeLog> updateTimeInputList = List.of(timeMockInput3, timeMockInput4);
        recordInput2.getTimeLogs().addAll(updateTimeInputList);
        recordStub2.setId(1L);
        given(recordService.update(1L, recordInput2)).willReturn(recordStub2);

        List<TimeLog> updatedTimeLogs = List.of(timeStub3, timeStub4);
        given(timeLogService.save(eq(recordStub2), eq(updateTimeInputList))).willReturn(updatedTimeLogs);

        //when
        Record updateRecord = recordTimeService.updateAllowingOverlap(1L, recordInput2);

        //then
        assertThat(deleteTimeStubList)
                .hasSize(2)
                .extracting("record")
                .containsExactly(null, null);

        assertThat(updateRecord).isEqualTo(recordStub2);
        assertThat(updateRecord.getTimeLogs())
                .hasSize(2)
                .contains(timeStub3, timeStub4);

        assertThat(updateRecord.getTimeLogs())
                .hasSize(2)
                .extracting("record")
                .contains(recordStub2, recordStub2);
    }


    
/*
    @Test
    @DisplayName("")
    void updateNotAllowingOverlap() {

        //given
        //업데이트할 정보

        //11:00 - 12:00
        recordTimeService.connectRecordAndTimeLogs(recordInput1, List.of(timeInput2));

        Record willBedeletedRecord = Record.builder()
                .id(10L)
                .action("테스트 액션")
                .memo("테스트 메모")
                .build();

        //11:00 - 12:00
        TimeLog sameRangeTimeLog = TimeLog.builder()
                .id(10L)
                .record(willBedeletedRecord)
                .date(timeInput2.getDate())
                .startTime(timeInput2.getStartTime())
                .endTime(timeInput2.getEndTime())
                .durationMinutes(timeInput2.getDurationMinutes())
                .build();

        //11:10 - 11:50
        TimeLog includedRangeTimeLog = TimeLog.builder()
                .id(11L)
                .record(willBedeletedRecord)
                .date(timeInput2.getDate())
                .startTime(timeInput2.getStartTime().plusMinutes(10L))
                .endTime(timeInput2.getEndTime().minusMinutes(10L))
                .durationMinutes(timeInput2.getDurationMinutes().minusMinutes(20L))
                .build();

        recordTimeService.connectRecordAndTimeLogs(willBedeletedRecord, List.of(sameRangeTimeLog, includedRangeTimeLog));

        given(timeLogService.searchOverlappingTimeLogs(timeInput2.getDate(), timeInput2.getStartTime(), timeInput2.getEndTime()))
                .willReturn(List.of(sameRangeTimeLog, includedRangeTimeLog));

        //when
        recordTimeService.updateNotAllowingOverlap(1L, recordInput1);

    }*/
    private Record copyRecordFrom(Record record) {
        if (record == null) {
            return null;
        }

        Record copyRecord = Record.builder()
                .id(record.getId())
                .action(record.getAction())
                .memo(record.getMemo())
                .build();

        if (record.getTimeLogs() != null) {
            record.getTimeLogs().forEach(t -> {
                TimeLog copyTimeLog = copyTimeLogFrom(t);
                copyTimeLog.setRecord(copyRecord);
                copyRecord.getTimeLogs().add(copyTimeLog);
            });
        }

        return copyRecord;
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

