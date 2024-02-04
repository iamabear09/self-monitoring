package monitoring.service;

import monitoring.domain.Record;
import monitoring.domain.TimeLog;
import monitoring.service.dto.UpdateRecordResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecordTimeServiceTest {


    @Mock
    TimeLogService timeLogService;
    @Mock
    RecordService recordService;

    @Spy
    @InjectMocks
    RecordTimeService recordTimeService;


    //given
    Record updateRecordInput1;
    Record updateRecordStub1;
    Record recordInput1;
    Record recordStub1;
    Record recordInput2;
    Record recordStub2;
    Record recordInput3;
    Record recordStub3;
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

        String updateAction = "밥";
        String updateMemo = "고등어";
        updateRecordInput1 = Record.builder()
                .action(updateAction)
                .memo(updateMemo)
                .build();
        updateRecordStub1 = Record.builder()
                .action(updateAction)
                .memo(updateMemo)
                .build();


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

        String action3 = "밥";
        String memo3 = "오트밀";
        recordInput3 = Record.builder()
                .action(action3)
                .memo(memo3)
                .build();
        recordStub3 = Record.builder()
                .id(3L)
                .action(action3)
                .memo(memo3)
                .build();




        //10:30 - 11:30
        // <--- time --->
        //   <-range->
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 30);
        LocalTime endTime1 = LocalTime.of(11, 30);
        Duration durationMinutes1 = Duration.ofMinutes(60L);
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

        //9:50 - 11:10
        // <-----time----->
        // <-----range---->
        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(9, 50);
        LocalTime endTime2 = LocalTime.of(11, 10);
        Duration durationMinutes2 = Duration.ofMinutes(80L);
        timeInput2 = TimeLog.builder()
                .date(date2)
                .startTime(startTime2)
                .endTime(endTime2)
                .durationMinutes(durationMinutes2)
                .record(recordStub1)
                .build();

        timeStub2 = TimeLog.builder()
                .id(2L)
                .date(date2)
                .startTime(startTime2)
                .endTime(endTime2)
                .durationMinutes(durationMinutes2)
                .record(recordStub1)
                .build();


        //10:30 - 11:30
        // <--time-->
        //           <-----range---->
        LocalDate date3 = LocalDate.of(2024, 1, 1);
        LocalTime startTime3 = LocalTime.of(10, 30);
        LocalTime endTime3 = LocalTime.of(11, 30);
        timeInput3 = TimeLog.builder()
                .date(date3)
                .startTime(startTime3)
                .endTime(endTime3)
                .build();

        Duration durationMinutes3 = Duration.ofMinutes(60L);
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

        //09:30 - 10:30
        //                <--time-->
        //<-----range---->
        LocalDate date4 = LocalDate.of(2024, 1, 1);
        LocalTime startTime4 = LocalTime.of(9, 30);
        Duration durationMinutes4 = Duration.ofMinutes(60L);
        timeInput4 = TimeLog.builder()
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .record(recordStub1)
                .build();

        LocalTime endTime4 = LocalTime.of(10, 30);
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

        //10:00 - 11:00
        //           <--time-->
        //<-----range---->
        LocalDate date5 = LocalDate.of(2024, 1, 1);
        LocalTime startTime5 = LocalTime.of(10, 0);
        LocalTime endTime5 = LocalTime.of(11, 0);
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
        recordInput1.getTimeLogs().addAll(List.of(timeInput1, timeInput2));
        given(recordService.create(eq(recordInput1))).willReturn(recordStub1);

        List<TimeLog> timeStubList = List.of(timeStub1, timeStub2);
        timeStub1.setRecord(recordStub1);
        timeStub2.setRecord(recordStub1);
        given(timeLogService.create(eq(recordStub1), eq(timeInputList))).willReturn(timeStubList);

        //when
        Record savedRecord = recordTimeService.create(recordInput1);

        //then
        assertThat(savedRecord).isEqualTo(recordStub1);
        assertThat(savedRecord.getTimeLogs())
                .hasSize(2)
                .contains(timeStub1, timeStub2);
    }

    @Test
    @DisplayName("Record 를 업데이트 할 수 있다. 이 때, TimeLog 는 모두 삭제되었다가 새로 추가 된다. 기존에 존재하던 Time 과 중복을 허용한다.")
    void update() {
        //given
        List<TimeLog> updateTimeInputList = List.of(timeInput1, timeInput2);
        updateRecordInput1.setTimeLogs(updateTimeInputList);

        given(recordService.update(eq(1L), eq(updateRecordInput1))).will(invocation -> {
            updateRecordStub1.setId(1L);
            return updateRecordStub1;
        });

        given(timeLogService.create(eq(updateRecordStub1), eq(updateTimeInputList))).will(invocation -> {
            return List.of(timeStub1, timeStub2);
        });

        //when
        Record updateRecord = recordTimeService.update(1L, updateRecordInput1);

        //then
        verify(timeLogService).deleteByRecordId(1L);

        assertThat(updateRecord).isEqualTo(updateRecordStub1);
        assertThat(updateRecord.getTimeLogs())
                .hasSize(2)
                .contains(timeStub1, timeStub2);

        assertThat(updateRecord.getTimeLogs())
                .hasSize(2)
                .extracting("record")
                .contains(updateRecordStub1, updateRecordStub1);
    }

    @Test
    @DisplayName(" Update 시 다른 TimeLog 와의 중복을 제거하면서 업데이트한다.")
    void updateWithSideEffect() {

        //given

        // >>> update Input
        TimeLog timeInput10_11 = copyTimeLogFrom(timeInput1);

        timeInput10_11.setStartTime(LocalTime.of(10,0));
        timeInput10_11.setEndTime(LocalTime.of(11,0));
        timeInput10_11.setRecord(updateRecordInput1);

        updateRecordInput1.getTimeLogs().add(timeInput10_11);
        // <<< update Input

        // >>> overlap time logs 1
        TimeLog timeInclude0950_1110 = copyTimeLogFrom(timeStub2);
        TimeLog timeInclude1030_1130 = copyTimeLogFrom(timeStub3);
        timeInclude0950_1110.setRecord(recordStub2);
        timeInclude1030_1130.setRecord(recordStub2);
        will(invocation -> {
            //이렇게 하지 않으면, recordStub2에서 time 을 삭제해도 다시 조회할 때, Time 이 그대로 남아있다.
            if (recordStub2.getTimeLogs().isEmpty()) {
                recordStub2.getTimeLogs().add(timeInclude0950_1110);
                recordStub2.getTimeLogs().add(timeInclude1030_1130);
            }

            return recordStub2;
        }).given(recordTimeService).getRecordWithTimeLogs(2L);
        // <<< overlap time logs 1


        //overlap time logs 2
        TimeLog timeSame10_11 = copyTimeLogFrom(timeStub5);
        timeSame10_11.setRecord(recordStub3);
        will(invocation -> {
            if (recordStub3.getTimeLogs().isEmpty()) {
                recordStub3.getTimeLogs().add(timeSame10_11);
            }
            return recordStub3;
        }).given(recordTimeService).getRecordWithTimeLogs(3L);
        //overlap time logs 2


        // >>> searched TimeLogs mocking
        willReturn(List.of(timeStub1, timeInclude0950_1110, timeInclude1030_1130, timeSame10_11))
                .given(timeLogService).searchOverlappingTimeLogs(timeInput10_11.getDate(), timeInput10_11.getStartTime(), timeInput10_11.getEndTime());
        // <<< searched TimeLogs mocking


        // >>> split time 1
        TimeLog splitTime0950_1000 = copyTimeLogFrom(timeInclude0950_1110);
        TimeLog splitTime1100_1110 = copyTimeLogFrom(timeInclude0950_1110);
        will(invocation -> {
            splitTime0950_1000.setId(10L);
            splitTime0950_1000.setEndTime(LocalTime.of(10, 0));
            splitTime0950_1000.setDurationMinutes(Duration.ofMinutes(10L));

            splitTime1100_1110.setId(11L);
            splitTime1100_1110.setStartTime(LocalTime.of(11, 10));
            splitTime1100_1110.setDurationMinutes(Duration.ofMinutes(10L));

            return List.of(splitTime0950_1000, splitTime1100_1110);
        }).given(timeLogService).splitByRemovingOverlapTimeRange(timeInclude0950_1110, timeInput10_11.getStartTime(), timeInput10_11.getEndTime());
        // <<< split time 1


        // >>> split time 2
        TimeLog splitTime1100_1130 = copyTimeLogFrom(timeInclude1030_1130);
        will(invocation -> {
            splitTime1100_1130.setId(12L);
            splitTime1100_1130.setStartTime(LocalTime.of(11, 0));

            return List.of(splitTime1100_1130);
        }).given(timeLogService).splitByRemovingOverlapTimeRange(timeInclude1030_1130, timeInput10_11.getStartTime(), timeInput10_11.getEndTime());
        // <<< split time 2


        // >>> split time 3
        willReturn(new ArrayList<>())
                .given(timeLogService).splitByRemovingOverlapTimeRange(timeSame10_11, timeInput10_11.getStartTime(), timeInput10_11.getEndTime());
        // <<< split time 3

        // >>> update mocking
        TimeLog createTimeLog = copyTimeLogFrom(timeInput10_11);
        will(invocation -> {
            createTimeLog.setId(20L);
            createTimeLog.setRecord(updateRecordStub1);

            updateRecordStub1.setId(1L);
            updateRecordStub1.getTimeLogs().add(createTimeLog);

            return updateRecordStub1;
        }).given(recordTimeService).update(1L, updateRecordInput1);
        // <<< update mocking


        //when
        UpdateRecordResponseDto updateRecordResponseDto = recordTimeService.updateWithSideEffect(1L, updateRecordInput1);

        //then
        List<Record> changedRecords = updateRecordResponseDto.getChangedRecords();
        List<Record> deletedRecords = updateRecordResponseDto.getDeletedRecords();
        Record updatedRecord = updateRecordResponseDto.getUpdatedRecord();

        assertThat(changedRecords)
                .hasSize(1)
                .contains(recordStub2);

        assertThat(recordStub2.getTimeLogs())
                .hasSize(3)
                .contains(splitTime0950_1000, splitTime1100_1110, splitTime1100_1130);

        assertThat(deletedRecords)
                .hasSize(1)
                .contains(recordStub3);

        assertThat(recordStub3.getTimeLogs())
                .hasSize(0);

        assertThat(updatedRecord).isEqualTo(updateRecordStub1);
        assertThat(updatedRecord.getTimeLogs())
                .hasSize(1)
                .contains(createTimeLog);

    }


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

