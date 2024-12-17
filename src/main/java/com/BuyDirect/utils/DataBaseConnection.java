package com.BuyDirect.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnection {

	public static List<String> testWithDataBase(String query) throws SQLException {
		String connectionUrl = "jdbc:sqlserver://bimservicesdb-qa.database.windows.net;databaseName=BimSys;user=bimx05dbqa;password=jVs4xpUsVd35eyr5";

		List<String> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(connectionUrl);
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					result.add(resultSet.getString(i));
				}
			}
		} // Resources are closed automatically here
		return result;
	}
}
