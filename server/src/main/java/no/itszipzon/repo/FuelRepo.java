package no.itszipzon.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import no.itszipzon.tables.Fuel;

/**
 * Repository interface for Fuel entity.
 */
public interface FuelRepo extends JpaRepository<Fuel, Long> {

  /**
   * Find all fuel records by vessel ID.
   *
   * @param vesselId the vessel ID
   * @return a list of fuel records
   */
  @Query(value = """
    SELECT DISTINCT ON (f.tank_id) *
    FROM fuel f
    WHERE f.vessel_id = :vesselId
    ORDER BY f.tank_id, f.added DESC
    """, nativeQuery = true)
  Optional<List<Fuel>> findByVesselIdLatest(@Param("vesselId") Long vesselId);

}
