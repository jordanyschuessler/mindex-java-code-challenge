package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.util.RandomNumberGeneratorUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureIdUrl;
    
    private final String NO_EMPLOYEE_ID_FOUND = "No employee with employeeId [{id}] could be found";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    
    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        
        reportingStructureIdUrl = "http://localhost:" + port + "/reporting_structure/{id}";
    }
    
    @Test
    public void testReportingStructureRead() {
    	
    	/* We'll generate 7 employees for this */
    	List<Employee> generatedEmployees = new ArrayList<Employee>();
    	for(int i = 0; i <= 7; i++) {
            Employee createdEmployee = restTemplate.postForEntity(employeeUrl, RandomNumberGeneratorUtil.generateRandomEmployee(), Employee.class).getBody();
            generatedEmployees.add(createdEmployee);
    	}
    	    	
    	Employee indexZeroEmployee = generatedEmployees.get(0);
    	
    	/* Verify an employee here has no direct reports yet and that the employee sent back is correct */
    	ReportingStructure zeroReportStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, indexZeroEmployee.getEmployeeId()).getBody();
    	assertReportingStructure(indexZeroEmployee, 0, zeroReportStructure);
    	
    	/* Now we'll assign direct reports to these employees, based on the following structure (Main Employee : Employee who Directly Reports to them):
    	 * Index 0: Index 1 
    	 * Index 0: Index 2
    	 * Index 0: Index 6 
    	 * Index 2: Index 3
    	 * Index 2: Index 6
    	 * Index 2: Index 7
    	 * Index 4: Index 3
    	 * Index 5: Index 6
    	 * 
    	 * Total Counts:
    	 * Index 0 has 5 (1, 2, 6, 3, 7)
    	 * Index 2 has 3 (3, 6, 7)
    	 * Index 4 has 1 (3)
    	 * Index 5 has 1 (6)
    	 */
    	List<Employee> indexZeroDirectReports = new ArrayList<Employee>();
    	List<Employee> indexTwoDirectReports = new ArrayList<Employee>();
    	List<Employee> indexFourDirectReports = new ArrayList<Employee>();
    	List<Employee> indexFiveDirectReports = new ArrayList<Employee>();

    	indexFiveDirectReports.add(generatedEmployees.get(6));
    	Employee indexFiveEmployee = generatedEmployees.get(5);
    	indexFiveEmployee.setDirectReports(indexFiveDirectReports);
    	indexFiveEmployee = updateEmployee(indexFiveEmployee);
    	
    	indexFourDirectReports.add(generatedEmployees.get(3));
    	Employee indexFourEmployee = generatedEmployees.get(4);
    	indexFourEmployee.setDirectReports(indexFourDirectReports);
    	indexFourEmployee = updateEmployee(indexFourEmployee);
    	
    	indexTwoDirectReports.add(generatedEmployees.get(3));
    	indexTwoDirectReports.add(generatedEmployees.get(6));
    	indexTwoDirectReports.add(generatedEmployees.get(7));
    	Employee indexTwoEmployee = generatedEmployees.get(2);
    	indexTwoEmployee.setDirectReports(indexTwoDirectReports);
    	indexTwoEmployee = updateEmployee(indexTwoEmployee);

    	indexZeroDirectReports.add(generatedEmployees.get(1));
    	indexZeroDirectReports.add(indexTwoEmployee);
    	indexZeroDirectReports.add(generatedEmployees.get(6));
    	indexZeroEmployee.setDirectReports(indexZeroDirectReports);
    	indexZeroEmployee = updateEmployee(indexZeroEmployee);
    	
    	/* Verify the number of direct reports and the respective Employees */
    	ReportingStructure indexZeroReportStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, indexZeroEmployee.getEmployeeId()).getBody();
    	ReportingStructure indexTwoReportStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, indexTwoEmployee.getEmployeeId()).getBody();
    	ReportingStructure indexFourReportStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, indexFourEmployee.getEmployeeId()).getBody();
    	ReportingStructure indexFiveReportStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, indexFiveEmployee.getEmployeeId()).getBody();
    	    	
    	assertReportingStructure(indexZeroEmployee, 5, indexZeroReportStructure);
    	assertReportingStructure(indexTwoEmployee, 3, indexTwoReportStructure);
    	assertReportingStructure(indexFourEmployee, 1, indexFourReportStructure);
    	assertReportingStructure(indexFiveEmployee, 1, indexFiveReportStructure);
    	
    	/* Verify that a non-existent employeeId returns a 404 error */
    	String nonExistentEmployeeIdError = restTemplate.getForEntity(reportingStructureIdUrl, String.class, "12345").getBody();
    	assertEquals(NO_EMPLOYEE_ID_FOUND.replace("{id}", "12345"), nonExistentEmployeeIdError);
    }
    
    private static void assertReportingStructure(Employee expectedEmployee, int expectedNumberOfReports, ReportingStructure actual)
    {
    	assertEmployeeEquivalence(expectedEmployee, actual.getEmployee());
    	assertEquals(expectedNumberOfReports, actual.getNumberOfReports());
    }
    
    /* Taken from EmployeeServiceImplTest*/
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
    
    /* Taken from EmployeeServiceImplTest*/
    private Employee updateEmployee(Employee employee) {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        return restTemplate.exchange(employeeIdUrl,
                HttpMethod.PUT,
                new HttpEntity<Employee>(employee, headers),
                Employee.class,
                employee.getEmployeeId()).getBody();
    }
}
