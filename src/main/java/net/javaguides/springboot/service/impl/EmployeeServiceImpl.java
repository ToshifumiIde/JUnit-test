package net.javaguides.springboot.service.impl;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Employee saveEmployee(Employee employee) {
    // メール重複のバリデーション
    Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
    if (savedEmployee.isPresent()) {
      throw new ResourceNotFoundException("Employee already exists with given email:" + employee.getEmail());
    }

    return employeeRepository.save(employee);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  @Override
  public Optional<Employee> getEmployeeById(long id) {
    return employeeRepository.findById(id);
  }

  @Override
  public Employee updateEmployee(Employee updatedEmployee) {
    return employeeRepository.save(updatedEmployee);
  }

  @Override
  public void deleteEmployeeById(long id) {
    employeeRepository.deleteById(id);
  }
}
