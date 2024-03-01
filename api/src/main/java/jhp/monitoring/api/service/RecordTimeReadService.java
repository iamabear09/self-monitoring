package jhp.monitoring.api.service;

import jhp.monitoring.api.service.request.RecordSearchCond;
import jhp.monitoring.domain.Record;
import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.service.RecordService;
import jhp.monitoring.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class RecordTimeReadService {

    private final RecordService recordService;
    private final TimeLogService timeLogService;
    private final ThreadPoolTaskExecutor threadPool;

    public Record getRecordWithTimeLogs(String recordId) {
        CompletableFuture<List<TimeLog>> timeLogsFuture = CompletableFuture.supplyAsync(() -> {
            List<TimeLog> timeLogs = timeLogService.getByRecordId(recordId);
            log.debug("find TimeLogs = {}", timeLogs);

            return timeLogs;
        }, threadPool);

        Record record = recordService.get(recordId);
        List<TimeLog> timeLogs = timeLogsFuture.join();
        connectRecordAndTimeLogList(record, timeLogs);
        return record;
    }

    private void connectRecordAndTimeLogList(Record record, List<TimeLog> timeLogs) {
        record.setTimeLogs(new ArrayList<>(timeLogs));
        timeLogs.forEach(timeLog -> {
            if (timeLog.getRecord() == record) return;
            timeLog.setRecord(record);
        });
    }

    public List<Record> getRecords(RecordSearchCond cond) {
        List<TimeLog> timeLogs = timeLogService.searchOverlappingTimeLogs(cond.getDate(), cond.getStartTime(), cond.getEndTime());
        Set<Record> searchRecordSet = timeLogs.stream()
                .map(t -> this.getRecordWithTimeLogs(t.getRecord().getId()))
                .filter(record -> {
                    if (!StringUtils.hasText(cond.getAction())) {
                        return true;
                    }
                    return record.getAction()
                            .toLowerCase()
                            .contains(cond.getAction().toLowerCase());
                })
                .collect(Collectors.toSet());

        return searchRecordSet.stream().toList();
    }
    public void validateRecordId(String id) {
        recordService.get(id);
    }
}
