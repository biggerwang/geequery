package jef.database.datasource;

import java.sql.SQLException;

import javax.management.ReflectionException;
import javax.sql.DataSource;

import jef.database.ConnectInfo;
import jef.database.DbUtils;
import jef.database.datasource.DataSourceWrapper;
import jef.database.datasource.DataSources;
import jef.tools.reflect.BeanUtils;

import org.junit.Test;

public class DataSourceWrapperTest {
	@Test
	public void testWrapper() throws SQLException {
		DataSource ds = DbUtils.createSimpleDataSource("aa", "aa", "aa");
		DataSourceWrapper dw=DataSources.wrapFor(ds);
		System.out.println(dw);
	}
	@Test
	public void testParseUrl() throws SQLException, ReflectionException{
		String[] x={
				"jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = host2)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = host1)(PORT = 1521))(LOAD_BALANCE = yes)(FAILOVER = ON)(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = db.domain)(FAILOVER_MODE=(TYPE = SELECT)(METHOD = BASIC)(RETIRES = 20)(DELAY = 15))))",
				"jdbc:mysql://localhost:3306/allandb?useUnicode=true&characterEncoding=UTF-8",
				"jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=WapSvc;User=sa;Password=pwd",
				"jdbc:oracle:thin:@hostname:1521:AAA",
				"jdbc:derby://localhost:1527/databaseName;create=true",
				"jdbc:postgresql://localhost:5432/soft",
				"jdbc:postgresql://localhost/soft",
				"jdbc:db2://aServer.myCompany.com:50002/name",
				"jdbc:derby:./db1;create=true",
				"jdbc:oracle:oci:@//example.com:5521:bjava21",
				"jdbc:oracle:oci:@//example.com:bjava21",
				"jdbc:oracle:oci:@sss",
				//"jdbc:sybase:Tds:aServer.myCompany.com:2025"
				};
				for(String s:x){
					ConnectInfo info=new ConnectInfo();
					info.setUrl(s);
					BeanUtils.invokeMethod(info, "parse");
					System.out.println(info);			
				}

	}
}
