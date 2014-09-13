package jef.database.routing.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import jef.database.OperateTarget;
import jef.database.wrapper.result.JdbcResultSetAdapter;
import jef.database.wrapper.result.ResultSetWrapper;

public class SimpleSQLExecutor implements SQLExecutor {
	private String sql;
	private OperateTarget db;
	private final ParamsContextProvider params=new ParamsContextProvider();
	private int fetchSize;
	private int maxRows;
	private int queryTimeout;
	
	public SimpleSQLExecutor(OperateTarget target, String sql) {
		this.db=target;
		this.sql=sql;
	}

	@Override
	public UpdateReturn executeUpdate(int generateKeys,int[] returnIndex,String[] returnColumns) throws SQLException {
		PreparedStatement st=db.prepareStatement(sql);
		try{
			params.apply(st);
			int count=st.executeUpdate();
			return new UpdateReturn(count);
		}finally{
			st.close();
		}
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		PreparedStatement st=db.prepareStatement(sql);
		try{
			params.apply(st);
			if(fetchSize>0)
				st.setFetchSize(fetchSize);
			if(maxRows>0);
				st.setMaxRows(maxRows);
			if(queryTimeout>0)
				st.setQueryTimeout(queryTimeout);
			ResultSet rs=st.executeQuery();
			return new JdbcResultSetAdapter(new ResultSetWrapper(db,st,rs));	
		}finally{
			st.close();
		}
	}
	

	@Override
	public void setFetchSize(int fetchSize) {
		this.fetchSize=fetchSize;
	}

	@Override
	public void setMaxResults(int maxRows) {
		this.maxRows=maxRows;
	}

	@Override
	public void setResultSetType(int resultSetType) {
		
	}

	@Override
	public void setResultSetConcurrency(int resultSetConcurrency) {
		
	}

	@Override
	public void setResultSetHoldability(int resultSetHoldability) {
		
	}

	@Override
	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout=queryTimeout;
	}

	@Override
	public void setParams(Collection<ParameterContext> params) {
		this.params.set(params);
	}

	@Override
	public UpdateReturn executeBatch(int autoGeneratedKeys, int[] columnIndexes, String[] columnNames, List<Collection<ParameterContext>> params) {
		// TODO Auto-generated method stub
		return null;
	}
}
