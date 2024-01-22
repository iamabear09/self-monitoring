package monitoring.infra.fake;

import monitoring.domain.Record;
import monitoring.service.RecordsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.groups.Tuple.tuple;

class FakeRecordsRepositoryTest {

    RecordsRepository recordsRepository = new FakeRecordsRepository(new FakeTimeRecordsRepository());

    @Test
    @DisplayName("Record 를 저장할 수 있다.")
    void save() {
        //given

        // >>> create Record without id
        String action = "공부";
        String memo = "코딩테스트";
        Record recordWithoutId = new Record(null, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time timeWithoutId1 = new Record.Time(null, date1, startTime1, durationMinutes1, recordWithoutId);

        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(5, 34);
        int durationMinutes2 = 120;
        Record.Time timeWithoutId2 = new Record.Time(null, date2, startTime2, durationMinutes2, recordWithoutId);
        // <<< create Record without id


        //when
        Record savedRecord = recordsRepository.save(recordWithoutId);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(savedRecord.getRecordId()).isPositive();
            softAssertions.assertThat(savedRecord.getAction()).isEqualTo(action);
            softAssertions.assertThat(savedRecord.getMemo()).isEqualTo(memo);
        });

        assertThat(savedRecord.getTimeRecords())
                .hasSize(2)
                .extracting("timeId")
                .isNotNull();

        assertThat(savedRecord.getTimeRecords())
                .extracting("date", "startTime", "durationMinutes")
                .contains(
                        tuple(date1, startTime1, durationMinutes1),
                        tuple(date2, startTime2, durationMinutes2)
                );
    }


    @Test
    @DisplayName("Record 를 업데이트 할 수 있다.")
    void update() {
        //given

        // >>> create Record without id
        String action = "공부";
        String memo = "코딩테스트";
        Record recordWithoutId = new Record(null, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time timeWithoutId1 = new Record.Time(null, date1, startTime1, durationMinutes1, recordWithoutId);

        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(5, 34);
        int durationMinutes2 = 120;
        Record.Time timeWithoutId2 = new Record.Time(null, date2, startTime2, durationMinutes2, recordWithoutId);
        // <<< create Record without id

        // >>> save Record and get Time
        Record oldRecord = recordsRepository.save(recordWithoutId);

        List<Record.Time> times = oldRecord.getTimeRecords()
                .stream()
                .toList();
        Record.Time time = times.get(0);
        // <<< save Record and get Time

        // >>> create Record for update
        String updateAction = "공부";
        String updateMemo = "코딩테스트";
        Record updateRecord = new Record(oldRecord.getRecordId(), updateAction, updateMemo);

        LocalDate updateDate = LocalDate.of(2024, 1, 2);
        LocalTime updateStartTime = LocalTime.of(5, 34);
        int updateDurationMinutes = 120;
        Record.Time updatedTime = new Record.Time(time.getTimeId(), updateDate, updateStartTime, updateDurationMinutes, updateRecord);
        // <<< create Record for update


        //when
        recordsRepository.save(updateRecord);
        Record findRecord = recordsRepository.findById(oldRecord.getRecordId()).get();

        //then
        assertThat(findRecord).isEqualTo(updateRecord);

        assertThat(findRecord.getTimeRecords())
                .hasSize(1)
                .contains(updatedTime);

        assertThat(findRecord.getTimeRecords())
                .extracting("date", "startTime", "durationMinutes")
                .contains(
                        tuple(updateDate, updateStartTime, updateDurationMinutes)
                );
    }

}

