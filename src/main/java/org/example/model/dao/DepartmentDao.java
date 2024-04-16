package org.example.model.dao;

import org.example.model.entities.Department;

import java.sql.SQLException;
import java.util.List;

public interface DepartmentDao {

    void insert(Department obj);
    void update(Department obj);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();
}
