package monitoring.service;

import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import monitoring.repository.TimeRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
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
    void updateAllowingTimeOverlap() {
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
        recordService.updateAllowingTimeOverlap(record.getId(), updateRecordData);

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

    @Test
    @DisplayName("Record 의 time 을 수정하는 경우, 해당 Time 과 중복되는 Time 을 모두 제거한다.")
    void updateNotAllowingTimeOverlap() {
        //given

        // >>> make record data
        String action1 = "운동";
        String memo1 = "헬스장";
        Record record1 = Record.builder()
                .action(action1)
                .memo(memo1)
                .build();

        //14:50 ~ 16:10 → 14:50 ~ 15:00 , 16:00 ~ 16:10 변경 예정
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(14, 50);
        int durationMinutes1 = 80;
        Record.Time time1 = Record.Time.builder()
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build();

        //15:20 ~ 15:40  → 삭제 예정
        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(15, 20);
        int durationMinutes2 = 20;
        Record.Time time2 = Record.Time.builder()
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build();

        String action2 = "공부";
        String memo2 = "프로젝트";
        Record record2 = Record.builder()
                .action(action2)
                .memo(memo2)
                .build();

        //14:50 ~ 15:10 → 14:50 ~ 15:00 변경 예정
        LocalDate date3 = LocalDate.of(2024, 1, 1);
        LocalTime startTime3 = LocalTime.of(14, 50);
        int durationMinutes3 = 20;
        Record.Time time3 = Record.Time.builder()
                .date(date3)
                .startTime(startTime3)
                .durationMinutes(durationMinutes3)
                .build();

        //15:50 ~ 16:10 → 16:00 ~ 16:10 변경 예정
        LocalDate date4 = LocalDate.of(2024, 1, 1);
        LocalTime startTime4 = LocalTime.of(15, 50);
        int durationMinutes4 = 20;
        Record.Time time4 = Record.Time.builder()
                .date(date4)
                .startTime(startTime4)
                .durationMinutes(durationMinutes4)
                .build();

        //16:00 ~ 16:30 → 그대로 예정
        LocalDate date5 = LocalDate.of(2024, 1, 1);
        LocalTime startTime5 = LocalTime.of(16, 0);
        int durationMinutes5 = 30;
        Record.Time time5 = Record.Time.builder()
                .date(date5)
                .startTime(startTime5)
                .durationMinutes(durationMinutes5)
                .build();

        //14:30 ~ 15:00 → 그대로 예정
        LocalDate date6 = LocalDate.of(2024, 1, 1);
        LocalTime startTime6 = LocalTime.of(14, 30);
        int durationMinutes6 = 30;
        Record.Time time6 = Record.Time.builder()
                .date(date6)
                .startTime(startTime6)
                .durationMinutes(durationMinutes6)
                .build();

        //삭제 예정
        String action3 = "운동";
        String memo3 = "헬스장";
        Record record3 = Record.builder()
                .action(action3)
                .memo(memo3)
                .build();

        //삭제 예정
        LocalDate date7 = LocalDate.of(2024, 1, 1);
        LocalTime startTime7 = LocalTime.of(15, 20);
        int durationMinutes7 = 20;
        Record.Time time7 = Record.Time.builder()
                .date(date7)
                .startTime(startTime7)
                .durationMinutes(durationMinutes7)
                .build();

        //삭제 예정
        String action4 = "수정예정";
        String memo4 = "수정값";
        Record updateRecord = Record.builder()
                .action(action4)
                .memo(memo4)
                .build();

        record1.addTime(time1);
        record1.addTime(time2);

        record2.addTime(time3);
        record2.addTime(time4);
        record2.addTime(time5);
        record2.addTime(time6);

        record3.addTime(time7);

        testEntityManager.persist(record1);
        testEntityManager.persist(record2);
        testEntityManager.persist(record3);
        testEntityManager.persist(updateRecord);

        testEntityManager.flush();
        testEntityManager.clear();


        String actionData = "수정됨";
        String memoData = "수정";
        Record updateRecordData = Record.builder()
                .action(actionData)
                .memo(memoData)
                .build();

        LocalDate updateDate = LocalDate.of(2024, 1, 1);
        LocalTime updateStartTime = LocalTime.of(15, 0);
        int updateDurationMinutes = 60;
        Record.Time updateTimeData = Record.Time.builder()
                .date(updateDate)
                .startTime(updateStartTime)
                .durationMinutes(updateDurationMinutes)
                .build();
        updateRecordData.addTime(updateTimeData);

        //when
        recordService.updateNotAllowingTimeOverlap(updateRecord.getId(), updateRecordData);

        testEntityManager.flush();
        testEntityManager.clear();

        //then
        Record findRecord1 = recordRepository.findByIdWithTimes(record1.getId()).get();
        Record findRecord2 = recordRepository.findByIdWithTimes(record2.getId()).get();
        Record updatedRecord = recordRepository.findByIdWithTimes(updateRecord.getId()).get();
        Assertions.assertThat(recordRepository.findByIdWithTimes(record3.getId()).isEmpty()).isTrue();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(findRecord1.getTimeRecords())
                    .hasSize(2)
                    .extracting("startTime", "endTime")
                    .contains(
                            tuple(LocalTime.of(14, 50), LocalTime.of(15, 0)),
                            tuple(LocalTime.of(16, 0), LocalTime.of(16, 10))
                    );

            softAssertions.assertThat(findRecord2.getTimeRecords())
                    .hasSize(4)
                    .extracting("startTime", "endTime")
                    .contains(
                            tuple(LocalTime.of(14, 30), LocalTime.of(15, 0)),
                            tuple(LocalTime.of(14, 50), LocalTime.of(15, 0)),
                            tuple(LocalTime.of(16, 0), LocalTime.of(16, 10)),
                            tuple(LocalTime.of(16, 0), LocalTime.of(16, 30))
                    );

            softAssertions.assertThat(updatedRecord.getAction()).isEqualTo(updateRecordData.getAction());
            softAssertions.assertThat(updatedRecord.getMemo()).isEqualTo(updateRecordData.getMemo());
            softAssertions.assertThat(updatedRecord.getTimeRecords())
                    .hasSize(1)
                    .contains(updateTimeData);

        });


    }

}

