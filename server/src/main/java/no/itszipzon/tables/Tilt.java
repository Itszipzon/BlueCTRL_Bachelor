package no.itszipzon.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tilt")
public class Tilt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "longitudinalDegree") // mellom baug og akter
  private double longitudinalDegree;

  @Column(name = "transverseDegree") // mellom styrbord og babord
  private double transverseDegree;

  @JoinColumn(name = "timetableId")
  @OneToOne
  private TimeTable timetableId;
  
}
