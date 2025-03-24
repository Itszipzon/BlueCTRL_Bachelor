package no.itszipzon.tables;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "timetable")
public class TimeTable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "time")
  private LocalDateTime time;

  @JoinColumn(name = "shipId")
  @ManyToOne
  private Ship shipId;

  @JoinColumn
  @OneToOne
  private Gps gps;

  @JoinColumn
  @OneToOne
  private Fuel fuel;

  @JoinColumn
  @OneToOne
  private Tilt tilt;
  
}
