package monitoring.repository;

import monitoring.domain.Record;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ActiveProfiles("dev")
@DataJpaTest
class TimeRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    TimeRepository timeRepository;

    @Test
    @DisplayName("Time 을 record 의 action 으로 검색할 수 있다.")
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

        testEntityManager.persist(record1);
        testEntityManager.persist(record2);
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

    @Test
    @DisplayName("Time 을 Time Range 로 검색할 수 있다.")
    void searchOverlapTimes() {
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

        LocalDate date8 = LocalDate.of(2024, 1, 1);
        LocalTime startTime8 = LocalTime.of(16, 1);
        int durationMinutes8 = 20;
        Record.Time time8 = Record.Time.builder()
                .date(date8)
                .startTime(startTime8)
                .durationMinutes(durationMinutes8)
                .build();

        record1.addTime(time1);
        record1.addTime(time2);

        record2.addTime(time3);
        record2.addTime(time4);
        record2.addTime(time5);
        record2.addTime(time6);

        record3.addTime(time7);
        record3.addTime(time8);

        testEntityManager.persist(record1);
        testEntityManager.persist(record2);
        testEntityManager.persist(record3);

        testEntityManager.flush();
        testEntityManager.clear();

        LocalDate updateDate = LocalDate.of(2024, 1, 1);
        LocalTime updateStartTime = LocalTime.of(15, 0);
        int updateDurationMinutes = 60;
        Record.Time updateTimeData = Record.Time.builder()
                .date(updateDate)
                .startTime(updateStartTime)
                .durationMinutes(updateDurationMinutes)
                .build();

        //when
        List<Record.Time> overlappingTimes = timeRepository.searchOverlappingTimesWithRecord(updateTimeData.getDate(), updateTimeData.getStartTime(), updateTimeData.getEndTime());

        //then
        Assertions.assertThat(overlappingTimes)
                .hasSize(5)
                .contains(time1, time2, time3, time4, time7);
    }

}
