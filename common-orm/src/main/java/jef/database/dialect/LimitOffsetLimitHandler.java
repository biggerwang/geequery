package jef.database.dialect;

import java.sql.ResultSet;

import jef.common.log.LogUtil;
import jef.common.wrapper.IntRange;
import jef.database.DbUtils;
import jef.database.dialect.statement.LimitHandler;
import jef.database.jsqlparser.parser.ParseException;
import jef.database.jsqlparser.statement.select.Select;
import jef.database.jsqlparser.statement.select.Union;
import jef.tools.StringUtils;

public class LimitOffsetLimitHandler implements LimitHandler {
	protected static final String PG_PAGE = " limit %next% offset %start%";

	public String toPageSQL(String sql, IntRange range) {
		boolean isUnion = false;
		try {
			Select select = DbUtils.parseNativeSelect(sql);
			if (select.getSelectBody() instanceof Union) {
				isUnion = true;
			}
			select.getSelectBody();
		} catch (ParseException e) {
			LogUtil.exception("SqlParse Error:", e);
		}

		String limit = StringUtils.replaceEach(PG_PAGE, new String[] { "%start%", "%next%" }, range.toStartLimit());
		return isUnion ? StringUtils.concat("select * from (", sql, ") tb__", limit) : sql.concat(limit);
	}

	public String toPageSQL(String sql, IntRange range, boolean isUnion) {
		String limit = StringUtils.replaceEach(PG_PAGE, new String[] { "%start%", "%next%" }, range.toStartLimit());
		return isUnion ? StringUtils.concat("select * from (", sql, ") tb__", limit) : sql.concat(limit);
	}

	@Override
	public ResultSet afterProcess(ResultSet rs, IntRange offsetLimit) {
		return rs;
	}
}
