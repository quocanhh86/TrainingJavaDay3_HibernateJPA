package com.example.springhibernatedatajpa.dao.impl;

import com.example.springhibernatedatajpa.dao.IProjectDao;
import com.example.springhibernatedatajpa.entity.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ProjectDaoImpl implements IProjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Project> findAll() {
        String hql = "SELECT DISTINCT p FROM Project p " +
                "LEFT JOIN FETCH p.department d " +
                "LEFT JOIN FETCH p.teamLead tl " +
                "ORDER BY p.createdAt";
        return entityManager.createQuery(hql, Project.class).getResultList();
    }

    @Override
    public Project findById(UUID id) {
        String hql = "SELECT DISTINCT p FROM Project p " +
                "LEFT JOIN FETCH p.department d " +
                "LEFT JOIN FETCH p.teamLead tl " +
                "LEFT JOIN FETCH p.employees e " +
                "WHERE p.id = :id";
        List<Project> list = entityManager.createQuery(hql, Project.class)
                .setParameter("id", id)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void add(Project project) {
        entityManager.persist(project);
    }

    @Override
    public void update(Project project) {
        entityManager.merge(project);
    }

    @Override
    public void delete(UUID id) {
        entityManager.remove(entityManager.find(Project.class, id));
    }
}
