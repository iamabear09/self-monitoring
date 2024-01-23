package monitoring.infra.fake;

import monitoring.domain.Record;
import monitoring.service.RecordsSearchCond;
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
        Record.Time updateTime = new Record.Time(savedTime.getTimeId(), updateData, updateStartTime, updateDurationMinutes, record);

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

    @Test
    @DisplayName("Time 을 날짜와 시간을 통해 검색할 수 있다")
    void searchTime() {
        //given
        String action = "공부";
        String memo = "코딩테스트";
        Record record = new Record(null, action, memo);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(9, 30);
        int durationMinutes1 = 30;
        Record.Time time1 = new Record.Time(null, date1, startTime1, durationMinutes1, record);

        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(10, 0);
        int durationMinutes2 = 20;
        Record.Time time2 = new Record.Time(null, date2, startTime2, durationMinutes2, record);

        LocalDate date3 = LocalDate.of(2024, 1, 2);
        LocalTime startTime3 = LocalTime.of(9, 30);
        int durationMinutes3 = 60;
        Record.Time time3 = new Record.Time(null, date3, startTime3, durationMinutes3, record);

        Set<Record.Time> times = timeRecordsRepository.saveAll(Set.of(time1, time2, time3));

        //when
        Set<Record.Time> searchedTimes
                = timeRecordsRepository.findAll(new RecordsSearchCond(null, null, LocalDate.of(2024, 1, 1), LocalTime.of(10, 0)));

        //then
        assertThat(searchedTimes)
                .extracting("timeId")
                .isNotNull();

        assertThat(searchedTimes)
                .hasSize(2)
                .extracting("date", "startTime", "durationMinutes", "record")
                .contains(
                        tuple(date1, startTime1, durationMinutes1, record),
                        tuple(date2, startTime2, durationMinutes2, record)
                );
    }


    @Test
    @DisplayName("Time 을 Record 의 action & memo 를 통해 검색할 수 있다.")
    void searchTime_withActionAndMemo() {
        //given
        String action1 = "공부";
        String memo1 = "코딩테스트";
        Record record1 = new Record(null, action1, memo1);

        String action2 = "헬스";
        String memo2 = "벤치프레스";
        Record record2 = new Record(null, action2, memo2);

        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalTime startTime1 = LocalTime.of(9, 30);
        int durationMinutes1 = 30;
        Record.Time time1 = new Record.Time(null, date1, startTime1, durationMinutes1, record1);

        LocalDate date2 = LocalDate.of(2024, 1, 1);
        LocalTime startTime2 = LocalTime.of(10, 0);
        int durationMinutes2 = 20;
        Record.Time time2 = new Record.Time(null, date2, startTime2, durationMinutes2, record1);

        LocalDate date3 = LocalDate.of(2024, 1, 2);
        LocalTime startTime3 = LocalTime.of(9, 30);
        int durationMinutes3 = 60;
        Record.Time time3 = new Record.Time(null, date3, startTime3, durationMinutes3, record2);

        Set<Record.Time> times = timeRecordsRepository.saveAll(Set.of(time1, time2, time3));

        //when
        //"공부", "공", "부", 모두 가능
        Set<Record.Time> searchedTimes
                = timeRecordsRepository.findAll(new RecordsSearchCond("부", null, null, null));

        //then
        assertThat(searchedTimes)
                .extracting("timeId")
                .isNotNull();

        assertThat(searchedTimes)
                .hasSize(2)
                .extracting("date", "startTime", "durationMinutes", "record")
                .contains(
                        tuple(date1, startTime1, durationMinutes1, record1),
                        tuple(date2, startTime2, durationMinutes2, record1)
                );
    }
}

