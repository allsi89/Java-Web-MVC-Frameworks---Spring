package softuni.residentevil.domain.models.binding;

import org.springframework.format.annotation.DateTimeFormat;
import softuni.residentevil.annotations.IsBefore;
import softuni.residentevil.annotations.IsCreator;
import softuni.residentevil.annotations.IsMagnitude;
import softuni.residentevil.annotations.IsMutation;
import softuni.residentevil.domain.entities.Creator;
import softuni.residentevil.domain.entities.Magnitude;
import softuni.residentevil.domain.entities.Mutation;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VirusBindingModel {
  private static final String INVALID_NAME_MSG = "Invalid name!";
  private static final String INVALID_DESC_MSG = "Description should be from 5 to 100 symbols long!";
  private static final String INVALID_SE_MSG = "Invalid side effect length! Maximum length is 50 symbols.";
  private static final String NO_SUCH_VALUE = "Invalid value.";

  private String id;
  private String name;
  private String description;
  private String sideEffects;
  private Creator creator;
  private boolean isDeadly;
  private boolean isCurable;
  private Mutation mutation;
  private Integer turnoverRate;
  private Integer hoursUntilTurn;
  private Magnitude magnitude;

  private LocalDate releasedOn;
  private List<String> capitals;

  public VirusBindingModel() {
    this.capitals = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Size(min = 3, max = 10, message = INVALID_NAME_MSG)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Size(min = 5, max = 100, message = INVALID_DESC_MSG)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Size(max = 50, message = INVALID_SE_MSG)
  public String getSideEffects() {
    return sideEffects;
  }

  public void setSideEffects(String sideEffects) {
    this.sideEffects = sideEffects;
  }

  @NotNull(message = NO_SUCH_VALUE)
  @IsCreator(enumClass = Creator.class, message = NO_SUCH_VALUE)
  public Creator getCreator() {
    return creator;
  }

  public void setCreator(Creator creator) {
    this.creator = creator;
  }

  public boolean isDeadly() {
    return isDeadly;
  }

  public void setDeadly(boolean deadly) {
    isDeadly = deadly;
  }

  public boolean isCurable() {
    return isCurable;
  }

  public void setCurable(boolean curable) {
    isCurable = curable;
  }

  @NotNull(message = NO_SUCH_VALUE)
  @IsMutation(enumClass = Mutation.class, message = NO_SUCH_VALUE)
  public Mutation getMutation() {
    return mutation;
  }

  public void setMutation(Mutation mutation) {
    this.mutation = mutation;
  }

  @NotNull
  @Min(value = 0)
  @Max(value = 100)
  public Integer getTurnoverRate() {
    return turnoverRate;
  }

  public void setTurnoverRate(Integer turnoverRate) {
    this.turnoverRate = turnoverRate;
  }

  @NotNull
  @Min(value = 1)
  @Max(value = 12)
  public Integer getHoursUntilTurn() {
    return hoursUntilTurn;
  }

  public void setHoursUntilTurn(Integer hoursUntilTurn) {
    this.hoursUntilTurn = hoursUntilTurn;
  }

  @NotNull(message = NO_SUCH_VALUE)
  @IsMagnitude(enumClass = Magnitude.class)
  public Magnitude getMagnitude() {
    return magnitude;
  }

  public void setMagnitude(Magnitude magnitude) {
    this.magnitude = magnitude;
  }

  @NotNull
  @IsBefore
  public LocalDate getReleasedOn() {
    return releasedOn;
  }

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  public void setReleasedOn(LocalDate releasedOn) {
    this.releasedOn = releasedOn;
  }

  public List<String> getCapitals() {
    return capitals;
  }

  public void setCapitals(List<String> capitals) {
    this.capitals = capitals;
  }
}
