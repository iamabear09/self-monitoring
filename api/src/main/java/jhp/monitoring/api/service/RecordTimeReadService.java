package jhp.monitoring.api.service;

import jhp.monitoring.domain.Record;
import jhp.monitoring.domain.TimeLog;
import jhp.monitoring.service.RecordService;
import jhp.monitoring.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
}
