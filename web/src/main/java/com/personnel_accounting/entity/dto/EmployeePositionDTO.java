package com.personnel_accounting.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EmployeePositionDTO {
    private Long id;
    private EmployeeDTO employee;
    private ProjectDTO project;
    private DepartmentDTO department;
    private PositionDTO position;
    private boolean isActive;
    private String start_date;
    private String end_date;
}
