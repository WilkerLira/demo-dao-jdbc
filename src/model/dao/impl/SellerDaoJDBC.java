package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection con;

	public SellerDaoJDBC(Connection connection) {
		this.con = connection;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findByid(Integer id) {
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		try {
			pStatement = con.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			pStatement.setInt(1, id);
			rSet = pStatement.executeQuery();
			if (rSet.next()) {
				Department department = instantiateDepartment(rSet);
				Seller obj = instantiateSeller(rSet, department);
				return obj;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		
		}finally {
			DB.closeStatement(pStatement);
			DB.closeResultSet(rSet);
		}
	}

	private Seller instantiateSeller(ResultSet rSet, Department department) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rSet.getInt("Id"));
		obj.setName(rSet.getString("Name"));
		obj.setEmail(rSet.getString("Email"));
		obj.setBaseSalary(rSet.getDouble("BaseSalary"));
		obj.setBirthDate(rSet.getDate("BirthDate"));
		obj.setDepartment(department);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rSet) throws SQLException {
		Department dep = new Department();
		dep.setId(rSet.getInt("DepartmentId"));
		dep.setName(rSet.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		try {
			pStatement = con.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE DepartmentId = ? "
					+"ORDER BY Name");
			
			pStatement.setInt(1, department.getId());
			rSet = pStatement.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while (rSet.next()) {
				
				Department depart = map.get(rSet.getInt("DepartmentId"));
				
				if (depart == null) {
					depart = instantiateDepartment(rSet);
					map.put(rSet.getInt("DepartmentId"), depart);
				}
				
				Seller obj = instantiateSeller(rSet, department);
				list.add(obj);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		
		}finally {
			DB.closeStatement(pStatement);
			DB.closeResultSet(rSet);
		}
	}

}
