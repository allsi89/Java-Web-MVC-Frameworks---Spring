package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartServiceTests {

  @Autowired
  private PartRepository partRepository;
  @Autowired
  private SupplierRepository supplierRepository;
  private ModelMapper modelMapper;
  private PartService partService;

  @Before
  public void init() {
    this.modelMapper = new ModelMapper();
    this.partService = new PartServiceImpl(this.partRepository, this.modelMapper);
  }

  @Test
  public void partService_savePartWithCorrectValues_returnsCorrect() {
    Supplier supplier = getSupplier();
    this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel supplierServiceModel = this.modelMapper
        .map(this.supplierRepository.findAll().get(0), SupplierServiceModel.class);

    PartServiceModel toBeSaved = getPartServiceModel(supplierServiceModel);

    PartServiceModel expected = this.partService.savePart(toBeSaved);

    PartServiceModel actual = this.modelMapper
        .map(this.partRepository.findAll().get(0), PartServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getPrice(), actual.getPrice());
    Assert.assertEquals(expected.getSupplier().getId(), actual.getSupplier().getId());
  }

  @Test(expected = Exception.class)
  public void partService_savePartWithIncorrectPartValues_throwsException() {
    Supplier supplier = getSupplier();
    this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel supplierServiceModel = this.modelMapper
        .map(this.supplierRepository.findAll().get(0), SupplierServiceModel.class);

    PartServiceModel toBeSaved = getPartServiceModel(supplierServiceModel);
    toBeSaved.setName(null);
    toBeSaved.setPrice(BigDecimal.ZERO);

    this.partService.savePart(toBeSaved);
  }

  @Test
  public void partService_editPartWithCorrectValues_returnsCorrect() {
    Supplier supplier = getSupplier();
    this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel supplierServiceModel = this.modelMapper
        .map(this.supplierRepository.findAll().get(0), SupplierServiceModel.class);

    PartServiceModel toBeEdited = getPartServiceModel(supplierServiceModel);

    toBeEdited = this.modelMapper
        .map(this.partRepository.saveAndFlush(this.modelMapper
            .map(toBeEdited, Part.class)), PartServiceModel.class);

    toBeEdited.setName("new name");
    toBeEdited.setPrice(BigDecimal.TEN);

    PartServiceModel expected = this.partService.editPart(toBeEdited);

    PartServiceModel actual = this.modelMapper
        .map(this.partRepository.findAll().get(0), PartServiceModel.class);

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getPrice(), actual.getPrice());
    Assert.assertEquals(expected.getSupplier().getId(), actual.getSupplier().getId());
  }

  @Test(expected = Exception.class)
  public void partService_editPartWithIncorrectValues_throwsException() {
    Supplier supplier = getSupplier();
    this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel supplierServiceModel = this.modelMapper
        .map(this.supplierRepository.findAll().get(0), SupplierServiceModel.class);

    PartServiceModel toBeEdited = getPartServiceModel(supplierServiceModel);

    toBeEdited = this.modelMapper
        .map(this.partRepository.saveAndFlush(this.modelMapper
            .map(toBeEdited, Part.class)), PartServiceModel.class);

    toBeEdited.setName(null);
    toBeEdited.setPrice(BigDecimal.ZERO);

    this.partService.editPart(toBeEdited);
  }

  @Test
  public void partService_deletePartWithCorrectId_returnsCorrect() {
    Supplier supplier = getSupplier();
    this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel supplierServiceModel = this.modelMapper
        .map(this.supplierRepository.findAll().get(0), SupplierServiceModel.class);

    PartServiceModel partServiceModel = getPartServiceModel(supplierServiceModel);

    Part toBeDeleted = this.partRepository.saveAndFlush(this.modelMapper
        .map(partServiceModel, Part.class));

    this.partService.deletePart(toBeDeleted.getId());

    long actual = this.partRepository.count();
    long expected = 0;

    Assert.assertEquals(expected, actual);
  }

  @Test(expected = Exception.class)
  public void partService_deletePartWithIncorrectId_throwsException() {
    Supplier supplier = getSupplier();
    this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel supplierServiceModel = this.modelMapper
        .map(this.supplierRepository.findAll().get(0), SupplierServiceModel.class);

    PartServiceModel partServiceModel = getPartServiceModel(supplierServiceModel);

    this.partRepository.saveAndFlush(this.modelMapper.map(partServiceModel, Part.class));

    this.partService.deletePart("InvalidId");
  }

  @Test
  public void partService_findPartByIdWithCorrectId_returnsCorrect() {
    Supplier supplier = getSupplier();
    supplier = this.supplierRepository.saveAndFlush(supplier);

    SupplierServiceModel supplierServiceModel = this.modelMapper
        .map(supplier, SupplierServiceModel.class);

    PartServiceModel partServiceModel = getPartServiceModel(supplierServiceModel);

    Part part = this.modelMapper.map(partServiceModel, Part.class);
    part = this.partRepository.saveAndFlush(part);

    PartServiceModel actual = this.modelMapper
        .map(part, PartServiceModel.class);

    PartServiceModel expected = this.partService.findPartById(part.getId());

    Assert.assertEquals(expected.getId(), actual.getId());
    Assert.assertEquals(expected.getName(), actual.getName());
    Assert.assertEquals(expected.getPrice(), actual.getPrice());
    Assert.assertEquals(expected.getSupplier().getId(), actual.getSupplier().getId());
  }

  @Test(expected = Exception.class)
  public void partService_findPartByIdWithIncorrectId_throwsException() {
    Supplier supplier = getSupplier();
    SupplierServiceModel supplierServiceModel = this.modelMapper
    .map(this.supplierRepository.saveAndFlush(supplier), SupplierServiceModel.class);

    PartServiceModel partServiceModel = getPartServiceModel(supplierServiceModel);

    this.partRepository.saveAndFlush(this.modelMapper.map(partServiceModel, Part.class));

    this.partService.findPartById("InvalidId");

  }

  private PartServiceModel getPartServiceModel(SupplierServiceModel supplierServiceModel) {
    PartServiceModel partServiceModel = new PartServiceModel();
    partServiceModel.setSupplier(supplierServiceModel);
    partServiceModel.setName("part");
    partServiceModel.setPrice(BigDecimal.ONE);
    return partServiceModel;
  }

  private Supplier getSupplier() {
    Supplier supplier = new Supplier();
    supplier.setName("supplier");
    supplier.setImporter(true);
    return supplier;
  }
}
