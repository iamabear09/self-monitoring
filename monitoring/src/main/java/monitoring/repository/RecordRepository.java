package monitoring.repository;

import monitoring.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("select r from Record r join fetch r.timeRecords t where r.id = :id")
    Optional<Record> findByIdWithTimes(@Param("id") Long id);

}
