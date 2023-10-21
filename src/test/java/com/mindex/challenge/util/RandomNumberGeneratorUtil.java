package com.mindex.challenge.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.impl.CompensationServiceImplTest;

public class RandomNumberGeneratorUtil {
	
    private static final Logger LOG = LoggerFactory.getLogger(RandomNumberGeneratorUtil.class);

	
	public static Employee generateRandomEmployee() {
		Employee employee = new Employee();
		employee.setFirstName(generateRandomString(5,11));
		employee.setLastName(generateRandomString(5,11));
		employee.setDepartment(generateRandomString(5,11));
		employee.setPosition(generateRandomString(5,11));
		
		return employee;
	}
	
	public static Compensation generateRandomCompensation(Employee employee) {
		Compensation compensation = new Compensation();
		compensation.setEmployee(employee);
		compensation.setSalary(Long.valueOf(generateRandomInt(0,2000000)));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,generateRandomInt(2000,2024));
		cal.set(Calendar.MONTH,generateRandomInt(0,12));
		cal.set(Calendar.DAY_OF_MONTH, generateRandomInt(1,32));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		compensation.setEffectiveDate(cal.getTime());
		
		return compensation;
	}
	
	/* Generates a random string between min (inclusive) and max (exclusive) 
	 * For each character, generates a random int between 0 and 26 (offset by the int value of 'a'), then casts that to a char */
	public static String generateRandomString(int min, int max) {
		String str = "";
		int randStrLength = generateRandomInt(min,max);
		for(int i = 0; i <= randStrLength; i++)
		{
			char c = (char) (generateRandomInt(0,26) + 'a');
			str += c;
		}
		return str;
	}
	
	public static int generateRandomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
}
