package monitoring.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

class RecordTest {
    
    @Test
    @DisplayName("Record(No id)와 TimeRecord(No record & No id)를 통해 온전한 Record 를 생성할 수 있다.")
    void createTime() {
        //given

        String action = "공부";
        String memo = "코딩테스트";
        Record inputRecord = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(10, 10);
        int durationMinutes = 30;
        Record.Time inputTime = Record.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build();
        
        //when
        Long recordId = 1L;
        Long timeId = 1L;
        Record savedRecord = Record.from(recordId, inputRecord);
        Record.Time savedTime = Record.Time.from(timeId, savedRecord, inputTime);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedRecord.getRecordId()).isEqualTo(recordId);
            softly.assertThat(savedRecord.getAction()).isEqualTo(action);
            softly.assertThat(savedRecord.getMemo()).isEqualTo(memo);
        });
        assertThat(savedTime.getRecord()).isEqualTo(savedRecord);

        assertThatList(savedRecord.getTimeRecords())
                .hasSize(1)
                .contains(savedTime);
    }

}