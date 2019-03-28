package org.softuni.cardealer.web.controllers;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartsControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private PartRepository partRepository;

  @Autowired
  private SupplierRepository supplierRepository;

  @Autowired
  private Gson gson;

  @Before
  public void emptyDb() {
    this.partRepository.deleteAll();
    this.supplierRepository.deleteAll();
  }

  @Test
  @WithMockUser
  public void addPost_addPartCorrectly() throws Exception {
    Supplier supplier = getSupplierEntity();


    this.mvc
        .perform(post("/parts/add")
            .param("name", "Part")
            .param("price", "100")
            .param("supplier", supplier.getName()));


    Part actual = this.partRepository.findAll().get(0);

    Assert.assertEquals(1, this.partRepository.count());
    Assert.assertEquals("Part", actual.getName());
  }

  @Test
  @WithMockUser
  public void addPost_returnsCorrectView() throws Exception {
    Supplier supplier = getSupplierEntity();

    this.mvc
        .perform(post("/parts/add")
            .param("name", "Part")
            .param("price", "100")
            .param("supplier", supplier.getName()))
    .andExpect(redirectedUrl("all"));
  }

  @Test
  @WithMockUser
  public void editPost_editPartCorrectly() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity("Part", 100, supplier);
    part = this.partRepository.saveAndFlush(part);

    this.mvc
        .perform(post("/parts/edit/" + part.getId())
            .param("name", "Part New")
            .param("price", part.getPrice().toString())
            .param("supplier", supplier.getName()));

    Part actual = this.partRepository.findAll().get(0);

    Assert.assertEquals(part.getId(), actual.getId());
    Assert.assertEquals("Part New", actual.getName());
  }

  @Test
  @WithMockUser
  public void editPost_returnsCorrectView() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity("Part", 100, supplier);
    part = this.partRepository.saveAndFlush(part);

    this.mvc
        .perform(post("/parts/edit/" + part.getId())
            .param("name", "Part New")
            .param("price", part.getPrice().toString())
            .param("supplier", supplier.getName()))
    .andExpect(redirectedUrl("/parts/all"));
  }

  @Test(expected = Exception.class)
  @WithMockUser
  public void editPost_throwsExceptionWhenInvalidData() throws Exception {
    this.mvc
        .perform(post("/parts/edit/" + "InvalidID"));
  }

  @Test
  @WithMockUser
  public void deletePost_deletesPart() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity("Part", 100, supplier);
    part = this.partRepository.saveAndFlush(part);

    this.mvc
        .perform(post("/parts/delete/" + part.getId()));

    Assert.assertEquals(0, this.partRepository.count());
  }

  @Test(expected = Exception.class)
  @WithMockUser
  public void deletePost_throwsExceptionWhenInvalidData() throws Exception {
    this.mvc
        .perform(post("/parts/delete/" + "IncorrectId"));
  }

  @Test
  @WithMockUser
  public void deletePost_returnsCorrectView() throws Exception {
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);
    Part part = getPartEntity("Part", 100, supplier);
    part = this.partRepository.saveAndFlush(part);

    this.mvc
        .perform(post("/parts/delete/" + part.getId()))
    .andExpect(redirectedUrl("/parts/all"));
  }

  @Test
  @WithMockUser
  public void allGet_returnsAllPartsCorrectly() throws Exception {

    this.mvc
        .perform(get("/parts/all"))
        .andExpect(model().attributeExists("parts"));
  }


  @Test
  @WithMockUser
  public void allGet_returnsCorrectView() throws Exception {
    this.mvc
        .perform(get("/parts/all"))
        .andExpect(view().name("all-parts"));
  }

  @Test
  @WithMockUser
  public void fetchGet_returnsCorrectResult() throws Exception{
    Supplier supplier = getSupplierEntity();
    supplier = this.supplierRepository.saveAndFlush(supplier);

    Part first = getPartEntity("Part1", 100, supplier);
    first = this.partRepository.saveAndFlush(first);

    Part second = getPartEntity("Part1", 100, supplier);
    second = this.partRepository.saveAndFlush(second);

    String result = this.mvc
        .perform(get("/parts/fetch"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    Part[] parts = this.gson.fromJson(result, Part[].class);


    Part actualFirst = this.partRepository.findAll().get(0);

    Assert.assertEquals(parts[0].getId(), actualFirst.getId());
    Assert.assertEquals(parts[0].getName(), actualFirst.getName());
    Assert.assertEquals(parts.length, this.partRepository.count());
  }

  private Supplier getSupplierEntity() {
    Supplier supplier = new Supplier();
    supplier.setName("Supplier");
    supplier.setIsImporter(true);
    supplier = this.supplierRepository.saveAndFlush(supplier);
    return supplier;
  }

  private Part getPartEntity(String name, int price, Supplier supplier) {
    Part part = new Part();
    part.setName(name);
    part.setPrice(BigDecimal.valueOf(price));
    part.setSupplier(supplier);
    return part;
  }
}
