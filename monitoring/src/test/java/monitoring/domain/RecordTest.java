package monitoring.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecordTest {

    @Test
    @DisplayName("Time 생성 시 Record 와 양방향 연결된다.")
    void createTime() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = new Record(null, action, memo);

        //when
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time time = new Record.Time(null, date1, startTime1, durationMinutes1, record);

        //then
        assertThat(record.getTimeRecords())
                .hasSize(1)
                .containsExactly(time);
    }

    @Test
    @DisplayName("Record 에 이미 존재하는 Time 내용이면, Record에 저장 되지 않는다.")
    void createTime_SameTime() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = new Record(null, action, memo);

        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(10, 10);
        int durationMinutes = 30;
        Record.Time time1 = new Record.Time(null, date, startTime, durationMinutes, record);

        //when
        Record.Time time2 = new Record.Time(null, date, startTime, durationMinutes, record);

        //then
        assertThat(record.getTimeRecords())
                .hasSize(1)
                .containsExactly(time1);
    }

    @Test
    @DisplayName("Time 생성 시 Record 는 필수이다.")
    void createTime_Without_Record() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = new Record(null, action, memo);

        //when
        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(10, 10);
        int durationMinutes = 30;

        //then
        assertThatThrownBy(() -> {
            new Record.Time(null, date, startTime, durationMinutes, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("현재 Time 을 통해 Id가 있는 Time 을 생성 시, Record 와 연결된 Time 이 새롭게 생성되는 Time 으로 변경된다.")
    void toTime() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = new Record(null, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time timeNoId = new Record.Time(null, date1, startTime1, durationMinutes1, record);

        //when
        Record.Time timeWithId = timeNoId.toTimeWith(1L);

        //then
        assertThat(record.getTimeRecords())
                .hasSize(1)
                .containsExactly(timeWithId);
    }

    @Test
    @DisplayName("현재 Record 를 가지고 Id가 있는 Record 를 생성할 수 있다.")
    void toRecord() {

        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record recordWithoutId = new Record(null, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time time1 = new Record.Time(null, date1, startTime1, durationMinutes1, recordWithoutId);

        LocalDate date2 = LocalDate.of(2024, 10, 4);
        LocalTime startTime2 = LocalTime.of(10, 10);
        int durationMinutes2 = 30;
        Record.Time time2 = new Record.Time(null, date2, startTime2, durationMinutes2, recordWithoutId);

        //when
        Long id = 1L;
        Record record = recordWithoutId.toRecordWith(id);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(record.getRecordId()).isPositive();
            softAssertions.assertThat(record.getAction()).isEqualTo(recordWithoutId.getAction());
            softAssertions.assertThat(record.getMemo()).isEqualTo(recordWithoutId.getMemo());
        });

        assertThat(record.getTimeRecords())
                .hasSize(2)
                .containsExactly(time1, time2);
    }

    @Test
    @DisplayName("Record 에 검색 조건을 입력해서 해당 조건에 부합하는지 확인할 수 있다.")
    void search_actionAndMemo() {
        //given
        String action1 = "공부";
        String memo1 = "코딩테스트";
        Record record1 = new Record(null, action1, memo1);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(9, 30);
        int durationMinutes1 = 30;
        Record.Time time1 = new Record.Time(null, date1, startTime1, durationMinutes1, record1);

        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(10, 0);
        int durationMinutes2 = 20;
        Record.Time time2 = new Record.Time(null, date2, startTime2, durationMinutes2, record1);

        //when
        boolean nullContent = time1.hasSameContentAs(null, null);
        boolean sameContent = time1.hasSameContentAs(action1, null);
        boolean partContent1 = time1.hasSameContentAs("공", null);
        boolean partContent2 = time1.hasSameContentAs("부", null);
        boolean differentContent = time1.hasSameContentAs("헬스", null);

        //then
        assertThat(nullContent).isTrue();
        assertThat(sameContent).isTrue();
        assertThat(partContent1).isTrue();
        assertThat(partContent2).isTrue();
        assertThat(differentContent).isFalse();
    }

}

