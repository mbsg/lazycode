package cdu.computer.hxl.db.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cdu.computer.hxl.configure.DefaultPropertiesConfigure;
import cdu.computer.hxl.db.DBConnection;
import cdu.computer.hxl.exception.DBConnectionException;

/**
 * 
 * @author hxl
 * 
 */
public class DefaultDBConnectionImpl implements DBConnection {
	private DefaultPropertiesConfigure defaultConfig = new DefaultPropertiesConfigure();

	public DefaultDBConnectionImpl() {
		try {
			Class.forName(defaultConfig.getDriver());// ��������
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ȡ��һ�����ݿ������ʵ��
	 * 
	 * @return Connection
	 */
	public Connection connection() throws DBConnectionException {
		Connection con = null;
		try {
			con = DriverManager.getConnection(defaultConfig.getConnectionURL());
		} catch (SQLException e) {
			throw new DBConnectionException(e.getMessage());
		}
		return con;

	}

	public static void main(String[] args) {
		DefaultDBConnectionImpl db = new DefaultDBConnectionImpl();
		try {
			Connection con = db.connection();
			Statement stm = con.createStatement();
			stm.executeUpdate("delete from income");
			//stm.executeUpdate("CREATE TABLE cost (amount REAL, reason TEXT, bankid INTEGER, date TEXT);");
			//ResultSet rs = stm.executeQuery("select * from test");
			//System.out.println(rs.getString(1));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}