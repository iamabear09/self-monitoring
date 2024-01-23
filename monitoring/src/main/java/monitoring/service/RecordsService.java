package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecordsService {

    private final RecordsRepository recordsRepository;

    public Record create(Record record) {
        return recordsRepository.save(record);
    }

    public Record get(Long id) {

        return recordsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Id 입니다."));
    }

    public Set<Record> getList(RecordsSearchCond Cond) {
        return recordsRepository.findAll(Cond);
    }

    public Record updateByPatch(Record updateRecord) {
        return recordsRepository.save(updateRecord);
    }
}
