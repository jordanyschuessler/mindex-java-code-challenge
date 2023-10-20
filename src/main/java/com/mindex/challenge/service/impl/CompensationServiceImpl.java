package com.mindex.challenge.service.impl;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

	@Autowired
	private CompensationRepository compensationRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Compensation create(Compensation compensation) {
		LOG.debug("Inserting Compensation: [{}]", compensation);
		/* Check to make sure the Employee is valid - It's possible just an employeeId was provided */
		Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployee().getEmployeeId());
		if(employee == null) {
			throw new IllegalArgumentException("No Employee could be found for employeeId [" + compensation.getEmployee().getEmployeeId() + "]");
		}
		
		compensation.setEmployee(employee); //Set the Employee to be the newly retrieved Employee
		
		return compensationRepository.insert(compensation);
	}

	@Override
	public Compensation read(String employeeId) {
		LOG.debug("Finding Compensation for employee with id [" + employeeId + "]");
		Compensation comp = compensationRepository.findByEmployeeEmployeeId(employeeId);
		if(comp == null) {
			throw new IllegalArgumentException("No Compensation could be found for employee with id [" + employeeId + "]");
		}
		return comp;
	}

}
