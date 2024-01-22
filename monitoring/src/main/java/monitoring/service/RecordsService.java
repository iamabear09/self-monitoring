package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordsService {

    private final RecordsRepository recordsRepository;

    public Record create(Record record) {
        return recordsRepository.save(record);
    }
}
