package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
// import org.assertj.core.api.Assertions;
// importをstaticにしてassertThatをimportするとstaticメソッドとして呼び出し可能
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

  @Autowired
  private EmployeeRepository employeeRepository;

  private Employee employee;

  @BeforeEach
  public void setup() {
    employee = Employee.builder()
      .firstName("Ramesh")
      .lastName("Fadatare")
      .email("ramesh@gmail.com")
      .build();
  }

  // JUnit test for save employee operation
  // given_when_thenでテスト名を組み立てる
  // @DisplayNameアノテーションを用いない場合、メソッド名がテスト名になる
  // @DisplayNameアノテーションを用いる場合、引数がテスト名になる
  @DisplayName("JUnit test for save employee operation")
  @Test
  public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .firstName("Ramesh")
    //      .lastName("Ramesh")
    //      .email("ramesh@gmail.com")
    //      .build();

    // when - action or the behavior that we are going to test
    Employee result = employeeRepository.save(employee);


    // then - verify the output
    // Assertions.assertThat(result).isNotNull();
    // Assertions.assertThat(result.getId()).isGreaterThan(0);
    // import static org.assertj.core.api.Assertions.assertThat; にすると、以下の様にいきなりassertThat()から開始できる

    assertThat(result).isNotNull();
    assertThat(result.getId()).isGreaterThan(0);
  }

  // JUnit test for get all employees operation
  // Employeeを2つ生成・保存して全件取得した場合のテスト
  @DisplayName("JUnit test for get all employees operation")
  @Test
  public void givenEmployeeList_whenFindAll_thenEmployeeList() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .firstName("Ramesh")
    //      .lastName("Ramesh")
    //      .email("ramesh@gmail.com")
    //      .build();

    Employee employee2 = Employee.builder()
      .firstName("John")
      .lastName("Cana")
      .email("cana@gmail.com")
      .build();

    employeeRepository.save(employee);
    employeeRepository.save(employee2);

    // when - action or the behaviour
    List<Employee> employeeList = employeeRepository.findAll();

    // then - verify the output
    assertThat(employeeList).isNotNull();
    assertThat(employeeList.size()).isEqualTo(2);

  }

  // 前回作成したライブテンプレートを使用する
  // JUnit test for get employee by id operation
  @DisplayName("JUnit test for get employee by id operation")
  @Test
  public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .lastName("Ramesh")
    //      .firstName("Ramesh")
    //      .email("ramesh@gmail.com")
    //      .build();
    employeeRepository.save(employee);

    // when - action or the behaviour

    // findById()メソッドはjava.util.Optionalの抽象クラスを返却するため、最後にget()メソッドをつける
    // id（Primary）はsave()メソッド実行時にemployeeオブジェクトにアサインされる
    Employee result = employeeRepository.findById(employee.getId()).get();
    // then - verify the output

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(employee.getId());
    assertThat(result.getFirstName()).isEqualTo(employee.getFirstName());
    assertThat(result.getLastName()).isEqualTo(employee.getLastName());
    assertThat(result.getEmail()).isEqualTo(employee.getEmail());
  }

  // JUnit test for get employee by email operation
  @DisplayName("JUnit test for get employee by email operation")
  @Test
  public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .firstName("Ramesh")
    //      .lastName("Fadatare")
    //      .email("ramesh@gmail.com")
    //      .build();
    employeeRepository.save(employee);

    // when - action or the behaviour
    Employee result = employeeRepository.findByEmail(employee.getEmail()).get();

    // then - verify the output
    assertThat(result).isNotNull();
    assertThat(result.getEmail()).isEqualTo(employee.getEmail());

  }

  // JUnit test for update employee operation
  @DisplayName("JUnit test for update employee operation")
  @Test
  public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .firstName("Ramesh")
    //      .lastName("Fadatare")
    //      .email("ramesh@gmail.com")
    //      .build();
    employeeRepository.save(employee);

    // when - action or the behaviour
    Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
    savedEmployee.setEmail("ram@gmail.com");
    savedEmployee.setFirstName("Ram");
    Employee updatedEmployee = employeeRepository.save(savedEmployee);

    // then - verify the output
    assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");
    assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");

  }

  // JUnit test for delete employee operation
  @DisplayName("JUnit test for delete employee operation")
  @Test
  public void givenEmployeeObject_whenDelete_thenEmpty() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .firstName("Ramesh")
    //      .lastName("Fadatare")
    //      .email("ramesh@gmail.com")
    //      .build();
    employeeRepository.save(employee);

    // when - action or the behaviour
    // id指定でもオブジェクト指定でもどちらでも良い
    // employeeRepository.delete(employee);
    employeeRepository.deleteById(employee.getId());

    Optional<Employee> result = employeeRepository.findById(employee.getId());

    // then - verify the output
    assertThat(result).isEmpty();

  }

  // JUnit test for custom query using JPQL with index
  @DisplayName("JUnit test for custom query using JPQL with index")
  @Test
  public void givenFirstNameAndLastName_whenFIndByJPQL_thenReturnEmployeeObject() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .firstName("Ramesh")
    //      .lastName("Fadatare")
    //      .email("ramesh@gmail.com")
    //      .build();
    employeeRepository.save(employee);

    String firstName = "Ramesh";
    String lastName = "Fadatare";

    // when - action or the behaviour
    Employee result = employeeRepository.findByJPQL(firstName, lastName).get();

    // then - verify the output
    assertThat(result).isNotNull();
    assertThat(result.getFirstName()).isEqualTo(firstName);
    assertThat(result.getLastName()).isEqualTo(lastName);
  }

  // JUnit test for custom query using JPQL with Named params
  @DisplayName("JUnit test for custom query using JPQL with NamedParams")
  @Test
  public void givenFirstNameAndLastName_whenFIndByJPQLNamedParams_thenReturnEmployeeObject() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .firstName("Ramesh")
    //      .lastName("Fadatare")
    //      .email("ramesh@gmail.com")
    //      .build();
    employeeRepository.save(employee);

    String firstName = "Ramesh";
    String lastName = "Fadatare";

    // when - action or the behaviour
    Employee result = employeeRepository.findByJPQLNamedParams(firstName, lastName).get();

    // then - verify the output
    assertThat(result).isNotNull();
    assertThat(result.getFirstName()).isEqualTo(firstName);
    assertThat(result.getLastName()).isEqualTo(lastName);
  }

  // JUnit test for custom query using native SQL with index
  @DisplayName("JUnit test for custom query using native SQL with index")
  @Test
  public void givenFirstNameAndLast_whenFindByNativeSQL_thenReturnEmployeeObject() {
    // given - precondition or setup
    //    Employee employee = Employee.builder()
    //      .firstName("Ramesh")
    //      .lastName("Fadatare")
    //      .email("ramesh@gmail.com")
    //      .build();
    employeeRepository.save(employee);

    String firstName = "Ramesh";
    String lastName = "Fadatare";

    // when - action or the behaviour
    Employee result = employeeRepository.findByNativeSQL(firstName, lastName).get();

    // then - verify the output
    assertThat(result).isNotNull();
    assertThat(result.getFirstName()).isEqualTo(firstName);
    assertThat(result.getLastName()).isEqualTo(lastName);
  }


  // JUnit test for custom query using native SQL with named params
  @DisplayName("JUnit test for custom query using native SQL with named params")
  @Test
  public void givenFirstNameAndLast_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
    // given - precondition or setup
//    Employee employee = Employee.builder()
//      .firstName("Ramesh")
//      .lastName("Fadatare")
//      .email("ramesh@gmail.com")
//      .build();
    employeeRepository.save(employee);

    String firstName = "Ramesh";
    String lastName = "Fadatare";

    // when - action or the behaviour
    Employee result = employeeRepository.findByNativeSQLNamed(firstName, lastName).get();

    // then - verify the output
    assertThat(result).isNotNull();
    assertThat(result.getFirstName()).isEqualTo(firstName);
    assertThat(result.getLastName()).isEqualTo(lastName);
  }
}
