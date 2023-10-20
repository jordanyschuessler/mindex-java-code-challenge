package com.mindex.challenge.service.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
	
	@Override
	public ReportingStructure read(String id) throws IllegalArgumentException{
		/* Get the employee first*/
		Employee employee = employeeRepository.findByEmployeeId(id);
		if(employee == null) {
			throw new IllegalArgumentException("No employee with the given id [" + id + "] could be found");
		}
		
		//Set<String> usedEmployeeIds = new HashSet<String>();
		
		//List<Employee> directReports = employee.getDirectReports();
		/* First attempt used recursion. Had some issues with loading employees, plus it's less efficient than iterating.
	
		if(directReports != null && !directReports.isEmpty()) { //Only check the direct report count if there actually are any direct reports
			numberOfReports = getDirectReportsCnt(employee, usedEmployeeIds);
		}
		*/				
		
		//By using a LinkedHashSet, we can guarantee we never count an employee twice
		Set<Employee> directReports = new LinkedHashSet<Employee>(employee.getDirectReports());
		
		Iterator<Employee> itr = directReports.iterator();
		while(itr.hasNext()) {
			Employee nextEmp = itr.next();
			nextEmp = employeeRepository.findByEmployeeId(nextEmp.getEmployeeId()); //DirectReport Employees are lazy-loaded. Load the read of their info
			directReports.addAll(nextEmp.getDirectReports());
		}
		
		return new ReportingStructure(employee, directReports.size());
	}
	
	/* For each employee in the employee's direct reports, get those employee's direct reports. 
	 * Make sure not to include any employee that has already been added. Using a separate set to keep track.
	 * (I.E. 'A' has directReports 'B' and 'C'. 'B' and 'C' both have directReports 'D'. 3 direct reports, not 4.
	 */
/*	private int getDirectReportsCnt(Employee employee, Set<String> usedEmployeeIds) {
		usedEmployeeIds.add(employee.getEmployeeId()); //Add our current employee to the 'do not use again' list
		List<Employee> directReports = employee.getDirectReports();
		if(directReports == null || directReports.isEmpty()) return 1; //If this employee doesn't have any reporting employee, final user. Return 1
		
		int count = 0;
		for(Employee emp : directReports) {
			Employee loadedEmployee = employeeRepository.findByEmployeeId(emp.getEmployeeId()); //Employees are lazy-loaded. Load the read of their info
			if(!usedEmployeeIds.contains(loadedEmployee.getEmployeeId())) { //If we haven't counted this employee yet, count them
				count += getDirectReportsCnt(loadedEmployee, usedEmployeeIds);
			}
		}
				
		return count;
	}*/

}
