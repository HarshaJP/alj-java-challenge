package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;

/**
 * Employee Repository for performing operations on db
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
