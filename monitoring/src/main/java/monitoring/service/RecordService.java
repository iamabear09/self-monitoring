package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.common.ErrorMessage;
import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional
    public Record save(Record recordData) {
        Record record = Record.builder()
                .action(recordData.getAction())
                .memo(recordData.getMemo())
                .build();

        return recordRepository.save(record);
    }

    public Record get(Long id) {

        return recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ENTITY_NOT_FOUND.getMessage()));
    }

    @Transactional
    public Record update(Long id, Record recordData) {

        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ENTITY_NOT_FOUND.getMessage()));

        if (StringUtils.hasText(recordData.getAction())) {
            recordData.setAction(record.getAction());
        }
        if (StringUtils.hasLength(recordData.getMemo())) {
            recordData.setMemo(record.getMemo());
        }

        return recordRepository.save(record);
    }
}

