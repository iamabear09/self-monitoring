package monitoring.repository;

import jakarta.persistence.EntityManager;
import monitoring.domain.Record;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ActiveProfiles("dev")
@DataJpaTest
class TimeRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    TimeRepository timeRepository;


    @Test
    @DisplayName("Time 을 record의 action 으로 검색할 수 있다.")
    void search() {
        //given
        // >>> make record data
        String action1 = "운동";
        String memo1 = "헬스장";
        Record record1 = Record.builder()
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

        String action2 = "공부";
        String memo2 = "프로젝트";
        Record record2 = Record.builder()
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

        record1.addTime(time1);
        record1.addTime(time2);

        record2.addTime(time3);
        record2.addTime(time4);
        record2.addTime(time5);

        entityManager.persist(record1);
        entityManager.persist(record2);
        // <<< make record data

        String actionCond = "공부";
        TimeSearchCond condOnlyAction = TimeSearchCond.builder()
                .action(actionCond)
                .build();

        TimeSearchCond condDateAndTime = TimeSearchCond.builder()
                .date(LocalDate.of(2024,1,2))
                .time(LocalTime.of(15, 10))
                .build();

        //when
        List<Record.Time> timesByAction = timeRepository.search(condOnlyAction);
        List<Record.Time> timesByDateAndTime = timeRepository.search(condDateAndTime);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(timesByAction)
                    .hasSize(3)
                    .containsExactly(time3, time4, time5);

            softAssertions.assertThat(timesByDateAndTime)
                    .hasSize(1)
                    .contains(time5);
        });
    }



}