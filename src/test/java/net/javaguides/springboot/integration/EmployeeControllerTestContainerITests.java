package net.javaguides.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerTestContainerITests extends AbstractionBaseTest {

//  @Container
//  private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
//    .withUsername("username")
//    .withPassword("password")
//    .withDatabaseName("ems");
//
//  // 動的プロパティを使用して、ロードされたプロパティを選択的にオーバーライド可能
//  @DynamicPropertySource
//  static void dynamicPropertySource(DynamicPropertyRegistry registry){
//    registry.add("spring.datasource.url" , mySQLContainer::getJdbcUrl);
//    registry.add("spring.datasource.username" , mySQLContainer::getUsername);
//    registry.add("spring.datasource.password" , mySQLContainer::getPassword);
//
//  }

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    employeeRepository.deleteAll();
  }

  // JUnit test for Create Employee REST API
  @DisplayName("JUnit test for Create Employee REST API")
  @Test
  public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

//    System.out.println("MySQLContainer user name is "  + mySQLContainer.getUsername());
//    System.out.println("MySQLContainer container name is "  + mySQLContainer.getPassword());
//    System.out.println("MySQLContainer database name is "  + mySQLContainer.getDatabaseName());

    // given - precondition or setup
    Employee employee = Employee.builder()
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@gmail.com")
      .build();

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(
      post("/api/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employee))
    );

    // then - verify the output
    response
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.firstName",
        is(employee.getFirstName())))
      .andExpect(jsonPath("$.lastName",
        is(employee.getLastName())))
      .andExpect(jsonPath("$.email",
        is(employee.getEmail())));
  }

  // JUnit test for Get All Employees REST API
  @DisplayName("JUnit test for Get All Employees REST API")
  @Test
  public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
    // given - precondition or setup
    List<Employee> listOfEmployees = new ArrayList<>();
    listOfEmployees.add(Employee.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").build());
    listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
    employeeRepository.saveAll(listOfEmployees);

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(get("/api/employees"));

    // then - verify the output
    response
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()", is(listOfEmployees.size())))
      .andExpect(jsonPath("$.length()", is(listOfEmployees.size())))
      .andExpect(jsonPath("$.[0].firstName", is(listOfEmployees.get(0).getFirstName())))
      .andExpect(jsonPath("$.[0].lastName", is(listOfEmployees.get(0).getLastName())))
      .andExpect(jsonPath("$.[0].email", is(listOfEmployees.get(0).getEmail())))
      .andExpect(jsonPath("$.[1].firstName", is(listOfEmployees.get(1).getFirstName())))
      .andExpect(jsonPath("$.[1].lastName", is(listOfEmployees.get(1).getLastName())))
      .andExpect(jsonPath("$.[1].email", is(listOfEmployees.get(1).getEmail())));
  }

  // positive scenario - valid employee id
  // JUnit test for Get Employee By Id REST API
  @DisplayName("JUnit test for Get Employee By Id REST API")
  @Test
  public void givenEmployeeId_whenGetEmployeeByID_thenReturnEmployeeObject() throws Exception {
    // given - precondition or setup
    Employee employee = Employee.builder()
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@gmail.com")
      .build();
    employeeRepository.save(employee);

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

    // then - verify the output
    response
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
      .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
      .andExpect(jsonPath("$.email", is(employee.getEmail())));
  }

  // negative scenario - invalid employee id
  // JUnit test for Get Employee By Id REST API
  @DisplayName("JUnit test for Get Employee By Invalid Id REST API")
  @Test
  public void givenInvalidEmployeeId_whenGetEmployeeByID_thenReturnEmpty() throws Exception {
    // given - precondition or setup
    long employeeId = 1L;
    Employee employee = Employee.builder()
      .id(employeeId)
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@gmail.com")
      .build();
    employeeRepository.save(employee);

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

    // then - verify the output
    response
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  // positive scenario - valid employee id
  // JUnit test for update employee REST API - positive scenario
  @DisplayName("JUnit test for update employee REST API - positive scenario")
  @Test
  public void givenEmployeeId_whenUpdateEmployee_thenReturnEmployeeObject() throws Exception {
    // given - precondition or setup
    Employee savedEmployee = Employee.builder()
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@email.com")
      .build();
    employeeRepository.save(savedEmployee);

    Employee updatedEmployee = Employee.builder()
      .firstName("Ram")
      .lastName("Jadhav")
      .email("ram@email.com")
      .build();

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(
      put("/api/employees/{id}", savedEmployee.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedEmployee))
    );

    // then - verify the output
    response
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
      .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
      .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
  }


  // negative scenario - invalid employee id
  // JUnit test for update employee REST API - negative scenario
  @DisplayName("JUnit test for update employee REST API - negative scenario")
  @Test
  public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturn404() throws Exception {
    // given - precondition or setup
    long employeeId = 1L;
    Employee savedEmployee = Employee.builder()
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@email.com")
      .build();
    employeeRepository.save(savedEmployee);

    Employee updatedEmployee = Employee.builder()
      .firstName("Ram")
      .lastName("Jadhav")
      .email("ram@email.com")
      .build();

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(
      put("/api/employees/{id}", employeeId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedEmployee))
    );

    // then - verify the output
    response
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  // JUnit test for delete Employee REST API
  @DisplayName("JUnit test for delete Employee REST API")
  @Test
  public void givenEmployeeId_whenDeleteEmployeeById_thenReturn200() throws Exception {
    // given - precondition or setup
    Employee savedEmployee = Employee.builder()
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@email.com")
      .build();
    employeeRepository.save(savedEmployee);

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

    // then - verify the output
    response
      .andDo(print())
      .andExpect(status().isOk());
  }
}
