package org.softuni.cardealer.domain.entities;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

  private String make;
  private String model;
  private Long travelledDistance;
  private List<Part> parts;

  public Car() {
  }

  @Column(name = "make", nullable = false)
  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  @Column(name = "model", nullable = false)
  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  @Column(name = "travelled_distance", nullable = false)
  public Long getTravelledDistance() {
    return travelledDistance;
  }

  public void setTravelledDistance(Long travelledDistance) {
    this.travelledDistance = travelledDistance;
  }

  @ManyToMany(targetEntity = Part.class)
  @JoinTable(
      name = "parts_cars",
      joinColumns = @JoinColumn(name = "car_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id")
  )
  public List<Part> getParts() {
    return parts;
  }

  public void setParts(List<Part> parts) {
    this.parts = parts;
  }
}
