package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SupplierServiceTests {

  @Autowired
  private SupplierRepository supplierRepository;
  private ModelMapper modelMapper;
  private SupplierService supplierService;

  @Before
  public void init() {
    this.modelMapper = new ModelMapper();
    this.supplierService = new SupplierServiceImpl(this.supplierRepository, this.modelMapper);
  }

  @Test
  public void supplierService_saveSupplierWithCorrectValues_returnsCorrect() {
    SupplierServiceModel toBeSaved = getSupplierServiceModel();

    SupplierServiceModel actual = this.supplierService.saveSupplier(toBeSaved);
    SupplierServiceModel expected = this.modelMapper
        .map(this.supplierRepository.findAll().get(0), SupplierServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.isImporter(), actual.isImporter());
  }

  @Test(expected = Exception.class)
  public void supplierService_saveSupplierWithNullValues_throwsException() {
    SupplierServiceModel toBeSaved = getSupplierServiceModel();
    toBeSaved.setName(null);

    supplierService.saveSupplier(toBeSaved);
  }

  @Test
  public void supplierService_editSupplierWithCorrectValues_ReturnsCorrect() {
    Supplier supplier = this.modelMapper.map(getSupplierServiceModel(), Supplier.class);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel toBeEdited = new SupplierServiceModel();
    toBeEdited.setId(supplier.getId());
    toBeEdited.setName("Pesho");
    toBeEdited.setImporter(false);

    SupplierServiceModel actual = this.supplierService.editSupplier(toBeEdited);
    SupplierServiceModel expected = this.modelMapper
        .map(this.supplierRepository.findAll().get(0), SupplierServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.isImporter(), actual.isImporter());
  }

  @Test(expected = Exception.class)
  public void supplierService_editSupplierWithNullValues_throwsException() {

    Supplier supplier = this.modelMapper.map(getSupplierServiceModel(), Supplier.class);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel toBeEdited = new SupplierServiceModel();
    toBeEdited.setId(supplier.getId());
    toBeEdited.setName(null);

    this.supplierService.editSupplier(toBeEdited);
  }

  @Test
  public void supplierService_deleteSupplierWithValidId_ReturnsCorrect() {
    Supplier supplier = this.modelMapper.map(getSupplierServiceModel(), Supplier.class);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    this.supplierService.deleteSupplier(supplier.getId());

    long expected = 0;
    long actual = this.supplierRepository.count();

    Assert.assertEquals(expected, actual);
  }

  @Test(expected = Exception.class)
  public void supplierService_deleteSupplierWithInvalidId_throwsException(){
    Supplier supplier = this.modelMapper.map(getSupplierServiceModel(), Supplier.class);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    this.supplierService.deleteSupplier("InvalidId");
  }

  @Test
  public void supplierService_findSupplierByCorrectId_ReturnsCorrect() {
    Supplier supplier = this.modelMapper.map(getSupplierServiceModel(), Supplier.class);
    supplier = this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel actual = this.supplierService
        .findSupplierById(supplier.getId());

    SupplierServiceModel expected = this.modelMapper
        .map(supplier, SupplierServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.isImporter(), actual.isImporter());
  }

  @Test(expected = Exception.class)
  public void supplierService_findSupplierByInvalidId_ReturnsNull() {
    Supplier supplier = this.modelMapper.map(getSupplierServiceModel(), Supplier.class);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    //ModelMapper throws exception in SupplierServiceImpl - cannot map a null object

    this.supplierService.findSupplierById("InvalidId");

  }

  private SupplierServiceModel getSupplierServiceModel() {
    SupplierServiceModel toBeSaved = new SupplierServiceModel();
    toBeSaved.setName("Misho");
    toBeSaved.setImporter(true);
    return toBeSaved;
  }


}
