package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.constants.ApplicationConstants;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.DataIssueException;
import jp.co.axa.apidemo.model.EmployeeModel;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class EmployeeServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeServiceImpl employeeService;

    Employee employee;

    EmployeeModel employeeModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        employee = Employee.builder().id(1L).name("test1").salary(10000).department("test-dept").build();
        employeeModel = EmployeeModel.builder().id(1L).name("test1").salary(10000).department("test-dept").build();
    }

    @Test
    public void testRetrieveEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(employeeRepository.findAll()).thenReturn(employeeList);
        List<EmployeeModel> employeeModelList = employeeService.retrieveEmployees();

        assertEquals(1, employeeModelList.size());
        assertEquals(1L, employeeModelList.get(0).getId().longValue());
        assertEquals("test1", employeeModelList.get(0).getName());
        assertEquals(10000, employeeModelList.get(0).getSalary().intValue());
        assertEquals("test-dept", employeeModelList.get(0).getDepartment());
    }

    @Test
    public void testRetrieveEmployeesForEmptyList() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        List<EmployeeModel> employeeModelList = employeeService.retrieveEmployees();
        assertEquals(0, employeeModelList.size());
    }

    @Test
    public void testRetrieveEmployeesForException() {
        when(employeeRepository.findAll()).thenThrow(new DataIssueException("test exception message"));
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        try {
            employeeModelList = employeeService.retrieveEmployees();
        } catch (DataIssueException ex) {
            assertEquals("test exception message", ex.getMessage());
        }
        assertEquals(0, employeeModelList.size());
    }

    @Test
    public void testGetEmployee() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        EmployeeModel employeeModel = employeeService.getEmployee(1L);
        assertEquals(1L, employeeModel.getId().longValue());
        assertEquals("test1", employeeModel.getName());
        assertEquals(10000, employeeModel.getSalary().intValue());
        assertEquals("test-dept", employeeModel.getDepartment());
    }

    @Test
    public void testGetEmployeeForEmpty() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            employeeService.getEmployee(1L);
        } catch (DataIssueException ex) {
            assertEquals(ApplicationConstants.NOT_FOUND_ERROR_MSG, ex.getMessage());
        }
    }

    @Test
    public void testSaveEmployee() {
        when(employeeRepository.save(any())).thenReturn(employee);
        employeeService.saveEmployee(employeeModel);
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    public void testSaveEmployeeForException() {
        when(employeeRepository.findById(any())).thenReturn(Optional.ofNullable(employee));
        when(employeeRepository.save(any())).thenReturn(employee);
        try {
            employeeService.saveEmployee(employeeModel);
        } catch (DataIssueException ex) {
            assertEquals(ApplicationConstants.SAVE_EXISTING_ERROR_MSG, ex.getMessage());
        }
        verify(employeeRepository, times(0)).save(any());
    }

    @Test
    public void testDeleteByEmployee() {
        doNothing().when(employeeRepository).deleteById(anyLong());
        employeeService.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testDeleteByEmployeeForException() {
        doThrow(new EmptyResultDataAccessException(1)).when(employeeRepository).deleteById(anyLong());
        try {
            employeeService.deleteEmployee(1L);
        } catch (DataIssueException ex) {
            assertEquals(ApplicationConstants.NOT_FOUND_ERROR_MSG, ex.getMessage());
        }
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testUpdateEmployee() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any())).thenReturn(employee);
        employeeService.updateEmployee(employeeModel, 1L);
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateEmployeeForException() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(employeeRepository.save(any())).thenReturn(employee);
        try {
            employeeService.updateEmployee(employeeModel,1L );
        } catch (DataIssueException ex) {
            assertEquals(ApplicationConstants.NOT_FOUND_ERROR_MSG, ex.getMessage());
        }
        verify(employeeRepository, times(0)).save(any());
    }

}
