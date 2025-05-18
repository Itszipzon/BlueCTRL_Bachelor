package no.itszipzon.tables;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Fuel entity class representing the fuel table in the database.
 */
@Entity
@Table(name = "fuel")
public class Fuel {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "added")
  private LocalDateTime added;

  @Column(name = "vesselId")
  private Long vesselId;

  @Column(name = "tankId")
  private Long tankId;

  @Column(name = "x")
  private double x;

  @Column(name = "y")
  private double y;

  @Column(name = "z")
  private double z;

  @PrePersist
  protected void onCreate() {
    added = LocalDateTime.now();
  }

  /**
   * Get the id saved in local database for the tank.
   * @return the id for the tank
   */
  public Long getId() {
    return id;
  }

  /**
   * Set the ID of the tank.
   * 
   * @param id the ID to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Get id from external api.
   * 
   * @return the id from external api
   */
  public Long getVesselId() {
    return vesselId;
  }

  /**
   * Set the ID of the vessel.
   * 
   * @param vesselId the ID to set
   */
  public void setVesselId(Long vesselId) {
    this.vesselId = vesselId;
  }

  /**
   * Get the id of the tank.
   * 
   * @return the id of the tank
   */
  public Long getTankId() {
    return tankId;
  }

  /**
   * Set the ID of the tank.
   * 
   * @param tankId the ID to set
   */
  public void setTankId(Long tankId) {
    this.tankId = tankId;
  }
  
  /**
   * Get the x-coordinate of the tank on the vessel.
   * 
   * @return the x-coordinate of the tank
   */
  public double getX() {
    return x;
  }

  /**
   * Set the X coordinate of the fuel.
   * 
   * @param x the X coordinate to set
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Get the y-coordinate of the tank on the vessel.
   * 
   * @return the y-coordinate of the tank
   */
  public double getY() {
    return y;
  }

  /**
   * Set the Y coordinate of the fuel.
   * 
   * @param y the Y coordinate to set
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Get the z-coordinate of the tank on the vessel.
   * 
   * @return the z-coordinate of the tank
   */
  public double getZ() {
    return z;
  }

  /**
   * Set the Z coordinate of the fuel.
   * 
   * @param z the Z coordinate to set
   */
  public void setZ(double z) {
    this.z = z;
  }

}
