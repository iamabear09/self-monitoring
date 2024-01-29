package monitoring.service;

import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("dev")
@DataJpaTest
class RecordServiceTest {

    @TestConfiguration
    static class ServiceConfig {
        @Bean
        public RecordService recordService(RecordRepository repository, TimeRepository timeRepository) {
            return new RecordService(repository, timeRepository);
        }
    }

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    TimeRepository timeRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    RecordService recordService;

    @Test
    @DisplayName("Record 를 업데이트 할 수 있다. 이 때, TimeRecords 가 null 이 아닌 경우 모두 삭제되었다가 새로 생성된다.")
    void update() {
        //given
        // >>> make record data
        String action1 = "운동";
        String memo1 = "헬스장";
        Record record = Record.builder()
                .action(action1)
                .memo(memo1)
                .build();

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(15, 10);
        int durationMinutes1 = 30;
        Record.Time time1 = Record.Time.builder()
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build();

        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(16, 10);
        int durationMinutes2 = 30;
        Record.Time time2 = Record.Time.builder()
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build();

        record.addTime(time1);
        record.addTime(time2);

        testEntityManager.persist(record);
        testEntityManager.flush();
        testEntityManager.clear();
        // <<< make record data


        // <<< make record data for update
        String action2 = "공부";
        String memo2 = "프로젝트";
        Record updateRecordData = Record.builder()
                .action(action2)
                .memo(memo2)
                .build();

        LocalDate date3 = LocalDate.of(2024, 1, 1);
        LocalTime startTime3 = LocalTime.of(7, 10);
        int durationMinutes3 = 30;
        Record.Time time3 = Record.Time.builder()
                .date(date3)
                .startTime(startTime3)
                .durationMinutes(durationMinutes3)
                .build();

        LocalDate date4 = LocalDate.of(2024, 1, 1);
        LocalTime startTime4 = LocalTime.of(14, 40);
        int durationMinutes4 = 30;
        Record.Time time4 = Record.Time.builder()
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .build();

        LocalDate date5 = LocalDate.of(2024, 1, 2);
        LocalTime startTime5 = LocalTime.of(14, 40);
        int durationMinutes5 = 30;
        Record.Time time5 = Record.Time.builder()
                .date(date5)
                .startTime(startTime5)
                .durationMinutes(durationMinutes5)
                .build();

        updateRecordData.addTime(time3);
        updateRecordData.addTime(time4);
        updateRecordData.addTime(time5);
        // <<< make record data for update

        //when
        recordService.update(record.getId(), updateRecordData);

        testEntityManager.flush();
        testEntityManager.clear();

        Record updatedRecord = recordRepository.findById(record.getId()).get();

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(updatedRecord.getAction()).isEqualTo(updateRecordData.getAction());
            softAssertions.assertThat(updatedRecord.getMemo()).isEqualTo(updateRecordData.getMemo());
        });

        Assertions.assertThat(updatedRecord.getTimeRecords())
                .hasSize(3)
                .extracting("id")
                .isNotNull();

        Assertions.assertThat(updatedRecord.getTimeRecords())
                .hasSize(3)
                .extracting("date", "startTime", "durationMinutes")
                .contains(
                        tuple(date3, startTime3, durationMinutes3),
                        tuple(date4, startTime4, durationMinutes4),
                        tuple(date5, startTime5, durationMinutes5)
                );

    }

    @Test
    @DisplayName("Record 를 삭제할 수 있다.")
    void delete() {
        //given
        // >>> make record data
        String action1 = "운동";
        String memo1 = "헬스장";
        Record record = Record.builder()
                .action(action1)
                .memo(memo1)
                .build();

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(15, 10);
        int durationMinutes1 = 30;
        Record.Time time1 = Record.Time.builder()
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build();

        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(16, 10);
        int durationMinutes2 = 30;
        Record.Time time2 = Record.Time.builder()
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build();

        record.addTime(time1);
        record.addTime(time2);

        Record savedRecord = testEntityManager.persist(record);
        testEntityManager.flush();
        testEntityManager.clear();
        // <<< make record data

        //when
        recordService.delete(savedRecord.getId());

        //then
        Assertions.assertThat(recordRepository.findById(record.getId()).isEmpty()).isTrue();
        Assertions.assertThat(timeRepository.findById(time1.getId()).isEmpty()).isTrue();
        Assertions.assertThat(timeRepository.findById(time2.getId()).isEmpty()).isTrue();

    }

}

