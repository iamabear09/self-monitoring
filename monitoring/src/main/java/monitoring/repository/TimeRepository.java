package monitoring.repository;

import monitoring.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRepository extends JpaRepository<Record.Time, Long> {

    List<Record.Time> findByRecordId(Long id);
}
