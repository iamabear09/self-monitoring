package monitoring.infra.fake;

import monitoring.domain.Record;
import monitoring.service.TimeRecordsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class FakeTimeRecordsRepositoryTest {

    TimeRecordsRepository timeRecordsRepository = new FakeTimeRecordsRepository();

    @Test
    @DisplayName("Time 을 저장할 수 있다.")
    void save_Time() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = new Record(null, action, memo);

        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(10, 10);
        int durationMinutes = 30;
        Record.Time time = new Record.Time(null, date, startTime, durationMinutes, record);

        //when
        Record.Time savedTime = timeRecordsRepository.save(time);
        Record.Time findTime = timeRecordsRepository.findById(savedTime.getTimeId()).get();

        //then
        assertThat(savedTime).isEqualTo(findTime);

    }

    @Test
    @DisplayName("Time 을 업데이트 할 수 있다.")
    void save_update() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = new Record(null, action, memo);

        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime startTime = LocalTime.of(10, 10);
        int durationMinutes = 30;
        Record.Time time = new Record.Time(1L, date, startTime, durationMinutes, record);

        Record.Time savedTime = timeRecordsRepository.save(time);


        LocalDate updateData = LocalDate.of(2024, 5, 2);
        LocalTime updateStartTime = LocalTime.of(10, 30);
        int updateDurationMinutes = 50;
        Record.Time updateTime = new Record.Time(1L, updateData, updateStartTime, updateDurationMinutes, record);

        //when
        Record.Time updatedTime = timeRecordsRepository.save(updateTime);
        Record.Time findTime = timeRecordsRepository.findById(savedTime.getTimeId()).get();

        //then
        assertThat(findTime).isSameAs(updatedTime);
    }

    @Test
    @DisplayName("여러 Time 을 한번에 저장할 수 있다.")
    void saveAll() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = new Record(null, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(10, 10);
        int durationMinutes1 = 30;
        Record.Time time1 = new Record.Time(null, date1, startTime1, durationMinutes1, record);

        LocalDate date2 = LocalDate.of(2024, 5, 19);
        LocalTime startTime2 = LocalTime.of(3, 11);
        int durationMinutes2 = 60;
        Record.Time time2 = new Record.Time(null, date2, startTime2, durationMinutes2, record);

        //when
        Set<Record.Time> times = timeRecordsRepository.saveAll(Set.of(time1, time2));

        //then
        List<Record.Time> findTimes = times.stream()
                .map(Record.Time::getTimeId)
                .map(i -> timeRecordsRepository.findById(i).get())
                .toList();

        assertThat(findTimes)
                .extracting("timeId")
                .isNotNull();

        assertThat(findTimes)
                .hasSize(2)
                .extracting("date", "startTime", "durationMinutes")
                .contains(
                        tuple(date1, startTime1, durationMinutes1),
                        tuple(date2, startTime2, durationMinutes2)
                );
    }
}

