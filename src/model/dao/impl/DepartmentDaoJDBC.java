package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection connect;

	public DepartmentDaoJDBC(Connection connection) {
		this.connect = connection;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			st = connect.prepareStatement("INSERT INTO department " + "(Name) " + "VALUES " + "(?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);

			} else {
				throw new DbException("Erro inesperado, nenhuma linha foi afetada!");

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
			st = connect.prepareStatement("UPDATE department " + "SET Name = ? " + "WHERE Id = ?");

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
		try {
			st = connect.prepareStatement("DELETE FROM department WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		
		try {
			pStatement = connect.prepareStatement("SELECT * FROM department WHERE Id = ? ");

			pStatement.setInt(1, id);
			rSet = pStatement.executeQuery();
			if (rSet.next()) {
				Department dep = new Department();
				dep.setId(rSet.getInt("Id"));
				dep.setName(rSet.getString("Name"));
				return dep;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(pStatement);
			DB.closeResultSet(rSet);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		try {
			pStatement = connect.prepareStatement("SELECT * FROM department ORDER BY Name");

			rSet = pStatement.executeQuery();
			List<Department> list = new ArrayList<>();

			while (rSet.next()) {
				Department dep = new Department();
				dep.setId(rSet.getInt("Id"));
				dep.setName(rSet.getString("Name"));
				list.add(dep);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(pStatement);
			DB.closeResultSet(rSet);
		}
	}

}
