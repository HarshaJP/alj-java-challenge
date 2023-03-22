package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.constants.ApplicationConstants;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.DataIssueException;
import jp.co.axa.apidemo.model.EmployeeModel;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class implements the service layer operations for the employee details
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeModel> retrieveEmployees() {
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            logger.info(ApplicationConstants.GET_ALL_ERROR_MSG);
            return employeeModelList;
        }
        employeeModelList = employees.stream().map(employee -> buildEmployeeModel(employee)).collect(Collectors.toList());
        return employeeModelList;
    }


    @Cacheable(cacheNames = "employees", key = "#employeeId")
    public EmployeeModel getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        if (!optEmp.isPresent()) {
            logger.error(ApplicationConstants.NOT_FOUND_ERROR_MSG);
            throw new DataIssueException(ApplicationConstants.NOT_FOUND_ERROR_MSG);
        }
        return buildEmployeeModel(optEmp.get());
    }

    public void saveEmployee(EmployeeModel employee) {
        if (employeeRepository.findById(employee.getId()).isPresent()) {
            logger.error(ApplicationConstants.SAVE_EXISTING_ERROR_MSG);
            throw new DataIssueException(ApplicationConstants.SAVE_EXISTING_ERROR_MSG);
        }
        employeeRepository.save(buildEmployeeEntity(employee));
    }

    @CacheEvict(cacheNames = "employees", key = "#employeeId")
    public void deleteEmployee(Long employeeId) {
        try {
            employeeRepository.deleteById(employeeId);
        } catch (EmptyResultDataAccessException exception) {
            logger.error(ApplicationConstants.NOT_FOUND_ERROR_MSG);
            throw new DataIssueException(ApplicationConstants.NOT_FOUND_ERROR_MSG);
        }

    }

    @CacheEvict(value = "employees", allEntries = true)
    public void updateEmployee(EmployeeModel employee, Long employeeId) {
        if (!employeeRepository.findById(employeeId).isPresent()) {
            logger.error(ApplicationConstants.NOT_FOUND_ERROR_MSG);
            throw new DataIssueException(ApplicationConstants.NOT_FOUND_ERROR_MSG);
        }
        employeeRepository.save(buildEmployeeEntity(employee));
    }

    private EmployeeModel buildEmployeeModel(Employee employee) {
        return EmployeeModel.builder().id(employee.getId()).name(employee.getName()).salary(employee.getSalary()).department(employee.getDepartment()).build();
    }

    private Employee buildEmployeeEntity(EmployeeModel employee) {
        return Employee.builder().id(employee.getId()).name(employee.getName()).salary(employee.getSalary()).department(employee.getDepartment()).build();
    }
}