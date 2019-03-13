package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.models.service.CarSaleServiceModel;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.domain.models.service.PartSaleServiceModel;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.repository.CarSaleRepository;
import org.softuni.cardealer.repository.PartSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SaleServiceTests {

  @Autowired
  private CarSaleRepository carSaleRepository;
  @Autowired
  private PartSaleRepository partSaleRepository;
  private SaleService saleService;

  @Before
  public void init() {
    this.saleService = new SaleServiceImpl(this.carSaleRepository, this.partSaleRepository,
        new ModelMapper());
  }

  @Test
  public void saleService_saleCarWithCorrectValues_returnsCorrect() {
    CarSaleServiceModel carSaleServiceModel = Mockito.mock(CarSaleServiceModel.class);

    this.saleService.saleCar(carSaleServiceModel);

    long expected = 1;
    long actual = this.carSaleRepository.count();

    Assert.assertEquals(expected, actual);
  }

  @Test(expected = Exception.class)
  public void saleService_saleCarWithIncorrectValues_throwsException() {
    CarSaleServiceModel carSaleServiceModel = new CarSaleServiceModel();

    CarServiceModel carServiceModel = new CarServiceModel();

    carServiceModel.setMake(null);
    carServiceModel.setModel(null);

    carSaleServiceModel.setCar(carServiceModel);

    this.saleService.saleCar(carSaleServiceModel);
  }

  @Test
  public void saleService_salePartWithCorrectValues_returnsCorrect() {
    PartSaleServiceModel partSaleServiceModel = Mockito.mock(PartSaleServiceModel.class);

    this.saleService.salePart(partSaleServiceModel);

    long expected = 1;
    long actual = this.partSaleRepository.count();

    Assert.assertEquals(expected, actual);
  }

  @Test(expected = Exception.class)
  public void saleService_salePartWithInCorrectValues_throwsxException() {
    PartSaleServiceModel partSaleServiceModel = new PartSaleServiceModel();

    PartServiceModel partServiceModel = new PartServiceModel();

    partServiceModel.setName(null);
    partServiceModel.setPrice(BigDecimal.ZERO);
    partServiceModel.setSupplier(null);

    partSaleServiceModel.setPart(partServiceModel);

    this.saleService.salePart(partSaleServiceModel);
  }

}
