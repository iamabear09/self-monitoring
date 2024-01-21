package monitoring.infra.fake;

import monitoring.domain.Record;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

class FakeTimeRecordsRepositoryTest {

    FakeTimeRecordsRepository fakeTimeRecordsRepository = new FakeTimeRecordsRepository();
    @Test
    @DisplayName("Time 을 저장할 수 있다.")
    void save() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        long recordId = 1L;
        Record record = Record.builder()
                .recordId(recordId)
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
                .record(record)
                .build();

        record.addTimeRecord(time);

        //when
        Record.Time savedTime = fakeTimeRecordsRepository.save(time);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(savedTime.getTimeId()).isPositive();
            softAssertions.assertThat(savedTime.getDate()).isEqualTo(date);
            softAssertions.assertThat(savedTime.getStartTime()).isEqualTo(startTime);
            softAssertions.assertThat(savedTime.getDurationMinutes()).isEqualTo(durationMinutes);
            softAssertions.assertThat(savedTime.getRecord()).isEqualTo(record);
        });

        assertThat(record.getTimeRecords())
                .hasSize(1)
                .doesNotContain(time)
                .contains(savedTime);
    }

    @Test
    @DisplayName("Time 을 조회 할 수 있다.")
    void find() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        long recordId = 1L;
        Record record = Record.builder()
                .recordId(recordId)
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
                .record(record)
                .build();

        record.addTimeRecord(time);
        Record.Time savedTime = fakeTimeRecordsRepository.save(time);

        //when
        Record.Time findTime = fakeTimeRecordsRepository.findById(savedTime.getTimeId()).get();

        //then
        assertThat(findTime).isEqualTo(savedTime);
    }

    @Test
    @DisplayName("Time 을 업데이트 할 수 있다.")
    void update() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        long recordId = 1L;
        Record record = Record.builder()
                .recordId(recordId)
                .action(action)
                .memo(memo)
                .build();

        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(10, 10);
        int durationMinutes = 30;
        Record.Time oldTime = Record.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .record(record)
                .build();

        record.addTimeRecord(oldTime);
        Record.Time savedTime = fakeTimeRecordsRepository.save(oldTime);


        LocalDate updateDate = LocalDate.of(2024, 5, 5);
        LocalTime updateStartTime = LocalTime.of(3, 20);
        int updateDurationMinutes = 50;
        Record.Time updateTimeData = Record.Time.builder()
                .timeId(savedTime.getTimeId())
                .date(updateDate)
                .startTime(updateStartTime)
                .durationMinutes(updateDurationMinutes)
                .record(record)
                .build();

        //when
        Record.Time updatedTime = fakeTimeRecordsRepository.update(updateTimeData);

        //then
        assertThat(updatedTime.getTimeId()).isEqualTo(savedTime.getTimeId());
        assertThat(updatedTime.getDate()).isEqualTo(updateDate);
        assertThat(updatedTime.getStartTime()).isEqualTo(updateStartTime);
        assertThat(updatedTime.getDurationMinutes()).isEqualTo(updateDurationMinutes);
        assertThat(updatedTime.getRecord()).isEqualTo(record);

        assertThat(record.getTimeRecords())
                .hasSize(1)
                .doesNotContain(oldTime)
                .contains(updatedTime);
    }

}