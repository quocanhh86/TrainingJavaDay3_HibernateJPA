package com.example.springhibernatedatajpa.dao.impl;

import com.example.springhibernatedatajpa.dao.IAuthenticationDao;
import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.entity.UserAdmin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthenticationDaoImpl implements IAuthenticationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserAdmin getAdminByEmail(String email) {
        String hql = "select u from UserAdmin u where u.email = :email";
        TypedQuery<UserAdmin> query = entityManager.createQuery(hql, UserAdmin.class);
        query.setParameter("email", email);
        query.setMaxResults(1);
        List<UserAdmin> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void employeeRegister(Employee employee) {
        entityManager.persist(employee);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        String hql = "select e from Employee e where e.email = :email";
        TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
        query.setParameter("email", email);
        query.setMaxResults(1);
        List<Employee> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
