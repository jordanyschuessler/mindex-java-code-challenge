package com.mindex.challenge.data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Compensation {
	private Employee employee;
	private Long salary;
	
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date effectiveDate;
	
	/* Constructors */
	public Compensation() {}

	public Compensation(Employee employee, Long salary, Date effectiveDate) {
		this.employee = employee;
		this.salary = salary;
		this.effectiveDate = effectiveDate;
	}
	
	public Compensation(Compensation compensation) {
		this.employee = compensation.getEmployee();
		this.salary = compensation.getSalary();
		this.effectiveDate = compensation.getEffectiveDate();
	}
	
	/* Getters */
	public Employee getEmployee() {
		return this.employee;
	}
	
	public Long getSalary() {
		return this.salary;
	}
	
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}
	
	/* Setters */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public void setSalary(Long salary) {
		this.salary = salary;
	}
	
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
