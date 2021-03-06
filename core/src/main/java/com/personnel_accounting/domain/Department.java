package com.personnel_accounting.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<Employee> employeeList;

    @OneToMany(mappedBy = "department")
    @ToString.Exclude
    private List<Project> projectList;

    public Department(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
        createDate = new Date(System.currentTimeMillis());
    }
}
