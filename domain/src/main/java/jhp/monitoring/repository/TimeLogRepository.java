package jhp.monitoring.repository;

import jhp.monitoring.domain.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {

    List<TimeLog> findByRecordId(String recordId);

    List<TimeLog> findByDate(LocalDate date);

}
