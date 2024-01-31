package monitoring.service;

import monitoring.domain.Record;
import monitoring.domain.TimeLog;
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
                .action(action1)
                .memo(memo1)
                .build();

        recordStub2 = Record.builder()
                .id(2L)
                .action(action2)
                .memo(memo2)
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
                .record(recordStub1)
                .build();

        timeStub2 = TimeLog.builder()
                .id(2L)
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(30L))
                .record(recordStub1)
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
                .record(recordStub1)
                .build();

        timeMockInput4 = TimeLog.builder()
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(20L))
                .endTime(LocalTime.of(11,0))
                .record(recordStub1)
                .build();

        timeStub3 = TimeLog.builder()
                .id(3L)
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(20L))
                .record(recordStub1)
                .build();

        timeStub4 = TimeLog.builder()
                .id(4L)
                .date(LocalDate.of(2024, 1, 2))
                .startTime(LocalTime.of(10, 40))
                .durationMinutes(Duration.ofMinutes(20L))
                .endTime(LocalTime.of(11,0))
                .record(recordStub1)
                .build();
    }
    @Test
    @DisplayName(" Record 와 TimeLog 를 저장 할 수 있다.")
    void save() {

        //given
        recordInput1.setTimeLogs(List.of(timeInput1, timeInput2));

        given(recordService.save(eq(recordInput1))).willReturn(recordStub1);
        given(timeLogService.save(eq(recordStub1), eq(List.of(timeInput1, timeInput2)))).willReturn(List.of(timeStub1, timeStub2));

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
        recordInput1.setTimeLogs(List.of(timeInput1, timeInput2));
        given(recordService.update(1L, recordInput1)).willReturn(recordStub1);
        given(timeLogService.save(eq(recordStub1), eq(List.of(timeInput1, timeInput2)))).willReturn(List.of(timeStub1, timeStub2));

        //when
        Record updateRecord = recordTimeService.updateAllowingOverlap(1L, recordInput1);

        //then
        verify(timeLogService).deleteByRecordId(eq(1L));

        assertThat(updateRecord).isEqualTo(recordStub1);
        assertThat(updateRecord.getTimeLogs())
                .hasSize(2)
                .contains(timeStub1, timeStub2);

    }
    private Record copyRecordFrom(Record record) {
        if (record == null) {
            return null;
        }

        return Record.builder()
                .id(record.getId())
                .action(record.getAction())
                .memo(record.getMemo())
                .timeLogs(copyTimeLogsFrom(record.getTimeLogs()))
                .build();
    }

    private List<TimeLog> copyTimeLogsFrom(List<TimeLog> timeLogs) {
        if (timeLogs == null) {
            return null;
        }

        List<TimeLog> copyTimeLogs = timeLogs.stream()
                .map(t -> TimeLog.builder()
                        .id(t.getId())
                        .date(t.getDate())
                        .startTime(t.getStartTime())
                        .endTime(t.getEndTime())
                        .durationMinutes(t.getDurationMinutes())
                        .record(t.getRecord())
                        .build()
                ).toList();

        return timeLogs;
    }

}

