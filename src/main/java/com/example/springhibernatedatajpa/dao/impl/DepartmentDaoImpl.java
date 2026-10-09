package com.example.springhibernatedatajpa.dao.impl;

import com.example.springhibernatedatajpa.dao.IDepartmentDao;
import com.example.springhibernatedatajpa.entity.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class DepartmentDaoImpl implements IDepartmentDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Department> findAll() {
        String hql = "SELECT d FROM Department d";
        return entityManager.createQuery(hql, Department.class).getResultList();
    }

    @Override
    public Department findById(UUID id) {
        return entityManager.find(Department.class, id);
    }

    @Override
    public void save(Department department) {
        entityManager.persist(department);
    }

    @Override
    public void update(Department department) {
        entityManager.merge(department);
    }

    @Override
    public void delete(UUID id) {
        entityManager.remove(entityManager.find(Department.class, id));
    }
}
