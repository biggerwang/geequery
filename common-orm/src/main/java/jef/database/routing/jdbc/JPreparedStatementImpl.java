package jef.database.routing.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import jef.database.innerpool.ReentrantConnection;
import jef.database.routing.sql.ExecutionPlan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPreparedStatementImpl extends JStatementImpl implements JPreparedStatement {

	private static final Logger log = LoggerFactory.getLogger(JPreparedStatementImpl.class);

	private final String originalSQL;

	List<Map<Integer, ParameterContext>> pstArgs;


	private int autoGeneratedKeys = -1;

	private int[] columnIndexes;

	private String[] columnNames;

	private Map<Integer, ParameterContext> parameterSettings = new TreeMap<Integer, ParameterContext>();

	/**
	 * 构造
	 * @param sql
	 * @param routingConnection
	 * @param resultsetType
	 * @param resultSetConcurrency
	 */
	public JPreparedStatementImpl(String sql, ReentrantConnection routingConnection, int resultsetType, int resultSetConcurrency,ExecutionPlan plan) {
		super(routingConnection,resultsetType,resultSetConcurrency,plan);
		this.originalSQL=sql;
	}

	public void clearParameters() throws SQLException {
		parameterSettings.clear();
	}

	public boolean execute() throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("invoke execute, sql = " + originalSQL);
		}
		if (SqlTypeParser.isQuerySql(originalSQL)) {
			executeQuery();
			return true;
		} else {
			executeUpdate();
			return false;
		}
	}

	public ResultSet executeQuery() throws SQLException {
		SqlType sqlType = SqlTypeParser.getSqlType(originalSQL);
		//return executeQueryInternal(originalSQL, parameterSettings, sqlType, this);
		return currentResultSet;
	}

	public int executeUpdate() throws SQLException {
		SqlType sqlType = SqlTypeParser.getSqlType(originalSQL);
		return executeUpdateInternal(originalSQL, autoGeneratedKeys, columnIndexes, columnNames, parameterSettings, sqlType, this);
	}

	protected PreparedStatement prepareStatementInternal(Connection connection, String targetSql) throws SQLException {
		PreparedStatement ps;
		if (getResultSetType() != -1 && getResultSetConcurrency() != -1 && getResultSetHoldability() != -1) {
			ps = connection.prepareStatement(targetSql, getResultSetType(), getResultSetConcurrency(), getResultSetHoldability());
		} else if (getResultSetType() != -1 && getResultSetConcurrency() != -1) {
			ps = connection.prepareStatement(targetSql, getResultSetType(), getResultSetConcurrency());
		} else if (autoGeneratedKeys != -1) {
			ps = connection.prepareStatement(targetSql, autoGeneratedKeys);
		} else if (columnIndexes != null) {
			ps = connection.prepareStatement(targetSql, columnIndexes);
		} else if (columnNames != null) {
			ps = connection.prepareStatement(targetSql, columnNames);
		} else {
			ps = connection.prepareStatement(targetSql);
		}

		return ps;
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		throw new UnsupportedOperationException("getMetaData");
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new UnsupportedOperationException("getParameterMetaData");
	}

	public void setArray(int i, Array x) throws SQLException {
		parameterSettings.put(i, new ParameterContext(ParameterMethod.setArray, new Object[] { i, x }));
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setAsciiStream, new Object[] { parameterIndex, x, length }));
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setBigDecimal, new Object[] { parameterIndex, x }));
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setBinaryStream, new Object[] { parameterIndex, x, length }));
	}

	public void setBlob(int i, Blob x) throws SQLException {
		parameterSettings.put(i, new ParameterContext(ParameterMethod.setBlob, new Object[] { i, x }));
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setBoolean, new Object[] { parameterIndex, x }));
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setByte, new Object[] { parameterIndex, x }));
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setBytes, new Object[] { parameterIndex, x }));
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setCharacterStream, new Object[] { parameterIndex, reader, length }));
	}

	public void setClob(int i, Clob x) throws SQLException {
		parameterSettings.put(i, new ParameterContext(ParameterMethod.setClob, new Object[] { i, x }));
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setDate1, new Object[] { parameterIndex, x }));
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setDate2, new Object[] { parameterIndex, x, cal }));
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setDouble, new Object[] { parameterIndex, x }));
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setFloat, new Object[] { parameterIndex, x }));
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setInt, new Object[] { parameterIndex, x }));
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setLong, new Object[] { parameterIndex, x }));
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setNull1, new Object[] { parameterIndex, sqlType }));
	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		parameterSettings.put(paramIndex, new ParameterContext(ParameterMethod.setNull2, new Object[] { paramIndex, sqlType, typeName }));
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setObject1, new Object[] { parameterIndex, x }));
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setObject2, new Object[] { parameterIndex, x, targetSqlType }));
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setObject3, new Object[] { parameterIndex, x, targetSqlType, scale }));
	}

	public void setRef(int i, Ref x) throws SQLException {
		parameterSettings.put(i, new ParameterContext(ParameterMethod.setRef, new Object[] { i, x }));
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setShort, new Object[] { parameterIndex, x }));
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setString, new Object[] { parameterIndex, x }));
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setTime1, new Object[] { parameterIndex, x }));
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setTime2, new Object[] { parameterIndex, x, cal }));
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setTimestamp1, new Object[] { parameterIndex, x }));
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setTimestamp2, new Object[] { parameterIndex, x, cal }));
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setURL, new Object[] { parameterIndex, x }));
	}

	@Deprecated
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		parameterSettings.put(parameterIndex, new ParameterContext(ParameterMethod.setUnicodeStream, new Object[] { parameterIndex, x, length }));
	}

	/**
	 * batch 操作中，如果一个连接内的pst出现更新异常。 则整个连接的后续更新都会终止。 但其他连接的更新还会继续。 目前并不提供返回值设置。
	 */
	public int[] executeBatch() throws SQLException {
		return new int[]{-1};
//		if (log.isDebugEnabled()) {
//			log.debug("invoke executeBatch, sql = " + originalSQL);
//		}
//
//		if (super.batchedArgs != null && super.batchedArgs.size() != 0) {
//
//		}
//
//		List<SQLException> exceptions = new ArrayList<SQLException>();
//		List<Integer> result = new ArrayList<Integer>();
//		checkClosed();
//		ensureResultSetIsEmpty();
//		// 这个地方等到执行计划出来之后，发现确实有跨库的事务再予以拒绝。
//		// if (!connectionManager.getAutoCommit()) {
//		// throw new SQLException("executeBatch暂不支持事务");
//		// }
//		if (pstArgs == null || pstArgs.isEmpty()) {
//			return new int[0];
//		}
//		if (batchedArgs != null && !batchedArgs.isEmpty() && !connectionManager.getAutoCommit()) {
//			throw new SQLException("事务中暂时不支持addBatch()和addBatch(String sql)混用，请选择前者替换，如使用addBatch(String sql),请使用Statement");
//		}
//
//		Map<String/* dbselectorid */, Map<String/* sql */, List/* 这条sql的参数 */<List<ParameterContext>>>> pstExecutionContext = null;
//
//		DirectlyRouteCondition ruleCondition = (DirectlyRouteCondition) getRouteContiongFromThreadLocal(ThreadLocalString.RULE_SELECTOR);
//		if (ruleCondition != null) {
//			String dbRuleId = ruleCondition.getDbRuleID();
//			pstExecutionContext = super.sortPreparedBatch(originalSQL, pstArgs, dbRuleId);
//		} else {
//			pstExecutionContext = super.sortPreparedBatch(originalSQL, pstArgs, null);
//		}
//		// add by jiechen.qzm batch不支持跨库事务
//		if (pstExecutionContext.size() > 1 && !connectionManager.getAutoCommit()) {
//			throw new SQLException("executeBatch暂不支持跨库事务，该事务涉及 " + pstExecutionContext.size() + " 个库。");
//		}
//
//		// add by jiechen.qzm batch不支持跨库事务
//		if (pstExecutionContext.size() > 1 && !connectionManager.getAutoCommit()) {
//			throw new SQLException("executeBatch暂不支持跨库事务，该事务涉及 " + pstExecutionContext.size() + " 个库。");
//		}
//
//		for (Entry<String, Map<String, List<List<ParameterContext>>>> entry : pstExecutionContext.entrySet()) {
//			String dbSelectorID = entry.getKey();
//			List<Integer> list = null;
//			try {
//				Connection conn = connectionManager.getConnection(dbSelectorID, false);
//				try {
//					list = executeBatchOnOneConnAndClosePreparedStatement(exceptions, entry.getValue(), conn);
//					result.addAll(list);
//				} finally {
//					exceptions = tryCloseConnection(exceptions, dbSelectorID);
//				}
//			} catch (SQLException e) {
//				exceptions = appendToExceptionList(exceptions, e);
//			} finally {
//				pstArgs.clear();
//			}
//		}
//		super.currentResultSet = null;
//		super.moreResults = false;
//		super.updateCount = 0;
//		ExceptionUtils.throwSQLException(exceptions, "batch", (List<Object>) null);
//		// 执行父类中的batch操作。因为有可能用PreparedStatement却addBatch了一些只有sql没有参数的数据。
//		// 这时候应该分别执行
//		// TODO:这里有个隐含的问题是，statement.batch操作和
//		// preparedStatement.batch 本身如果是有先后顺序的话，那么现在的处理并不能保证先后顺序的强关系性。
//		super.executeBatch();
//
//		// 只返回ps的返回值
//		return fromListToArray(result);
	}

	/**
	 * batch TPreparedStatement 批量执行操作。 如果有一个异常出现，则当前preparedStatement出现一次异常
	 * 则针对当前连接的所有更新都会终止。
	 * 
	 * @param sqlExceptions
	 * @param sqlMap
	 * @param conn
	 * @return
	 */
	protected List<Integer> executeBatchOnOneConnAndClosePreparedStatement(List<SQLException> sqlExceptions, Map<String, List<List<ParameterContext>>> sqlMap, Connection conn) {
		List<Integer> result = new ArrayList<Integer>();
		try {
			for (Entry<String/* sql */, List<List<ParameterContext>>> sqlMapEntry : sqlMap.entrySet()) {
				PreparedStatement ps = prepareStatementInternal(conn, sqlMapEntry.getKey());
				try {
					int[] temp = null;
					// 添加batch参数
					for (List<ParameterContext> params : sqlMapEntry.getValue()) {
						setBatchParameters(ps, params);
						ps.addBatch();
					}

					temp = ps.executeBatch();
					//TODO
//					result.addAll(super.fromArrayToList(temp));
					ps.clearBatch();
				} finally {
					ps.close();
				}
			}
		} catch (SQLException e) {
			//TODO
//			sqlExceptions = appendToExceptionList(sqlExceptions, e);
			
		}
		return result;
	}

	private static void setBatchParameters(PreparedStatement ps, List<ParameterContext> batchedParameters) throws SQLException {
		for (ParameterContext context : batchedParameters) {
			context.getParameterMethod().setParameter(ps, context.getArgs());
		}
	}

	public void addBatch() throws SQLException {
		if (pstArgs == null) {
			pstArgs = new LinkedList<Map<Integer, ParameterContext>>();
		}
		// #bug 2011-11-15# modify by junyu
		// newArg为一个普通hashMap,这个会造成parameterSettings顺序无效
		// 应该和parameterSettings一样改成TreeMap
		Map<Integer, ParameterContext> newArg = new TreeMap<Integer, ParameterContext>();
		newArg.putAll(parameterSettings);

		pstArgs.add(newArg);
	}

	public int getAutoGeneratedKeys() {
		return autoGeneratedKeys;
	}

	public void setAutoGeneratedKeys(int autoGeneratedKeys) {
		this.autoGeneratedKeys = autoGeneratedKeys;
	}

	public int[] getColumnIndexes() {
		return columnIndexes;
	}

	public void setColumnIndexes(int[] columnIndexes) {
		this.columnIndexes = columnIndexes;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public boolean isClosed() throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setPoolable(boolean poolable) throws SQLException {
		throw new SQLException("not support exception");
	}

	public boolean isPoolable() throws SQLException {
		throw new SQLException("not support exception");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.getClass().isAssignableFrom(iface);
	}

	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		try {
			return (T) this;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		throw new SQLException("not support exception");
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		throw new SQLException("not support exception");
	}
}
