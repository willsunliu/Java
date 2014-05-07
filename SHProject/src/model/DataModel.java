package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataModel {

	// 客户表
	public static final String CLIENT_TABLE_NAME = "client_table";
	public static final String CLIENT_NAME_LABEL = "client_name";
	public static final String CLIENT_ADDR_LABEL = "client_addr";
	public static final String CLIENT_PHONE_LABEL = "client_phone";

	// 货物表
	public static final String GOOD_TABLE_NAME = "good_table";
	public static final String GOOD_NAME_LABEL = "good_name";
	public static final String GOOD_PRICE_LABEL = "good_unit_price";
	
	// 送货单表
	public static final String FORM_TABLE_NAME = "form_table";
	public static final String FORM_NUM_LABEL = "form_num";
	public static final String FORM_CLIENT_LABEL = "form_client";
	public static final String FORM_DATE_LABEL = "form_date";
	public static final String FORM_DATA_LABEL = "form_data";
	public static final String FORM_TOTAL_LABEL = "form_total";
	public static final String FORM_CONTRACT_LABEL = "form_contract";

	private Connection conn;
	private DatabaseMetaData meta;
	private Statement stmt;
	private ResultSet rs;

	public void init() {
		try {
			// 获取数据库连接
			conn = getConnection();
			if (conn != null) {
				// 获取数据库的MetaData对象
				meta = conn.getMetaData();
				// 创建Statement
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				// // 查询当前数据库的全部数据表
				// rs = stmt.executeQuery("select * from good_table");
			}
		} catch (IOException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询货物表
	 * 
	 * @throws SQLException
	 */
	public void querryAll(String tableName) throws SQLException {
		rs = stmt.executeQuery("select * from " + tableName);
	}

	public boolean compareValue(String tableName, String columnLabel,
			String inputValue) throws SQLException {
		querryAll(tableName);
		while (rs.next()) {
			String value = rs.getString(columnLabel);
			if (value.equals(inputValue)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取table的记录数，即行数
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public int countTableRow(String tableName) throws SQLException {
		querryAll(tableName);
		if (rs.next()) {
			rs.last();
			return rs.getRow();
		}
		return 0;
	}

	/**
	 * 获取table的列数
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public int countTableColumn(String tableName) throws SQLException {
		querryAll(tableName);
		if (rs.next()) {
			return rs.getMetaData().getColumnCount();
		}
		return 0;
	}

	/**
	 * 获取table中指定行列处的值
	 * 
	 * @param tableName
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public String getValueAt(String tableName, int rowIndex, int columnIndex)
			throws SQLException {
		querryAll(tableName);
		if (rs.next()) {
			rs.absolute(rowIndex);
			return rs.getString(columnIndex);
		}
		return null;
	}
	
	/**
	 * 获取满足conditions条件的指定列的值
	 * @param tableName
	 * @param valueLabel
	 * @param conditions
	 * @return
	 * @throws SQLException
	 */
	public String getValueAt(String tableName, String valueLabel, String... conditions) throws SQLException {
		querryAll(tableName);
		String str = null;
		while (rs.next()) {
			str = rs.getString(conditions[0]);
			if (str.equals(conditions[1])) {
				return rs.getString(valueLabel);
			}
		}
		return " ";
	}

	/**
	 * 获取表中特定列的所有值
	 * @param tableName
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 */
	public String[] getAllValueInColumn(String tableName, String columnLabel)
			throws SQLException {
		ArrayList<String> valueList = new ArrayList<>();
		valueList.add("<空>");
		querryAll(tableName);
		while (rs.next()) {
			valueList.add(rs.getString(columnLabel));
		}
		String[] values = new String[valueList.size()];
		valueList.toArray(values);
		return values;
	}

	/**
	 * 设置表中key=keyValue处changedValueKey的值为changedValue
	 * @param tableName
	 * @param changedValueKey
	 * @param changedValue
	 * @param key
	 * @param keyValue
	 * @throws SQLException
	 */
	public void setGoodValueAt(String tableName, String changedValueKey,
			String changedValue, String key, String keyValue)
			throws SQLException {
		String sql = "update " + tableName + " set " + changedValueKey + "='"
				+ changedValue + "' where " + key + "='" + keyValue + "';";
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}

	/**
	 * 插入数据
	 * 
	 * @param 表名
	 * @param 行的所有值
	 * @throws SQLException
	 */
	public void insertValue(String tableName, String... values)
			throws SQLException {
		String sql = "insert into " + tableName + " values(";
		for (int i = 0; i < values.length; i++) {
			if (0 == i) {
				sql += "'" + values[i] + "'";
			} else {
				sql += ",'" + values[i] + "'";
			}
		}
		sql += ");";
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}

	/**
	 * 删除数据
	 * 
	 * @param tableName
	 * @param values
	 *            用作条件判断，格式：列名, 列值, 列名, 列值, ...
	 * @throws SQLException
	 */
	public void deleteValue(String tableName, String... values)
			throws SQLException {
		String sql = "delete from " + tableName + " where ";
		for (int i = 0; i < values.length; i += 2) {
			String key = values[i];
			String value = values[i + 1];
			if (value.contains("format")) {
				sql += (" " + key + "=" + value);
				if (i + 2 < values.length) {
					sql += " and";
				} else {
					sql += "";
				}
			} else {
				sql += (" " + key + "='" + value);
				if (i + 2 < values.length) {
					sql += "' and";
				} else {
					sql += "'";
				}
			}
		}
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getConnection() throws IOException,
			ClassNotFoundException, SQLException {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream("src/mysql.ini");
		props.load(fis);
		fis.close();
		String driver = props.getProperty("driver");
		String url = props.getProperty("url");
		String user = props.getProperty("user");
		String pass = props.getProperty("pass");

		Class.forName(driver);

		return DriverManager.getConnection(url, user, pass);
	}
}
