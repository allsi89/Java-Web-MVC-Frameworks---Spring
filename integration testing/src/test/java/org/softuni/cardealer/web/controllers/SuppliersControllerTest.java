package org.softuni.cardealer.web.controllers;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SuppliersControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private SupplierRepository supplierRepository;

  @Autowired
  private Gson gson;

  @Before
  public void emptyDb() {
    this.supplierRepository.deleteAll();
  }

  @Test
  @WithMockUser
  public void addPost_addsSupplierCorrectly() throws Exception {
    this.mvc
        .perform(post("/suppliers/add")
            .param("name", "TestSupplier")
            .param("isImporter", "on"));

    //works with "on"/"off" and "true"/"false"
//    this.mvc
//        .perform(post("/suppliers/add")
//            .param("name", "TestSupplier")
//            .param("isImporter", "true"));

    Supplier actual = this.supplierRepository.findAll().get(0);
    Assert.assertEquals(1, this.supplierRepository.count());
    Assert.assertEquals("TestSupplier", actual.getName());
    Assert.assertTrue(actual.getIsImporter());
  }

  @Test
  @WithMockUser
  public void addPost_returnsCorrectView() throws Exception {
    this.mvc
        .perform(post("/suppliers/add")
            .param("name", "TestSupplier")
            .param("isImporter", "on"))
        .andExpect(redirectedUrl("all"));
  }

  @Test
  @WithMockUser
  public void editPost_editsSupplierCorrectly() throws Exception {
    Supplier first = getSupplierEntity("Supplier1", true);

    Supplier second = getSupplierEntity("Supplier2", false);

    first = this.supplierRepository.saveAndFlush(first);
    second = this.supplierRepository.saveAndFlush(second);

    this.mvc
        .perform(post("/suppliers/edit/" + first.getId())
            .param("name", "New Supplier 1")
            .param("isImporter", "false"));

    this.mvc
        .perform(post("/suppliers/edit/" + second.getId())
            .param("name", "New Supplier 2")
            .param("isImporter", "true"));

    Supplier firstActual = this.supplierRepository.findById(first.getId()).orElse(null);
    Supplier secondActual = this.supplierRepository.findById(second.getId()).orElse(null);

    Assert.assertEquals("New Supplier 1", firstActual.getName());
    Assert.assertFalse(firstActual.getIsImporter());

    Assert.assertEquals("New Supplier 2", secondActual.getName());
    Assert.assertTrue(secondActual.getIsImporter());

  }

  @Test(expected = Exception.class)
  @WithMockUser
  public void editPost_throwsExceptionWhenInvalidData() throws Exception {
    Supplier supplier = getSupplierEntity("Supplier1", true);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    this.mvc
        .perform(post("/suppliers/edit/" + "InvalidID"));
  }


  @Test
  @WithMockUser
  public void editPost_returnsCorrectView() throws Exception {
    Supplier supplier = getSupplierEntity("Supplier1", true);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    this.mvc
        .perform(post("/suppliers/edit/" + supplier.getId())
            .param("name", "New Supplier 1")
            .param("isImporter", "false"))
        .andExpect(redirectedUrl("/suppliers/all"));

  }

  @Test
  @WithMockUser
  public void deletePost_deletesSupplier() throws Exception {
    Supplier supplier = getSupplierEntity("Supplier1", true);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    this.mvc
        .perform(post("/suppliers/delete/" + supplier.getId()));

    Assert.assertEquals(0, this.supplierRepository.count());
  }

  @Test(expected = Exception.class)
  @WithMockUser
  public void deletePost_throwsExceptionWhenInvalidData() throws Exception {
    Supplier supplier = getSupplierEntity("Supplier1", true);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    this.mvc
        .perform(post("/suppliers/delete/" + "InvalidID"));
  }

  @Test
  @WithMockUser
  public void deletePost_returnsCorrectView() throws Exception {
    Supplier supplier = getSupplierEntity("Supplier1", true);

    supplier = this.supplierRepository.saveAndFlush(supplier);

    this.mvc
        .perform(post("/suppliers/delete/" + supplier.getId()))
        .andExpect(redirectedUrl("/suppliers/all"));
  }


  @Test
  @WithMockUser
  public void allGet_returnsAllSuppliers() throws Exception {
    this.mvc
        .perform(get("/suppliers/all"))
        .andExpect(model().attributeExists("suppliers"));
  }

  @Test
  @WithMockUser
  public void allGet_returnsCorrectView() throws Exception {

    this.mvc.perform(get("/suppliers/all"))
        .andExpect(view().name("all-suppliers"));
  }

  @Test
  @WithMockUser
  public void fetchGet_returnsCorrectResult() throws Exception {
    Supplier first = getSupplierEntity("Supplier1", true);

    first = this.supplierRepository.saveAndFlush(first);

    Supplier second = getSupplierEntity("Supplier2", false);

    first = this.supplierRepository.saveAndFlush(first);
    second = this.supplierRepository.saveAndFlush(second);

    String result = this.mvc
        .perform(get("/suppliers/fetch"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    Supplier[] suppliers = this.gson.fromJson(result, Supplier[].class);


    Supplier actualFirst = this.supplierRepository.findAll().get(0);

    Assert.assertEquals(suppliers[0].getId(), actualFirst.getId());
    Assert.assertEquals(suppliers[0].getName(), actualFirst.getName());
    Assert.assertEquals(suppliers[0].getIsImporter(), actualFirst.getIsImporter());
    Assert.assertEquals(suppliers.length, this.supplierRepository.count());
  }

  private Supplier getSupplierEntity(String name, boolean isImporter) {
    Supplier supplier = new Supplier();
    supplier.setName(name);
    supplier.setIsImporter(isImporter);
    return supplier;
  }
}
