package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import monitoring.repository.RecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {

    private RecordRepository recordRepository;

    public Record create(Record record) {
        return recordRepository.save(record);
    }

    public Record get(Long id) {

        return recordRepository.findByIdWithTimes(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Record 입니다."));
    }

}
