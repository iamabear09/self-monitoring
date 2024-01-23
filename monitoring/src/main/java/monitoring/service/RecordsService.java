package monitoring.service;

import lombok.RequiredArgsConstructor;
import monitoring.domain.Record;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

}
