package org.softuni.cardealer.domain.entities;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

  private String name;
  private LocalDate birthDate;
  private boolean isYoungDriver;

  public Customer() {
  }

  @Column(name = "name", nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "birth_date", nullable = false)
  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  @Column(name = "is_young_driver", nullable = false)
  public boolean isYoungDriver() {
    return isYoungDriver;
  }

  public void setYoungDriver(boolean youngDriver) {
    isYoungDriver = youngDriver;
  }
}
