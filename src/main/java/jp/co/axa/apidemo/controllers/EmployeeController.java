package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.model.EmployeeModel;
import jp.co.axa.apidemo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Employee Controller is the entry point for the employee related APIs
 */
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);


    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Method returns the list of employees
     * @return
     */
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeModel>> getEmployees() {
        List<EmployeeModel> employees = employeeService.retrieveEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Method is responsible for fetching employee details using employee id
     * @param employeeId
     * @return
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeModel> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        EmployeeModel employeeModel= employeeService.getEmployee(employeeId);
        return new ResponseEntity(employeeModel, HttpStatus.OK);
    }

    /**
     * Method is responsible for persisting the employee details
     * @param employee
     * @return
     */
    @PostMapping("/employees")
    public ResponseEntity saveEmployee(@Valid @RequestBody EmployeeModel employee) {
        employeeService.saveEmployee(employee);
        logger.info("Employee Saved Successfully");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Method is responsible for deleting the employee details using employee-id in the application
     * @param employeeId
     * @return
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        logger.info("Employee Deleted Successfully");
        return new ResponseEntity(HttpStatus.OK);

    }

    /**
     * Method is responsible for updating the existing employee details
     * @param employee
     * @return
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity updateEmployee(@RequestBody EmployeeModel employee,
                               @PathVariable(name="employeeId")Long employeeId){
        employeeService.updateEmployee(employee, employeeId );
        logger.info("Employee updated Successfully");
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

}
