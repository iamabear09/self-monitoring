package jhp.monitoring.service;

import lombok.RequiredArgsConstructor;
import jhp.monitoring.common.ErrorMessage;
import jhp.monitoring.domain.Record;
import jhp.monitoring.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional
    public Record create(Record recordData) {
        Record record = Record.builder()
                .id(recordData.getId())
                .action(recordData.getAction())
                .memo(recordData.getMemo())
                .build();

        return recordRepository.save(record);
    }

    public Record get(String id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ENTITY_NOT_FOUND.getMessage()));
    }

    @Transactional
    public Record update(String id, Record recordData) {

        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ENTITY_NOT_FOUND.getMessage()));

        if (StringUtils.hasText(recordData.getAction())) {
            record.setAction(recordData.getAction());
        }
        if (StringUtils.hasLength(recordData.getMemo())) {
            record.setMemo(recordData.getMemo());
        }

        return recordRepository.save(record);
    }

    @Transactional
    public Record delete(String id) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ENTITY_NOT_FOUND.getMessage()));
        recordRepository.delete(record);
        return record;
    }
}

