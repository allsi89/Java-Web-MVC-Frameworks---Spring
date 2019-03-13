package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarServiceTests {
  @Autowired
  private CarRepository carRepository;
  private ModelMapper modelMapper;
  private CarService carService;
  private CarServiceModel carServiceModel;

  @Before
  public void init() {
    this.modelMapper = new ModelMapper();
    this.carService = new CarServiceImpl(this.carRepository, this.modelMapper);
    this.carServiceModel = getCarServiceModel();
  }

  private CarServiceModel getCarServiceModel() {
    CarServiceModel carServiceModel = new CarServiceModel();
    carServiceModel.setMake("BMW");
    carServiceModel.setModel("M2");
    carServiceModel.setParts(new ArrayList<>());
    carServiceModel.setTravelledDistance(25000L);
    return carServiceModel;
  }

  @Test
  public void carService_saveCarWithCorrectValues_returnsCorrect() {
    CarServiceModel actual = this.carService.saveCar(this.carServiceModel);
    CarServiceModel expected = this.modelMapper
        .map(this.carRepository.findAll().get(0),
            CarServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getMake(), actual.getMake());
    Assert.assertEquals(expected.getModel(), actual.getModel());
    Assert.assertEquals(expected.getTravelledDistance(), actual.getTravelledDistance());
    Assert.assertEquals(expected.getParts().size(), actual.getParts().size());
  }

  @Test(expected = Exception.class)
  public void carService_saveCarWithIncorrectValues_throwsException() {
    CarServiceModel car = new CarServiceModel();
    car.setModel(null);
    car.setMake(null);
    car.setTravelledDistance(0L);
    car.setParts(new ArrayList<>());

    this.carService.saveCar(car);
  }

  @Test
  public void carService_editCarWithCorrectValues_returnsCorrect() {
    Car car = this.carRepository.saveAndFlush(this.modelMapper
        .map(this.carServiceModel, Car.class));

    CarServiceModel toBeEdited = this.modelMapper
        .map(car, CarServiceModel.class);

    toBeEdited.setMake("VW");
    toBeEdited.setModel("Arteon");
    toBeEdited.setTravelledDistance(24058L);

    CarServiceModel expected = this.carService.editCar(toBeEdited);

    CarServiceModel actual = this.modelMapper
        .map(this.carRepository.findAll().get(0), CarServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getMake(), actual.getMake());
    Assert.assertEquals(expected.getModel(), actual.getModel());
    Assert.assertEquals(expected.getTravelledDistance(), actual.getTravelledDistance());
    Assert.assertEquals(expected.getParts().size(), actual.getParts().size());
  }

  @Test(expected = Exception.class)
  public void carService_editWithIncorrectValues_throwsException() {
    Car car = this.carRepository.saveAndFlush(this.modelMapper
        .map(this.carServiceModel, Car.class));

    CarServiceModel toBeEdited = this.modelMapper
        .map(car, CarServiceModel.class);

    toBeEdited.setMake(null);
    toBeEdited.setModel(null);
    toBeEdited.setTravelledDistance(0L);

    this.carService.editCar(toBeEdited);
  }

  @Test
  public void carService_deleteCarWithValidId_returnsCorrect() {
    Car car = this.carRepository.saveAndFlush(this.modelMapper
        .map(this.carServiceModel, Car.class));

    CarServiceModel toBeDeleted = this.modelMapper
        .map(car, CarServiceModel.class);

    this.carService.deleteCar(toBeDeleted.getId());

    long expected = 0;
    long actual = this.carRepository.count();

    Assert.assertEquals(expected, actual);
  }

  @Test(expected = Exception.class)
  public void carService_deleteCarWithInvalidId_throwsException() {
    this.carRepository.saveAndFlush(this.modelMapper
        .map(this.carServiceModel, Car.class));

    this.carService.deleteCar("InvalidId");
  }

  @Test
  public void carService_findCarByIdWithValidId_returnsCorrect() {
    Car car = this.carRepository.saveAndFlush(this.modelMapper
        .map(this.carServiceModel, Car.class));

    CarServiceModel actual = this.carService.findCarById(car.getId());

    CarServiceModel expected = this.modelMapper
        .map(car, CarServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getMake(), actual.getMake());
    Assert.assertEquals(expected.getModel(), actual.getModel());
    Assert.assertEquals(expected.getTravelledDistance(), actual.getTravelledDistance());
    Assert.assertEquals(expected.getParts().size(), actual.getParts().size());
  }

  @Test(expected = Exception.class)
  public void carService_findCarByIdWithInvalidId_throwsException() {
    this.carRepository.saveAndFlush(this.modelMapper
        .map(this.carServiceModel, Car.class));

    this.carService.findCarById("InvalidId");
  }
}
