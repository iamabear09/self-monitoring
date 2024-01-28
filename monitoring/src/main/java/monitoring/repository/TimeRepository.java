package monitoring.repository;

import monitoring.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Record.Time, Long>, TimeRepositoryCustom {
}
