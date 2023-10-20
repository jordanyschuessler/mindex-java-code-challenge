package com.mindex.challenge.data;

public class ReportingStructure {
	private Employee employee;
	private int numberOfReports;
	
	/* Constructors */
	public ReportingStructure() {}
	
	public ReportingStructure(Employee employee, int numberOfReports) {
		this.employee = employee;
		this.numberOfReports = numberOfReports;
	}

	/* Copy Constructor */
	public ReportingStructure(ReportingStructure reportingStructure) {
		this.employee = reportingStructure.getEmployee();
		this.numberOfReports = reportingStructure.getNumberOfReports();
	}
	
	/* Getters */
	public Employee getEmployee() {
		return this.employee;
	}
	
	public int getNumberOfReports() {
		return this.numberOfReports;
	}
	
	/* Setters */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public void setNumberOfReports(int numberOfReports) {
		this.numberOfReports = numberOfReports;
	}
}
