package jhp.monitoring.consumer.service;

import jhp.monitoring.consumer.service.response.UpdateRecordResult;
import jhp.monitoring.domain.Record;
import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.service.RecordService;
import jhp.monitoring.service.TimeLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.will;
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
    Record recordInput1;
    Record recordResult1;
    TimeLog timeInput1;
    TimeLog timeResult1;
    TimeLog timeInput2;
    TimeLog timeResult2;

    Record updateRecordInput1;
    Record updateRecordResult1;

    Record recordInput2;
    Record recordResult2;

    Record recordInput3;
    Record recordResult3;


    //given


    TimeLog timeInput3;
    TimeLog timeInput4;
    TimeLog timeMockInput3;
    TimeLog timeMockInput4;
    TimeLog timeResult4;


    TimeLog timeResult3;

    TimeLog timeResult5;
    TimeLog timeStub6;

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

        String updateRecordId1 = "updateId1";
        String updateAction1 = "밥";
        String updateMemo1 = "고등어";
        updateRecordInput1 = Record.builder()
                .action(updateAction1)
                .memo(updateMemo1)
                .build();
        updateRecordResult1 = Record.builder()
                .action(updateAction1)
                .memo(updateMemo1)
                .build();

        String recordId2 = "id2";
        String action2 = "헬스";
        String memo2 = "벤치프레스";
        recordInput2 = Record.builder()
                .id(recordId2)
                .action(action2)
                .memo(memo2)
                .build();
        recordResult2 = Record.builder()
                .id(recordId2)
                .action(action2)
                .memo(memo2)
                .build();

        String recordId3 = "id3";
        String action3 = "밥";
        String memo3 = "오트밀";
        recordInput3 = Record.builder()
                .id(recordId3)
                .action(action3)
                .memo(memo3)
                .build();
        recordResult3 = Record.builder()
                .id(recordId3)
                .action(action3)
                .memo(memo3)
                .build();



    }

    @BeforeEach
    void initGivenTimeLogs() {

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


        /*
         * timeLogs3
         * 10:30 - 11:30
         */
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

        timeResult3 = TimeLog.builder()
                .id(3L)
                .date(date3)
                .startTime(startTime3)
                .endTime(endTime3)
                .durationMinutes(durationMinutes3)
                .build();



        /*
         * timeLogs4
         * 09:30 - 10:30
         */
        LocalDate date4 = LocalDate.of(2024, 1, 1);
        LocalTime startTime4 = LocalTime.of(9, 30);
        Duration durationMinutes4 = Duration.ofMinutes(60L);
        timeInput4 = TimeLog.builder()
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .build();

        LocalTime endTime4 = LocalTime.of(10, 30);
        timeMockInput4 = TimeLog.builder()
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .endTime(endTime4)
                .build();
        timeResult4 = TimeLog.builder()
                .id(4L)
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .endTime(endTime4)
                .build();


        /*
         * timeLogs5
         * 13:00 - 14:00
         */
        LocalDate date5 = LocalDate.of(2024, 1, 1);
        LocalTime startTime5 = LocalTime.of(9, 30);
        LocalTime endTime5 = LocalTime.of(10, 30);
        Duration durationMinutes5 = Duration.ofMinutes(60L);
        timeResult5 = TimeLog.builder()
                .id(5L)
                .date(date5)
                .startTime(startTime5)
                .endTime(endTime5)
                .durationMinutes(durationMinutes5)
                .build();
    }

    @Test
    @DisplayName(" Record 와 TimeLog 를 저장 할 수 있다.")
    void save() {

        //given
        given(recordService.create(recordInput1)
        ).will(invocation -> {
            return recordResult1;
        });

        given(timeLogService.create(recordResult1, List.of(timeInput1, timeInput2))
        ).will(invocation -> {
                    timeResult1.setRecord(recordResult1);
                    timeResult2.setRecord(recordResult1);

                    return List.of(timeResult1, timeResult2);
                }
        );

        //when
        recordInput1.getTimeLogs()
                .addAll(List.of(timeInput1, timeInput2));

        Record savedRecord = recordTimeService.create(recordInput1);

        //then
        assertThat(savedRecord).isEqualTo(recordResult1);
        assertThat(savedRecord.getTimeLogs())
                .hasSize(2)
                .contains(timeResult1, timeResult2);
    }

    @Test
    @DisplayName(
            "Record 를 update 할 수 있다. " +
            "TimeLog 가 업데이트 되는 경우, 기존 TimeLog 는 모두 삭제되고 새로운 TimeLog 로 변경된다. " +
            "update 시 다른 TimeLog 와 Time 구간의 중복을 허용한다."
    )
    void update() {
        //given
        TimeLog updateTimeLogInput = timeInput2;
        TimeLog updateTimeLogResult = timeResult2;


        given(recordService.update(recordInput1.getId(), updateRecordInput1)
        ).will(invocation -> {
            updateRecordResult1.setId(recordInput1.getId());
            return updateRecordResult1;
        });

        given(timeLogService.create(updateRecordResult1, List.of(updateTimeLogInput)
        )).will(invocation -> {
            return List.of(updateTimeLogResult);
        });

        //when
        updateRecordInput1.setTimeLogs(List.of(updateTimeLogInput));
        Record updateRecord = recordTimeService.update(recordInput1.getId(), updateRecordInput1);

        //then
        verify(timeLogService).deleteByRecordId(recordInput1.getId());

        assertThat(updateRecord).isEqualTo(updateRecordResult1);
        assertThat(updateRecord.getTimeLogs())
                .hasSize(1)
                .contains(updateTimeLogResult)
                .extracting("record")
                .contains(updateRecordResult1);
    }

    @Test
    @DisplayName("Record 를 update 할 수 있다. TimeLog 가 전달되지 않은 경우 TimeLog 는 기존 TimeLog 를 사용한다.")
    void updateWithoutTimeLogs() {
        //given
        TimeLog originTimeLogData = timeInput1;

        given(recordService.update(recordInput1.getId(), updateRecordInput1)
        ).will(invocation -> {
            updateRecordResult1.setId(recordInput1.getId());
            return updateRecordResult1;
        });

        given(timeLogService.getByRecordId(recordInput1.getId())
        ).will(invocation -> {
            return List.of(originTimeLogData);
        });

        //when
        Record updateRecord = recordTimeService.update(recordInput1.getId(), updateRecordInput1);

        //then
        assertThat(updateRecord).isEqualTo(updateRecordResult1);
        assertThat(updateRecord.getTimeLogs())
                .hasSize(1)
                .contains(originTimeLogData)
                .extracting("record")
                .contains(updateRecordResult1);
    }

    @Test
    @DisplayName(
            "Put Update 시 Record 의 TimeLog 가 변경되는 경우" +
            "변경된 TimeLog 의 구간이 다른 Record 의 TimeLog 와 겹칠 수 있다." +
            "이때, 다른 Record 의 TimeLog 에서 변경된 TimeLog 와 겹치는 구간을 제거한다."
    )
    void updateWithRemovingDuplicatedTimeLogs() {

        //given

        // Origin Record
        Record originRecord = recordResult1;
        TimeLog originTimeLog = timeResult5;
        originTimeLog.setRecord(originRecord);
        originRecord.getTimeLogs().add(originTimeLog);

        // To be updated Data
        TimeLog updateTimeInput__1000_1100 = timeInput1;
        TimeLog updateTimeResult__1000_1100 = timeResult1;
        updateTimeInput__1000_1100.setRecord(updateRecordInput1);
        updateRecordInput1.getTimeLogs().add(updateTimeInput__1000_1100);


        // Affected Record 1
        TimeLog time1_AftRecord1__0950_1110 = timeResult2;   // will be 0950-1000 & 1100-1110
        time1_AftRecord1__0950_1110.setStartTime(LocalTime.of(9, 50));
        time1_AftRecord1__0950_1110.setEndTime(LocalTime.of(11, 10));

        TimeLog time2_AftRecord1__1030_1130 = timeResult3;   // will be 1100-1130
        time2_AftRecord1__1030_1130.setStartTime(LocalTime.of(10, 30));
        time2_AftRecord1__1030_1130.setEndTime(LocalTime.of(11, 30));

        Record affectedRecord1 = recordResult2;
        time1_AftRecord1__0950_1110.setRecord(affectedRecord1);
        time2_AftRecord1__1030_1130.setRecord(affectedRecord1);    // record will have 3 timeLogs
        affectedRecord1.getTimeLogs()
                .addAll(List.of(time1_AftRecord1__0950_1110, time2_AftRecord1__1030_1130));

        will(invocation -> {
            return affectedRecord1;
        }).given(recordTimeService).getRecordWithTimeLogs(affectedRecord1.getId());

        // Affected Record 2
        TimeLog originTime__1000_1100__sameRange = timeResult4;
        originTime__1000_1100__sameRange.setStartTime(LocalTime.of(10, 0));
        originTime__1000_1100__sameRange.setEndTime(LocalTime.of(11, 0));

        Record affectedRecord2 = recordResult3;
        originTime__1000_1100__sameRange.setRecord(affectedRecord2);
        affectedRecord2.getTimeLogs().add(originTime__1000_1100__sameRange);

        will(invocation -> {
            return affectedRecord2;
        }).given(recordTimeService).getRecordWithTimeLogs(affectedRecord2.getId());

        // searchOverlappingTimeLogs method mocking
        will(invocation -> {
            return List.of(originTimeLog, time1_AftRecord1__0950_1110, time2_AftRecord1__1030_1130, originTime__1000_1100__sameRange);
        }).given(timeLogService).searchOverlappingTimeLogs(updateTimeInput__1000_1100.getDate(), updateTimeInput__1000_1100.getStartTime(), updateTimeInput__1000_1100.getEndTime());


        // affected record 1 → split time * 2
        TimeLog splitTime__0950_1000 = copyTimeLogFrom(time1_AftRecord1__0950_1110);
        TimeLog splitTime__1100_1110 = copyTimeLogFrom(time1_AftRecord1__0950_1110);
        will(invocation -> {
            splitTime__0950_1000.setId(10L);
            splitTime__0950_1000.setEndTime(LocalTime.of(10, 0));
            splitTime__0950_1000.setDurationMinutes(Duration.ofMinutes(10L));

            splitTime__1100_1110.setId(11L);
            splitTime__1100_1110.setStartTime(LocalTime.of(11, 10));
            splitTime__1100_1110.setDurationMinutes(Duration.ofMinutes(10L));

            return List.of(splitTime__0950_1000, splitTime__1100_1110);
        }).given(timeLogService).splitByRemovingOverlapTimeRange(time1_AftRecord1__0950_1110, updateTimeInput__1000_1100.getStartTime(), updateTimeInput__1000_1100.getEndTime());


        // affected record 2 → split time * 1
        TimeLog splitTime__1100_1130 = copyTimeLogFrom(time2_AftRecord1__1030_1130);
        given(timeLogService.splitByRemovingOverlapTimeRange(time2_AftRecord1__1030_1130, updateTimeInput__1000_1100.getStartTime(), updateTimeInput__1000_1100.getEndTime())
        ).will(invocation -> {

            splitTime__1100_1130.setId(12L);
            splitTime__1100_1130.setStartTime(LocalTime.of(11, 0));
            splitTime__1100_1130.setDurationMinutes(Duration.ofMinutes(30L));

            return List.of(splitTime__1100_1130);
        });


        // affected record 3 → split time * 1
        given(timeLogService.splitByRemovingOverlapTimeRange(originTime__1000_1100__sameRange, updateTimeInput__1000_1100.getStartTime(), updateTimeInput__1000_1100.getEndTime())
        ).willReturn(new ArrayList<>());


        // update method mocking
        will(invocation -> {
            updateRecordResult1.setId(originRecord.getId());
            updateTimeResult__1000_1100.setId(30L);
            updateRecordResult1.getTimeLogs().add(updateTimeResult__1000_1100);

            return updateRecordResult1;
        }).given(recordTimeService).update(originRecord.getId(), updateRecordInput1);

        //when
        UpdateRecordResult updateRecordResult = recordTimeService.updateWithRemovingDuplicatedTimeLogs(originRecord.getId(), updateRecordInput1);

        //then
        Set<String> deletedRecordIdSet = updateRecordResult.getDeletedRecordIdSet();
        Set<String> changedRecordIdSet = updateRecordResult.getChangedRecordIdSet();
        Record updatedRecord = updateRecordResult.getUpdatedRecord();

        assertThat(changedRecordIdSet)
                .hasSize(1)
                .contains(affectedRecord1.getId());

        assertThat(affectedRecord1.getTimeLogs())
                .hasSize(3)
                .contains(splitTime__0950_1000, splitTime__1100_1110, splitTime__1100_1130);

        assertThat(deletedRecordIdSet)
                .hasSize(1)
                .contains(affectedRecord2.getId());

        assertThat(affectedRecord2.getTimeLogs())
                .hasSize(0);

        assertThat(updatedRecord).isEqualTo(updateRecordResult1);
        assertThat(updatedRecord.getTimeLogs())
                .hasSize(1)
                .contains(updateTimeResult__1000_1100);
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

