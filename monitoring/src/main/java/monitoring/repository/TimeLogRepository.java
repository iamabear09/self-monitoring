package monitoring.repository;

import monitoring.domain.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
}
