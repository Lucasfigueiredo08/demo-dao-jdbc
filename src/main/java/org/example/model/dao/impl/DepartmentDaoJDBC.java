package org.example.model.dao.impl;

import org.example.db.DB;
import org.example.db.DbException;
import org.example.model.dao.DepartmentDao;
import org.example.model.entities.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }
    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try {
             st = conn.prepareStatement(
                    "INSERT INTO department (Name)" +
                        "VALUES (?)", Statement.RETURN_GENERATED_KEYS
            );

             st.setString(1, obj.getName());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0){
                 ResultSet rs = st.getGeneratedKeys();
                 if(rs.next()){
                     int id = rs.getInt(1);
                     obj.setId(id);
                 }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Não houve linhas afetadas.");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE department SET name = ?"
                    + "WHERE Id = ?"
            );

            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "DELETE FROM department WHERE Id = ?"
            );

            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);

        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
             st = conn.prepareStatement(
                    "SELECT department.* "
                    + "FROM department "
                    + "WHERE Id = ?"
            );

             st.setInt(1, id);

            rs = st.executeQuery();
            if (rs.next()){
                Department dp = instatiateDepartment(rs);
                return dp;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);

        }
    }

    private Department instatiateDepartment(ResultSet rs) throws SQLException {
        Department dp = new Department();
        dp.setId(rs.getInt("Id"));
        dp.setName(rs.getString("Name"));
        return dp;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM department ORDER BY Name;"

            );
            rs = st.executeQuery();

            List<Department> departmentsList = new ArrayList<>();
            while (rs.next()){
                instatiateDepartment(rs, departmentsList);
            }
            return departmentsList;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    private static void instatiateDepartment(ResultSet rs, List<Department> departmentsList) throws SQLException {
        Department dp = new Department();
        dp.setId(rs.getInt("Id"));
        dp.setName(rs.getString("Name"));
        departmentsList.add(dp);
    }
}
