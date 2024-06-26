package org.example.model.dao.impl;

import org.example.db.DB;
import org.example.db.DbException;
import org.example.model.dao.SellerDao;
import org.example.model.entities.Department;
import org.example.model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    private Connection conn;


    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }
    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) "
                        +  " VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!" );
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, " +
                        " BaseSalary = ?, DepartmentId = ? " +
                        "WHERE Id = ?"
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());
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
        try {
            st = conn.prepareStatement(
                "DELETE FROM seller " +
                     "WHERE Id = ?"
            );
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.name as DepName "
                    +  "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?"
            );

            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){
                var dp = instantiateDepartment(rs);
                var seller = instantiateSeller(rs, dp);
                return seller;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    private static Seller instantiateSeller(ResultSet rs, Department dp) throws SQLException {
        var seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(dp);
        return seller;
    }

    private static Department instantiateDepartment(ResultSet rs) throws SQLException {
        var dp = new Department();
        dp.setId(rs.getInt("Id"));
        dp.setName(rs.getString("DepName"));
        return dp;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name"
            );
            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dp = map.get(rs.getInt("DepartmentId"));

                if (dp == null){
                    dp = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dp);
                }


                var seller = instantiateSeller(rs, dp);
                list.add(seller);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.name as DepName "
                        + "FROM seller INNER JOIN department "
                        + "ON seller.DepartmentId = department.Id "
                        + "WHERE DepartmentId = ? "
                        + "ORDER BY Name;"
            );

            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dp = map.get(rs.getInt("DepartmentId"));

                if (dp == null){
                    dp = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dp);
                }
                var seller = instantiateSeller(rs, dp);
                list.add(seller);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }
}
