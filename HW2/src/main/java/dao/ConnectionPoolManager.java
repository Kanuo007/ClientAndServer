package dao;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolManager {

	BasicDataSource source = new BasicDataSource();
	private final String user = "kanuo_007";
	private final String password = "13050305111";
	private final String hostName = "wendb.c9h7fbdsti5f.us-west-2.rds.amazonaws.com";
	private final int port= 3306;
	private final String schema = "Distributed";
	private final int DB_MAX_CONNECTIONS = 60;


	public ConnectionPoolManager(){
		String url = "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema;
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl(url);
		source.setUsername(user);
		source.setPassword(password);
		source.setMaxActive(DB_MAX_CONNECTIONS);
		source.setInitialSize(10);
	}

	public Connection getConnection(){
		try {
			return source.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
