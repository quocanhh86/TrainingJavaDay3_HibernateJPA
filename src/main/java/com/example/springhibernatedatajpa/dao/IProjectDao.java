package com.example.springhibernatedatajpa.dao;

import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.entity.Project;

import java.util.List;
import java.util.UUID;

public interface IProjectDao {
    List<Project> findAll();
    Project findById(UUID id);
    void add(Project department);
    void update(Project department);
    void delete(UUID id);
}
