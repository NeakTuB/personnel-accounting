package com.personnel_accounting.employee;

import com.personnel_accounting.domain.Department;
import com.personnel_accounting.domain.Employee;
import com.personnel_accounting.domain.Profile;
import com.personnel_accounting.domain.User;
import com.personnel_accounting.enums.Role;
import com.personnel_accounting.pagination.entity.Column;
import com.personnel_accounting.pagination.entity.Order;
import com.personnel_accounting.pagination.entity.PagingRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Repository
@Transactional
public class EmployeeDAOImpl implements EmployeeDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Employee find(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Employee.class, id);
    }

    @Override
    public List<Employee> findAll(PagingRequest pagingRequest) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from Employee";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " where concat(id, user.username, name, user.authority.role, user.isActive, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public List<Employee> findAllActiveEmployee(PagingRequest pagingRequest) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from Employee where user.isActive =:active and user.authority.role in (:roles)";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " and concat(id, user.username, name, user.authority.role, user.isActive, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setParameter("active", true);
        query.setParameterList("roles", Arrays.asList(Role.EMPLOYEE, Role.DEPARTMENT_HEAD, Role.PROJECT_MANAGER));
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public List<Employee> findAllActiveAdmins(PagingRequest pagingRequest) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from Employee where user.isActive =:active and user.authority.role =:role";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " and concat(id, user.username, name, user.authority.role, user.isActive, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setParameter("active", true);
        query.setParameter("role", Role.ADMIN);
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public List<Employee> findAllFreeAndActiveEmployees(PagingRequest pagingRequest) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from Employee where user.isActive =:active and department is null";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " and concat(id, user.username, name, user.authority.role, user.isActive, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setParameter("active", true);
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public List<Employee> findAllAssignedAndActiveEmployees(PagingRequest pagingRequest) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from Employee where user.isActive =:active and department is not null";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " and concat(id, user.username, name, user.authority.role, user.isActive, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setParameter("active", true);
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public List<Employee> findAllDismissed(PagingRequest pagingRequest) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from Employee where user.isActive =:active and user.authority.role in (:roles)";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " and concat(id, user.username, name, user.authority.role, user.isActive, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setParameter("active", false);
        query.setParameterList("roles", Arrays.asList(Role.EMPLOYEE, Role.DEPARTMENT_HEAD, Role.PROJECT_MANAGER, Role.ADMIN));
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public Long getEmployeeCount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select count(*) from Employee");
        return (Long) query.getSingleResult();
    }

    @Override
    public Long getActiveEmployeeCount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "select count(*) from Employee where user.isActive =:active and user.authority.role in (:roles)");
        query.setParameterList("roles", Arrays.asList(Role.EMPLOYEE, Role.DEPARTMENT_HEAD, Role.PROJECT_MANAGER));
        query.setParameter("active", true);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long getActiveAdminCount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "select count(*) from Employee where user.isActive =:active and user.authority.role =:role");
        query.setParameter("role", Role.ADMIN);
        query.setParameter("active", true);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long getFreeAndActiveEmployeesCount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "select count(*) from Employee where user.isActive =:active and department is null");
        query.setParameter("active", true);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long getAssignedAndActiveEmployeesCount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "select count(*) from Employee where user.isActive =:active and department is not null");
        query.setParameter("active", true);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long getDismissedEmployeeCount() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "select count(*) from Employee where user.isActive =:active and user.authority.role in (:roles)");
        query.setParameterList("roles", Arrays.asList(Role.EMPLOYEE, Role.DEPARTMENT_HEAD, Role.PROJECT_MANAGER, Role.ADMIN));
        query.setParameter("active", false);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long getEmployeeByDepartmentCount(Department department) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select count(*) from Employee where department =:department");
        query.setParameter("department", department);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<Employee> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where name = :name");
        query.setParameter("name", name);
        return query.list();
    }

    @Override
    public List<Employee> findByNamePart(String namePart) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where name like:namePart");
        query.setParameter("namePart", "%" + namePart + "%");
        return query.list();
    }

    @Override
    public List<Employee> findByActive(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where isActive = :isActive");
        query.setParameter("isActive", isActive);
        return query.list();
    }

    @Override
    public Employee findByUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where user = :user");
        query.setParameter("user", user);
        return (Employee) query.getSingleResult();
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where department = :department");
        query.setParameter("department", department);
        return query.list();
    }

    @Override
    public List<Employee> findByDepartmentPaginated(Department department, PagingRequest pagingRequest) {
        Session session = sessionFactory.getCurrentSession();
        Order order = pagingRequest.getOrder().get(0);
        Column column = pagingRequest.getColumns().get(order.getColumn());
        String hql = "from Employee where department = :department";
        if (!pagingRequest.getSearch().getValue().equals(""))
            hql += " and concat(id, user.username, name, user.authority.role, user.isActive, isActive) " +
                    "like '%" + pagingRequest.getSearch().getValue() + "%'";
        hql += " order by " + column.getData() + " " + order.getDir().toString();
        Query query = session.createQuery(hql);
        query.setParameter("department", department);
        query.setFirstResult(pagingRequest.getStart());
        query.setMaxResults(pagingRequest.getLength());
        return query.list();
    }

    @Override
    public Employee findByProfile(Profile profile) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                "from Employee where profile = :profile");
        query.setParameter("profile", profile);
        return (Employee) query.getSingleResult();
    }

    @Override
    public Employee save(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(employee);
        return employee;
    }

    @Override
    public List<Employee> save(List<Employee> employees) {
        Session session = sessionFactory.getCurrentSession();
        employees.forEach(session::saveOrUpdate);
        return employees;
    }

    @Override
    public Employee merge(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        return (Employee) session.merge(employee);
    }

    @Override
    public boolean removeById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Employee employee = session.get(Employee.class, id);
        if (employee == null) return false;
        else {
            try {
                session.delete(employee);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        employee = (Employee) session.merge(employee);
        if (employee == null) return false;
        else {
            try {
                session.delete(employee);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public boolean inactivateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Employee employee = session.get(Employee.class, id);
        if (employee == null) return false;
        else if (employee.isActive()) {
            try {
                employee.setActive(false);
                session.saveOrUpdate(employee);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean inactivate(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        employee = (Employee) session.merge(employee);
        if (employee == null) return false;
        else if (!employee.isActive()) return true;
        else {
            try {
                employee.setActive(false);
                session.saveOrUpdate(employee);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activateById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Employee employee = session.get(Employee.class, id);
        if (employee == null) return false;
        else if (!employee.isActive()) {
            try {
                employee.setActive(true);
                session.saveOrUpdate(employee);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean activate(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        employee = (Employee) session.merge(employee);
        if (employee == null) return false;
        else if (employee.isActive()) return true;
        else {
            try {
                employee.setActive(true);
                session.saveOrUpdate(employee);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
