package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findByEmail(String email);

  // index param を用いたJPQLを使用してカスタムクエリを定義
  // JPQLを用いており、SQLクエリを用いているわけではない、よってクラス名やクラスの変数で検索をかける
  @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
  Optional<Employee> findByJPQL(String firstName, String lastName);

  // named param を用いたJPQLを使用してカスタムクエリを定義
  // JPQLを用いており、SQLクエリを用いているわけではない、よってクラス名やクラスの変数で検索をかける
  // named paramを用いる場合、クエリ文に渡される文字は =:text の形式を用いる
  // named paramを用いる場合、interfaceメソッドの引数には@Param("text")アノテーションをつける
  @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
  Optional<Employee> findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

  // JPQLを使わずネイティブのSQLクエリのindexを用いたメソッドの定義
  // テーブル名とカラム名はDBテーブルで用いられるように小文字とアンダースコアを用いたクエリに変更されている
  @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
  Optional<Employee> findByNativeSQL(String firstName, String lastName);

  // JPQLを使わずネイティブのSQLクエリのnamed parametersを用いたメソッドの定義
  // テーブル名とカラム名はDBテーブルで用いられるように小文字とアンダースコアを用いたクエリに変更されている
  @Query(value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName", nativeQuery = true)
  Optional<Employee> findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
