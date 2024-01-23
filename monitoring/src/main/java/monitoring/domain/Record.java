package monitoring.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter
@EqualsAndHashCode
@ToString
public class Record {

    private Long recordId;
    private String action;
    private String memo;
    private final Set<Time> timeRecords = new HashSet<>();

    public Record(Long recordId, String action, String memo) {
        this.recordId = recordId;
        this.action = action;
        this.memo = memo;
    }

    public Record toRecordWith(Long id) {
        Record record = new Record(id, action, memo);

        //반환할 Record 에 Time Record 를 채운다.
        timeRecords.forEach(t -> {
            new Time(t.getTimeId(), t.getDate(), t.getStartTime(), t.getDurationMinutes(), record);
        });

        return record;
    }
    public Set<Record.Time> getTimeRecords() {
        return Collections.unmodifiableSet(timeRecords);
    }

    public void updateContent(String action, String memo) {
        this.action = action;
        this.memo = memo;
    }

    private void deleteTime(Record.Time time) {
        timeRecords.remove(time);
    }
    private void addTime(Time time) {
        timeRecords.add(time);
    }

    private boolean hasSameContentAs(String action, String memo) {
        if (action == null) return true;
        if (!this.action.toLowerCase().contains(action.toLowerCase())) return false;

        //memo 는 null 이면 전부다 검색하는 것으로 생각하고 True
        if (memo == null) return true;
        return this.memo.toLowerCase().contains(memo.toLowerCase());

    }

    @Getter
    @EqualsAndHashCode(exclude = "record")
    @ToString(exclude = "record")
    public static class Time {

        private final Long timeId;
        private final LocalDate date;
        private final LocalTime startTime;
        private final LocalTime endTime;
        private final Integer durationMinutes;
        private final Record record;

        public Time(Long timeId, LocalDate date, LocalTime startTime, Integer durationMinutes, Record record) {
            this.timeId = timeId;
            this.date = date;
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
            this.endTime = startTime.plusMinutes(durationMinutes);

            this.record = Optional.ofNullable(record)
                    .orElseThrow(() -> new IllegalArgumentException("Record 는 필수 입니다."));
            this.record.addTime(this);
        }

        public Record.Time toTimeWith(Long id) {

            Time time = new Time(id, date, startTime, durationMinutes, record);

            //Record 의 Time 을 Id가 존재하는 Time 으로 변경한다.
            this.delete();
            record.addTime(time);

            return time;
        }

        public void delete() {
            record.deleteTime(this);
        }

        public boolean isOnTimeline(LocalDate date, LocalTime time) {

            if (date != null && !this.date.isEqual(date)) return false;
            if (time != null && (time.isBefore(startTime) || time.isAfter(endTime))) return false;

            //조건이 null 인 경우에도 True 를 반환한다.
            return true;
        }

        public boolean hasSameContentAs(String action, String memo) {
            return record.hasSameContentAs(action, memo);
        }
    }


}
