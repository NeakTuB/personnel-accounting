package com.personnel_accounting.controller.rest;

import com.personnel_accounting.department.DepartmentService;
import com.personnel_accounting.domain.Department;
import com.personnel_accounting.entity.dto.DepartmentDTO;
import com.personnel_accounting.entity.dto.ProjectDTO;
import com.personnel_accounting.exception.ActiveStatusDataException;
import com.personnel_accounting.exception.ExistingDataException;
import com.personnel_accounting.exception.OperationExecutionException;
import com.personnel_accounting.pagination.entity.Page;
import com.personnel_accounting.pagination.entity.PagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/department")
public class DepartmentRESTController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/add")
    public ResponseEntity<?> addDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        Department department = conversionService.convert(departmentDTO, Department.class);
        department.setActive(false);
        department.setCreateDate(new Date(System.currentTimeMillis()));

        department = departmentService.addDepartment(department);
        if (department.getId() == null)
            throw new ExistingDataException(messageSource.getMessage("department.error.add", null, null));
        return new ResponseEntity<>(conversionService.convert(department, DepartmentDTO.class), HttpStatus.OK);
    }

    @PostMapping("/get_all/open")
    public ResponseEntity<?> getAllOpenDepartments(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(departmentService.findAllOpen(pagingRequest)
                        .stream().map(department -> conversionService.convert(department, DepartmentDTO.class))
                        .collect(Collectors.toList()),
                pagingRequest.getDraw(), departmentService.getOpenDepartmentCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/get_all/closed")
    public ResponseEntity<?> getAllClosedDepartments(@RequestBody PagingRequest pagingRequest) {
        return new ResponseEntity<>(getPage(departmentService.findAllClosed(pagingRequest)
                        .stream().map(department -> conversionService.convert(department, DepartmentDTO.class))
                        .collect(Collectors.toList()),
                pagingRequest.getDraw(), departmentService.getClosedDepartmentCount().intValue()), HttpStatus.OK);
    }

    @PostMapping("/projects/open/{id}")
    public ResponseEntity<?> getAllOpenProjectsByDepartment(@RequestBody PagingRequest pagingRequest,
                                                            @PathVariable Long id) {
        Department department = departmentService.find(id);
        return new ResponseEntity<>(getPage(departmentService.findOpenProjectsByDepartmentPaginated(pagingRequest, department)
                        .stream().map(project -> conversionService.convert(project, ProjectDTO.class))
                        .collect(Collectors.toList()),
                pagingRequest.getDraw(), departmentService.getOpenProjectsByDepartmentCount(department).intValue()), HttpStatus.OK);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateDepartment(@PathVariable Long id) {
        Department department = departmentService.find(id);
        if (!departmentService.activate(department))
            throw new OperationExecutionException(
                    messageSource.getMessage("department.error.activation", null, null) + " " + id);
        return new ResponseEntity<>(conversionService.convert(department, DepartmentDTO.class), HttpStatus.OK);
    }

    @PutMapping("/inactivate/{id}")
    public ResponseEntity<?> inactivateDepartment(@PathVariable Long id) {
        Department department = departmentService.find(id);
        if (!departmentService.inactivate(department))
            throw new OperationExecutionException(
                    messageSource.getMessage("department.error.deactivation", null, null) + " " + id);
        return new ResponseEntity<>(conversionService.convert(department, DepartmentDTO.class), HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        Department department = departmentService.find(id);
        departmentService.editDepartmentName(department, departmentDTO.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/close/{id}")
    public ResponseEntity<?> closeDepartment(@PathVariable Long id) {
        Department department = departmentService.find(id);
        if (department.isActive())
            throw new ActiveStatusDataException(
                    messageSource.getMessage("department.error.active.status", null, null));
        if (!departmentService.closeDepartment(departmentService.find(id)))
            throw new ActiveStatusDataException(
                    messageSource.getMessage("department.error.close", null, null));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Page<?> getPage(List<?> list, int draw, int count) {
        Page<?> page = new Page<>(list);
        page.setRecordsFiltered(count);
        page.setDraw(draw);
        page.setRecordsTotal(count);
        return page;
    }
}
