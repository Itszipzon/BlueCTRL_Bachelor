package no.itszipzon.tables;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ship")
public class Ship {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "bluectrlId")
  private Long blueCtrlId;

  @Column(name = "vesselName")
  private String vesselName;

  @Column(name = "countryCode")
  private String countryCode;

  @Column(name = "vesselCode")
  private String vesselCode;

  @JoinColumn(name = "timetableId")
  @OneToMany
  private List<TimeTable> timetableId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBlueCtrlId() {
    return blueCtrlId;
  }

  public void setBlueCtrlId(Long blueCtrlId) {
    this.blueCtrlId = blueCtrlId;
  }

  public String getVesselName() {
    return vesselName;
  }

  public void setVesselName(String vesselName) {
    this.vesselName = vesselName;
  }
  
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getVesselCode() {
    return vesselCode;
  }

  public void setVesselCode(String vesselCode) {
    this.vesselCode = vesselCode;
  }

}
