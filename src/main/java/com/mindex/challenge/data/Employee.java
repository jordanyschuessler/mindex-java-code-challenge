package com.mindex.challenge.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Employee {
	@Id
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<Employee> directReports;

    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getDirectReports() {
        return directReports != null ? directReports : new ArrayList<Employee>();
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("employeeId: ").append(this.employeeId == null ? "" : this.employeeId).append("\n");
    	sb.append("First Name: ").append(this.firstName == null ? "" : this.firstName).append("\n");
    	sb.append("Last Name: ").append(this.lastName == null ? "" : this.lastName).append("\n");
    	sb.append("Position: ").append(this.position == null ? "" : this.position).append("\n");
    	sb.append("Department: ").append(this.department == null ? "" : this.department).append("\n");
    	sb.append("DirectReports: ");
    	for(Employee emp : getDirectReports()) {
    		sb.append(emp.getEmployeeId() + ", ");
    	}
    	sb.delete(sb.length()-2, sb.length()).append("\n");
    	return sb.toString();
    }
}
