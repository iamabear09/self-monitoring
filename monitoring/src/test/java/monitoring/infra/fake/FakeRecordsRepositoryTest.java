package monitoring.infra.fake;

import monitoring.domain.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FakeRecordsRepositoryTest {

    FakeRecordsRepository fakeRecordsRepository = new FakeRecordsRepository();

    @BeforeEach
    void clearDB() {
        fakeRecordsRepository.clear();
    }

    @Test
    @DisplayName("Record 를 저장할 수 있다. Record 저장 시 새로운 객체가 반환 된다.")
    void saveRecord_NormalCase() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        //when
        Record saveRecord = fakeRecordsRepository.save(record);

        //then
        assertSoftly(softly -> {
            softly.assertThat(saveRecord.getRecordId()).isNotNull();
            softly.assertThat(saveRecord.getAction()).isEqualTo(action);
            softly.assertThat(saveRecord.getMemo()).isEqualTo(memo);
        });
    }

    @Test
    @DisplayName("Record 저장 시 반환된 객체는 TimeRecord 을 가지고 있지 않다.")
    void saveRecord_TimeNotSaved() {
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

        record.addTimeRecord(time);

        //when
        Record saveRecord = fakeRecordsRepository.save(record);

        //then
        assertThat(saveRecord.getTimeRecords()).isEmpty();
    }

    @Test
    @DisplayName("Record 를 업데이트시 새로운 객체가 반환된다. Time Record 는 업데이트 하지 못한다. 새로운 Record 는 기존 Record 의 Time 값을 그대로 가지고 있다.")
    void updateRecord() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record recordData = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        Record oldRecord = fakeRecordsRepository.save(recordData);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time time1 = Record.Time.builder()
                .timeId(1L)
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build();

        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(10, 10);
        int durationMinutes2 = 30;
        Record.Time time2 = Record.Time.builder()
                .timeId(2L)
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build();

        oldRecord.addTimeRecords(Set.of(time1, time2));

        String updateAction = "헬스";
        String updateMemo = "벤치프레스";
        Record updateRecord = Record.builder()
                .recordId(oldRecord.getRecordId())
                .action(updateAction)
                .memo(updateMemo)
                .build();

        //when
        Record updatedRecord = fakeRecordsRepository.update(updateRecord);
        Record findRecord = fakeRecordsRepository.findById(oldRecord.getRecordId()).get();

        //then
        assertThat(findRecord).isEqualTo(updatedRecord);

        assertSoftly(softly -> {
            softly.assertThat(findRecord.getAction()).isEqualTo(updateAction);
            softly.assertThat(findRecord.getMemo()).isEqualTo(updateMemo);
        });

        assertThat(findRecord.getTimeRecords())
                .containsExactly(time1, time2);

        assertThat(oldRecord.getTimeRecords())
                .isEmpty();
    }
}


