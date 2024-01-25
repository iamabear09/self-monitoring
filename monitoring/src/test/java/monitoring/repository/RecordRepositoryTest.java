package monitoring.repository;

import jakarta.persistence.EntityManager;
import monitoring.domain.Record;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@ActiveProfiles("dev")
@DataJpaTest(showSql = false)
class RecordRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    TimeRepository timeRepository;

    @Test
    @DisplayName("학습 - Record 저장 시 Record 에 포함된 Time 은 어떻게 되는 거지?")
    @Transactional
    @Rollback(value = false)
    void study_save_record() {

        //given
        Record record = new Record(null, "action", "memo");
        Record.Time time1 = new Record.Time(null, LocalDate.now(), LocalTime.now(), 30, record);
        Record.Time time2 = new Record.Time(null, LocalDate.now(), LocalTime.now(), 30, record);

        System.out.println("record = " + record);
        System.out.println("time1 = " + time1);

        //when
        recordRepository.save(record);
        timeRepository.saveAll(record.getTimeRecords());

        //then
        System.out.println("record = " + record);
        System.out.println("time1 = " + time1);
        System.out.println("time2 = " + time2);
    }
}