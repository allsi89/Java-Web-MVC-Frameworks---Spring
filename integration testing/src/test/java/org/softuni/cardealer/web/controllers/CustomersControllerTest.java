package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Customer;
import org.softuni.cardealer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomersControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CustomerRepository customerRepository;

  @Before
  public void emptyDb() {
    this.customerRepository.deleteAll();
  }

  @Test
  @WithMockUser
  public void addPost_addCustomerCorrectly() throws Exception {

    this.mvc
        .perform(post("/customers/add")
        .param("name", "Pesho")
        .param("birthDate", "1995-02-15"));

    Customer actual = this.customerRepository.findAll().get(0);

    Assert.assertEquals(1, this.customerRepository.count());
    Assert.assertEquals("Pesho", actual.getName());
    Assert.assertEquals(LocalDate.parse("1995-02-15"), actual.getBirthDate());

  }

  @Test
  @WithMockUser
  public void addPost_returnsCorrectView() throws Exception {

    this.mvc
        .perform(post("/customers/add")
            .param("name", "Pesho")
            .param("birthDate", "1995-02-15"))
    .andExpect(redirectedUrl("all"));
  }

  @Test
  @WithMockUser
  public void allGet_returnsAllCustomersCorrectly() throws Exception {
    this.mvc
        .perform(get("/customers/all"))
        .andExpect(model().attributeExists("customers"));
  }

  @Test
  @WithMockUser
  public void allGet_returnsCorrectView() throws Exception {

    this.mvc
        .perform(get("/customers/all"))
        .andExpect(view().name("all-customers"));
  }
}
