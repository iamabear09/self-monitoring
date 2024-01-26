package monitoring.repository;

import monitoring.domain.Record;
import monitoring.domain.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {

    List<Time> findByRecordId(Long id);
}
