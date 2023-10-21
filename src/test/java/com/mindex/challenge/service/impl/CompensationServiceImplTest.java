package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.util.RandomNumberGeneratorUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImplTest.class);
	
	private String employeeUrl;
	
	private String compensationUrl;
	private String compensationIdUrl;
	    
	private final String COMP_EXISTS_ERROR = "Compensation for employee with employeeId [{id}] already exists";
	private final String NO_EMPLOYEE_ID_FOUND = "No employee with employeeId [{id}] could be found";
	private final String NO_COMP_EXISTS = "No compensation for employee with employeeId [{id}] could be found";
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	
	@Before
	public void setup() {
	    employeeUrl = "http://localhost:" + port + "/employee";
	    
	    compensationUrl = "http://localhost:" + port + "/compensation";
	    compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";

	}

	@Test
    public void testCompensationCreate() {
		/* Create an employee to generate Compensation for */
		Employee compEmployeeTest = restTemplate.postForEntity(employeeUrl, RandomNumberGeneratorUtil.generateRandomEmployee(), Employee.class).getBody();
		Compensation compensationTest = RandomNumberGeneratorUtil.generateRandomCompensation(compEmployeeTest);
		
		Compensation postedCompensation = restTemplate.postForEntity(compensationUrl, compensationTest, Compensation.class).getBody();

		assertCompensationEquivalence(compensationTest, postedCompensation);
		
		/* Verify that you cannot add compensation for an employee twice */
		compensationTest.setSalary(new Long(1234567));
		String compAlreadyExists = restTemplate.postForEntity(compensationUrl, compensationTest, String.class).getBody();
		assertEquals(COMP_EXISTS_ERROR.replace("{id}", compEmployeeTest.getEmployeeId()), compAlreadyExists);
		
		/* Verify that you cannot add compensation for an employee that doesn't exist */
		compEmployeeTest.setEmployeeId("12345");
		compensationTest.setEmployee(compEmployeeTest);
		String noEmployeeExists = restTemplate.postForEntity(compensationUrl, compensationTest, String.class).getBody();
		assertEquals(NO_EMPLOYEE_ID_FOUND.replace("{id}", "12345"), noEmployeeExists);
		
		/* Verify that salary must be greater than 0 */
		Employee secondCompEmployeeTest = restTemplate.postForEntity(employeeUrl, RandomNumberGeneratorUtil.generateRandomEmployee(), Employee.class).getBody();
		Compensation badSalaryTest = RandomNumberGeneratorUtil.generateRandomCompensation(secondCompEmployeeTest);
		badSalaryTest.setSalary(new Long (-12345));
		String salaryValidationFaliure = restTemplate.postForEntity(compensationUrl, badSalaryTest, String.class).getBody();
		Assert.assertTrue(salaryValidationFaliure.contains("Bad Request")
				&& salaryValidationFaliure.contains("compensation.salary")
				&& salaryValidationFaliure.contains("must be greater than or equal to 0"));
	}
	
	@Test
	public void testCompensationRead() {
		/* Create an employee to generate Compensation for */
		Employee compEmployeeTest = restTemplate.postForEntity(employeeUrl, RandomNumberGeneratorUtil.generateRandomEmployee(), Employee.class).getBody();
		Compensation compensationTest = RandomNumberGeneratorUtil.generateRandomCompensation(compEmployeeTest);
		
		Compensation postedCompensation = restTemplate.postForEntity(compensationUrl, compensationTest, Compensation.class).getBody();

		/* Verify retrieved compensation equals posted compensation*/
		Compensation retrievedCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, postedCompensation.getEmployee().getEmployeeId()).getBody();
		assertCompensationEquivalence(postedCompensation, retrievedCompensation);
		
		/* Verify the error for an employeeId without Compensation posted */
		String noCompFound = restTemplate.getForEntity(compensationIdUrl, String.class, "67890").getBody();
		assertEquals(NO_COMP_EXISTS.replace("{id}", "67890"), noCompFound);
	}
	
	private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
		assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
		assertEquals(expected.getSalary(), actual.getSalary());
		assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
	}
	
    /* Taken from EmployeeServiceImplTest*/
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
