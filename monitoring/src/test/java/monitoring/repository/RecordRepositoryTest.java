package monitoring.repository;

import jakarta.persistence.EntityManager;
import monitoring.domain.Record;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(11, 11);
        int durationMinutes = 30;
        Record.Time time = Record.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build();

        record.addTime(time);

        //when
        Record savedRecord = recordRepository.save(record);

        //then
        Assertions.assertThat(savedRecord).isEqualTo(record);
        Assertions.assertThat(savedRecord.getId()).isPositive();
        Assertions.assertThat(savedRecord.getTimeRecords())
                .hasSize(1)
                .extracting("id")
                .isNotNull();
    }

    @Test
    @DisplayName("Record 를 TimeRecord 와 함께 가져온다.")
    void findByIdWithTimes() {

        //given
        String action = "운동";
        String memo = "헬스장";
        Record record = Record.builder()
                .action(action)
                .memo(memo)
                .build();

        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(11, 11);
        int durationMinutes = 30;
        Record.Time time = Record.Time.builder()
                .date(date)
                .startTime(startTime)
                .durationMinutes(durationMinutes)
                .build();

        record.addTime(time);

        testEntityManager.persist(record);
        testEntityManager.persist(time);

        testEntityManager.flush();
        testEntityManager.clear();

        //when
        Record findRecord = recordRepository.findByIdWithTimes(record.getId()).get();

        //then
        Assertions.assertThat(findRecord).isEqualTo(record);
        Assertions.assertThat(findRecord.getTimeRecords())
                .hasSize(1)
                .containsExactly(time);
    }

}

