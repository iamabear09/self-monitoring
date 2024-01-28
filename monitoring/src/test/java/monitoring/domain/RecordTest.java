package monitoring.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

class RecordTest {

    @Test
    @DisplayName("Record 와 Time 을 생성시 연관관계가 설정된다.")
    void addTime() {

        //given
        String action = "운동";
        Record record = Record.builder()
                .action(action)
                .memo("헬스장")
                .build();

        Record.Time time = Record.Time.builder()
                .date(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(11, 11))
                .durationMinutes(30)
                .build();

        //when
        record.addTime(time);

        //then

    }

}