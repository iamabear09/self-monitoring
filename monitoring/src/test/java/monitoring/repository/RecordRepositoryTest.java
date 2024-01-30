package monitoring.repository;

import jakarta.persistence.EntityManager;
import monitoring.domain.Record;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

@ActiveProfiles("dev")
@DataJpaTest
class RecordRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    private RecordRepository recordRepository;

    @Test
    @DisplayName("Record 를 Time 과 함께 저장할 수 있다.")
    void save() {
        //given
        String action = "운동";
        String memo = "헬스장";
        Record record = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(11, 11);
        int durationMinutes1 = 30;
        Record.Time time1 = Record.Time.builder()
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build();

        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(1, 30);
        int durationMinutes2 = 30;
        Record.Time time2 = Record.Time.builder()
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build();

        record.addTime(time1);
        record.addTime(time2);

        //when
        Record savedRecord = recordRepository.save(record);

        //then
        Assertions.assertThat(savedRecord).isEqualTo(record);
        Assertions.assertThat(savedRecord.getId()).isPositive();
        Assertions.assertThat(savedRecord.getTimeRecords())
                .hasSize(2)
                .extracting("id")
                .isNotNull();
    }

    @Test
    @DisplayName("Record 를 TimeRecord 와 함께 가져온다.")
    void findByIdWithTimes() {

        //given
        String action1 = "운동";
        String memo1 = "헬스장";
        Record record1 = Record.builder()
                .action(action1)
                .memo(memo1)
                .build();

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(11, 11);
        int durationMinutes1 = 30;
        Record.Time time1 = Record.Time.builder()
                .date(date1)
                .startTime(startTime1)
                .durationMinutes(durationMinutes1)
                .build();

        LocalDate date2 = LocalDate.of(2024, 1, 2);
        LocalTime startTime2 = LocalTime.of(1, 30);
        int durationMinutes2 = 30;
        Record.Time time2 = Record.Time.builder()
                .date(date2)
                .startTime(startTime2)
                .durationMinutes(durationMinutes2)
                .build();


        LocalDate date3 = LocalDate.of(2024, 1, 2);
        LocalTime startTime3 = LocalTime.of(5, 30);
        int durationMinutes3 = 30;
        Record.Time time3 = Record.Time.builder()
                .date(date3)
                .startTime(startTime3)
                .durationMinutes(durationMinutes3)
                .build();


        record1.addTime(time1);
        record1.addTime(time2);
        record1.addTime(time3);

        testEntityManager.persist(record1);
        testEntityManager.persist(time1);
        testEntityManager.persist(time2);
        testEntityManager.persist(time3);

        testEntityManager.flush();
        testEntityManager.clear();

        //when
        Record findRecord = recordRepository.findByIdWithTimes(record1.getId()).get();

        //then
        Assertions.assertThat(findRecord).isEqualTo(record1);
        Assertions.assertThat(findRecord.getTimeRecords())
                .hasSize(3)
                .containsExactly(time1, time2, time3);
    }

}

