package no.itszipzon.tables;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVesselId() {
    return vesselId;
  }

  public void setVesselId(Long vesselId) {
    this.vesselId = vesselId;
  }

  public Long getTankId() {
    return tankId;
  }

  public void setTankId(Long tankId) {
    this.tankId = tankId;
  }
  
  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

}
