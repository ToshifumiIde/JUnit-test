package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class EmployeeControllerTests {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private EmployeeService employeeService;

  @Autowired
  private ObjectMapper objectMapper;

  // JUnit test for Create Employee REST API
  @DisplayName("JUnit test for Create Employee REST API")
  @Test
  public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
    // given - precondition or setup
    Employee employee = Employee.builder()
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@gmail.com")
      .build();

    given(employeeService.saveEmployee(any(Employee.class)))
      .willAnswer((invocation) -> invocation.getArgument(0));

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
    given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

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
    long employeeId = 1L;
    Employee employee = Employee.builder()
      .id(employeeId)
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@gmail.com")
      .build();
    given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

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
    given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

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
    long employeeId = 1L;
    Employee savedEmployee = Employee.builder()
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@email.com")
      .build();
    Employee updatedEmployee = Employee.builder()
      .firstName("Ram")
      .lastName("Jadhav")
      .email("ram@email.com")
      .build();

    given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
    given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(
      put("/api/employees/{id}", employeeId)
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
    Employee updatedEmployee = Employee.builder()
      .firstName("Ram")
      .lastName("Jadhav")
      .email("ram@email.com")
      .build();

    given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
    given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

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
    long employeeId = 1L;
    willDoNothing().given(employeeService).deleteEmployeeById(employeeId);

    // when - action or the behaviour
    ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

    // then - verify the output
    response
      .andDo(print())
      .andExpect(status().isOk());

  }
}
