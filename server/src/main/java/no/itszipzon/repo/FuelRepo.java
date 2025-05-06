package no.itszipzon.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import no.itszipzon.tables.Fuel;

public interface FuelRepo extends JpaRepository<Fuel, Long> {
  
  @Query("""
    SELECT f
    FROM Fuel f
    WHERE f.vesselId = :vesselId
    AND f.added = (
      SELECT MAX(f2.added)
      FROM Fuel f2
      WHERE f2.vesselId = :vesselId
    )
      """)
  Optional<List<Fuel>> findByVesselIdLatest(Long vesselId);

}
