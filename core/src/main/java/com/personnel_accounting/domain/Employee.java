package com.personnel_accounting.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH}
            , fetch = FetchType.LAZY)
    @JoinColumn(name = "username", unique = true, nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    private Department department;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @ToString.Exclude
    private Profile profile;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private List<EmployeePosition> employeePositionList;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private List<ReportCard> reportCardList;

    public Employee(String name, boolean isActive, User user, Profile profile) {
        this.user = user;
        this.profile = profile;
        this.name = name;
        this.isActive = isActive;
        createDate = new Date(System.currentTimeMillis());
    }

    public Employee(String name, boolean isActive, User user) {
        this.user = user;
        profile = null;
        this.name = name;
        this.isActive = isActive;
        createDate = new Date(System.currentTimeMillis());
        profile = new Profile();
    }
}
