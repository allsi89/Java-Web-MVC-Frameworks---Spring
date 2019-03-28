package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarsControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private PartRepository partRepository;

  @Autowired
  private SupplierRepository supplierRepository;

  @Before
  public void emptyDb() {
    this.carRepository.deleteAll();
    this.partRepository.deleteAll();
    this.supplierRepository.deleteAll();
  }


  @Test
  @WithMockUser
  public void addPost_addCarCorrectly() throws Exception {

    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity(supplier);
    part = this.partRepository.saveAndFlush(part);


    this.mvc
        .perform(post("/cars/add")
            .param("make", "Make")
            .param("model", "Model")
            .param("travelledDistance", "10000")
            .param("parts", part.getId()));

    Assert.assertEquals(1, this.carRepository.count());
    Assert.assertEquals(1, this.carRepository.findAll().get(0).getParts().size());
  }

  @Test
  @WithMockUser
  public void addPost_returnsCorrectView() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity(supplier);
    part = this.partRepository.saveAndFlush(part);


    this.mvc
        .perform(post("/cars/add")
            .param("make", "Make")
            .param("model", "Model")
            .param("travelledDistance", "10000")
            .param("parts", part.getId()))
        .andExpect(redirectedUrl("all"));
  }

  @Test
  @WithMockUser
  public void editPost_editCarWithValidId() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity(supplier);
    part = this.partRepository.saveAndFlush(part);

    Car car = getCarEntity();
    car = this.carRepository.saveAndFlush(car);

    this.mvc
        .perform(post("/cars/edit/" + car.getId())
            .param("make", "New Make")
            .param("model", "New Model")
            .param("travelledDistance", "15000")
            .param("parts", part.getId())
        );

    Car actual = this.carRepository.findAll().get(0);

    Assert.assertEquals("New Make", actual.getMake());
    Assert.assertEquals("New Model", actual.getModel());
    Assert.assertEquals(15000L, (long) actual.getTravelledDistance());
    Assert.assertEquals(car.getId(), actual.getId());
    Assert.assertEquals(1, this.carRepository.count());
  }

  @Test(expected = Exception.class)
  @WithMockUser
  public void editPost_editCarIncorrectId() throws Exception {
    this.mvc
        .perform(post("/cars/edit/" + "IncorrectID"));
  }

  @Test
  @WithMockUser
  public void editPost_returnsCorrectView() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity(supplier);
    part = this.partRepository.saveAndFlush(part);

    Car car = getCarEntity();
    car = this.carRepository.saveAndFlush(car);

    this.mvc
        .perform(post("/cars/edit/" + car.getId())
            .param("make", "New Make")
            .param("model", "New Model")
            .param("travelledDistance", "15000")
            .param("parts", part.getId()))
        .andExpect(redirectedUrl("/cars/all"));
  }

  @Test
  @WithMockUser
  public void deletePost_deletesCar() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity(supplier);
    part = this.partRepository.saveAndFlush(part);

    Car car = getCarEntity();
    car = this.carRepository.saveAndFlush(car);

    this.mvc
        .perform(post("/cars/delete/" + car.getId()));

    Assert.assertEquals(0, this.carRepository.count());

  }

  @Test(expected = Exception.class)
  @WithMockUser
  public void deletePost_throwsExceptionWhenInvalidData() throws Exception {
    this.mvc
        .perform(post("/cars/delete/" + "IncorrectId"));
  }

  @Test
  @WithMockUser
  public void deletePost_returnsCorrectView() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity(supplier);
    part = this.partRepository.saveAndFlush(part);

    Car car = getCarEntity();
    car = this.carRepository.saveAndFlush(car);

    this.mvc
        .perform(post("/cars/delete/" + car.getId()))
        .andExpect(redirectedUrl("/cars/all"));
  }


  @Test
  @WithMockUser
  public void allGet_returnsAllCarsCorrectly() throws Exception {
    this.mvc
        .perform(get("/cars/all"))
        .andExpect(model().attributeExists("cars"));
  }

  @Test
  @WithMockUser
  public void allGet_returnsCorrectView() throws Exception {
    this.mvc
        .perform(get("/cars/all"))
        .andExpect(view().name("all-cars"));
  }

  private Supplier getSupplierEntity() {
    Supplier supplier = new Supplier();
    supplier.setName("Supplier");
    supplier.setIsImporter(true);
    return supplier;
  }

  private Part getPartEntity(Supplier supplier) {
    Part part = new Part();
    part.setName("Part");
    part.setPrice(BigDecimal.valueOf(100));
    part.setSupplier(supplier);
    return part;
  }

  private Car getCarEntity() {
    Car car = new Car();
    car.setMake("Make");
    car.setModel("Model");
    car.setTravelledDistance(10000L);
    car.setParts(new ArrayList<>());
    return car;
  }
}
