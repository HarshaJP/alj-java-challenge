package jp.co.axa.apidemo.controllers;

import com.google.gson.Gson;
import jp.co.axa.apidemo.constants.ApplicationConstants;
import jp.co.axa.apidemo.model.EmployeeModel;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpServerErrorException;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    private final String employeeURI = "/api/v1/employees";

    EmployeeModel employeeModel;

    @Before
    public void setUp() {
        employeeModel = EmployeeModel.builder().id(1L).name("test1").salary(10000).department("test-dept").build();
    }


    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testGetEmployees() throws Exception {
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        employeeModelList.add(employeeModel);
        Mockito.when(employeeService.retrieveEmployees()).thenReturn(employeeModelList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(this.employeeURI).accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModelList)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testGetEmployeesForException() throws Exception {
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        employeeModelList.add(employeeModel);
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(employeeService).retrieveEmployees();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(this.employeeURI).accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModelList)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(ApplicationConstants.SERVER_ERROR_MSG, response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testGetEmployee() throws Exception {
        Mockito.when(employeeService.getEmployee(any())).thenReturn(employeeModel);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(this.employeeURI + "/1").accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModel)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testGetEmployeeForException() throws Exception {
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(employeeService).getEmployee(any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(this.employeeURI + "/1").accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModel)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(ApplicationConstants.SERVER_ERROR_MSG, response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testSaveEmployee() throws Exception {
        doNothing().when(employeeService).saveEmployee(any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(this.employeeURI).accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModel)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testSaveEmployeeForException() throws Exception {
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(employeeService).saveEmployee(any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(this.employeeURI).accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModel)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(ApplicationConstants.SERVER_ERROR_MSG, response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(this.employeeURI + "/1").accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModel)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testDeleteEmployeeForException() throws Exception {
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(employeeService).deleteEmployee(any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(this.employeeURI + "/1").accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModel)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(ApplicationConstants.SERVER_ERROR_MSG, response.getContentAsString());
    }


    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testUpdateEmployee() throws Exception {
        doNothing().when(employeeService).updateEmployee(any(), any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(this.employeeURI + "/1").accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModel)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    public void testUpdateEmployeeForException() throws Exception {
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(employeeService).updateEmployee(any(), any());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(this.employeeURI + "/1").accept(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(employeeModel)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(ApplicationConstants.SERVER_ERROR_MSG, response.getContentAsString());
    }


}
