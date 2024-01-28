package monitoring.repository;

import monitoring.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("select distinct r from Record r join fetch r.timeRecords t")
    Optional<Record> findByIdWithTimes(Long id);

}
