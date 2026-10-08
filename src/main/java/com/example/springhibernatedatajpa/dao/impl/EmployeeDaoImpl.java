package com.example.springhibernatedatajpa.dao.impl;

import com.example.springhibernatedatajpa.dao.IEmployeeDao;
import com.example.springhibernatedatajpa.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class EmployeeDaoImpl implements IEmployeeDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> getEmployees() {
        String hql = "SELECT e FROM Employee e LEFT JOIN FETCH e.department d";
        return entityManager.createQuery(hql, Employee.class).getResultList();
    }

    @Override
    public Employee getEmployee(UUID id) {
        return entityManager.find(Employee.class, id);
    }

    @Override
    public void addEmployee(Employee employee) {
        entityManager.persist(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        entityManager.merge(employee);
    }

    @Override
    public void deleteEmployee(UUID id) {
        entityManager.remove(getEmployee(id));
    }
}
