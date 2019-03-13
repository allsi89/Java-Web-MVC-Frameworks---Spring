package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Customer;
import org.softuni.cardealer.domain.models.service.CustomerServiceModel;
import org.softuni.cardealer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerServiceTests {
  @Autowired
  private CustomerRepository customerRepository;
  private ModelMapper modelMapper;
  private CustomerService customerService;
  private CustomerServiceModel customerServiceModel;

  @Before
  public void init() {
    this.modelMapper = new ModelMapper();
    this.customerService = new CustomerServiceImpl(this.customerRepository, this.modelMapper);
    this.customerServiceModel = getCustomerServiceModel();
  }

  private CustomerServiceModel getCustomerServiceModel() {
    CustomerServiceModel customerServiceModel = new CustomerServiceModel();
    customerServiceModel.setBirthDate(LocalDate.parse("12-01-1990",
        DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    customerServiceModel.setName("Pesho");
    customerServiceModel.setYoungDriver(false);
    return customerServiceModel;
  }

  @Test
  public void customerService_saveCustomerWithCorrectValues_returnsCorrect() {
    CustomerServiceModel expected = this.customerService.saveCustomer(this.customerServiceModel);

    CustomerServiceModel actual = this.modelMapper
        .map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
    Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
  }

  @Test(expected = Exception.class)
  public void customerService_saveCustomerWithIncorrectValues_throwsException() {
    CustomerServiceModel toBeSaved = new CustomerServiceModel();

    toBeSaved.setName(null);
    toBeSaved.setYoungDriver(true);
    toBeSaved.setBirthDate(null);

    this.customerService.saveCustomer(toBeSaved);
  }

  @Test
  public void customerService_editCustomerWithCorrectValues_returnsCorrect() {
    CustomerServiceModel toBeEdited = this.modelMapper
        .map(this.customerRepository.saveAndFlush(this.modelMapper
        .map(this.customerServiceModel, Customer.class)),
            CustomerServiceModel.class);

    toBeEdited.setName("Gosho");
    toBeEdited.setYoungDriver(true);
    toBeEdited.setBirthDate(LocalDate.parse("12-12-1995",
        DateTimeFormatter.ofPattern("dd-MM-yyyy")));

    CustomerServiceModel expected = this.customerService.editCustomer(toBeEdited);

    Customer actual = this.customerRepository.findAll().get(0);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
    Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
  }

  @Test(expected = Exception.class)
  public void customerService_editCustomerWithIncorrectValues_throwsException() {
    CustomerServiceModel toBeEdited = this.modelMapper
        .map(this.customerRepository.saveAndFlush(this.modelMapper
                .map(this.customerServiceModel, Customer.class)),
            CustomerServiceModel.class);

    toBeEdited.setName(null);
    toBeEdited.setYoungDriver(true);
    toBeEdited.setBirthDate(null);

    this.customerService.editCustomer(toBeEdited);
  }

  @Test
  public void customerService_deleteCustomerWithValidId_returnsCorrect() {
    CustomerServiceModel toBeDeleted = this.modelMapper
        .map(this.customerRepository.saveAndFlush(this.modelMapper
                .map(this.customerServiceModel, Customer.class)),
            CustomerServiceModel.class);

    this.customerService.deleteCustomer(toBeDeleted.getId());

    long expected = 0;
    long actual = this.customerRepository.count();

    Assert.assertEquals(expected, actual);
  }

  @Test(expected = Exception.class)
  public void customerService_deleteCustomerWithInvalidId_throwsException() {
    this.customerRepository.saveAndFlush(this.modelMapper
        .map(this.customerServiceModel, Customer.class));

    this.customerService.deleteCustomer("InvalidId");
  }

  @Test
  public void customerService_findCustomerByIdWithValidId_returnsCorrect() {
    Customer customer = this.modelMapper
        .map(this.customerServiceModel, Customer.class);
    customer = this.customerRepository.saveAndFlush(customer);

    CustomerServiceModel actual = this.modelMapper
        .map(customer, CustomerServiceModel.class);

    CustomerServiceModel expected = this.customerService.findCustomerById(actual.getId());

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
    Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
  }

  @Test(expected = Exception.class)
  public void customerService_findCustomerByIdWithInvalidId_throwsException() {
    this.customerRepository.saveAndFlush(this.modelMapper
        .map(this.customerServiceModel, Customer.class));

    this.customerService.findCustomerById("InvalidId");
  }

}
