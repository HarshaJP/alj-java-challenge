package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.model.EmployeeModel;

import java.util.List;

/**
 * Class represents service layer for the Employee details
 */
public interface EmployeeService {

    /**
     * Method is responsible for fetching all the employees from the database
     * @return
     */
    public List<EmployeeModel> retrieveEmployees();

    /**
     * Method is responsible for fetching the employee details using employee-id
     * @param employeeId
     * @return
     */
    public EmployeeModel getEmployee(Long employeeId);

    /**
     * Method is responsible for persisting the employee details
     * @param employee
     */
    public void saveEmployee(EmployeeModel employee);

    /**
     * Method is responsible for deleting the employee details using employee-id
     * @param employeeId
     */
    public void deleteEmployee(Long employeeId);

    /**
     * Method is responsible for updating the existing employee details
     * @param employee
     * @param employeeId
     */
    public void updateEmployee(EmployeeModel employee, Long employeeId);
}