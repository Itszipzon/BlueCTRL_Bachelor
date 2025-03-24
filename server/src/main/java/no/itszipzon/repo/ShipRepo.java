package no.itszipzon.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import no.itszipzon.tables.Ship;

public interface ShipRepo extends JpaRepository<Ship, Long> {
  
}
