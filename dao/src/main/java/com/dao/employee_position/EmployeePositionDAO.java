package com.dao.employee_position;

import com.core.domain.*;

import java.util.List;

public interface EmployeePositionDAO {
    EmployeePosition find(Long id);
    List<EmployeePosition> findAll();
    List<EmployeePosition> findByEmployee(Employee employee);
    List<EmployeePosition> findByPosition(Position position);
    List<EmployeePosition> findByProject(Project project);
    List<EmployeePosition> findByDepartment(Department department);
    List<EmployeePosition> findByActive(boolean isActive);

    EmployeePosition save(EmployeePosition employeePosition);
    EmployeePosition update(EmployeePosition employeePosition);
    boolean removeById(Long id);
    boolean remove(EmployeePosition employeePosition);
}