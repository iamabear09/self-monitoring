package monitoring.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;

class RecordTest {

    @Test
    @DisplayName("Record 에 Time Record 를 추가할 수 있다.")
    void addTimeRecord_Success() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(10, 10);
        int durationMinutes = 30;
        Record.Time time = Record.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build();

        //when
        record.addTimeRecord(time);

        //then
        assertThat(time.getRecord()).isEqualTo(record);
        assertThat(record.getTimeRecords())
                .hasSize(1)
                .contains(time);
    }


    @Test
    @DisplayName("Record 에 Time Record 를 추가 시 Time Record에 다른 Record가 있다면 해당 Record에서 Time Record를 지운다.")
    void addTimeRecord_deletePreRecord() {

        //given
        String action1 = "공부";
        String memo1 = "코딩테스트";
        Record preRecord = Record.builder()
                .action(action1)
                .memo(memo1)
                .build();

        String action2 = "헬스";
        String memo2 = "벤치프레스";
        Record newRecord = Record.builder()
                .action(action2)
                .memo(memo2)
                .build();


        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time time1 = Record.Time.builder()
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build();

        LocalDate date2 = LocalDate.of(2024, 1, 20);
        LocalTime startTime2 = LocalTime.of(4, 10);
        int durationMinutes2 = 40;
        Record.Time time2 = Record.Time.builder()
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build();

        preRecord.addTimeRecords(Set.of(time1, time2));

        //when
        newRecord.addTimeRecords(Set.of(time1, time2));

        //then
        assertThat(time1.getRecord()).isEqualTo(newRecord);
        assertThat(time2.getRecord()).isEqualTo(newRecord);

        assertThat(newRecord.getTimeRecords())
                .hasSize(2)
                .contains(time1, time2);

        assertThat(preRecord.getTimeRecords())
                .isEmpty();
    }

}

