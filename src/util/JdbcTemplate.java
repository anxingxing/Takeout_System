package util;

import java.sql.Connection;
import Databaseutil.DatabaseUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import resultsetextractor.ResultSetExtractor;
import exception.DataAccessException;

public class JdbcTemplate {
	public Integer getPrimaryKey(String sql, String name,String date){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Integer id = 0;
		try {
			con = DatabaseUtil.getConnection();
			ps=con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, name);
			ps.setString(2, date);
			ps.executeUpdate();
			rs=ps.getGeneratedKeys();
			
			if(rs.next()){
				id=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("数据访问失败", e);
		} finally {
			DatabaseUtil.Close(rs, ps, con);
		}
		return id;
	}
	public void update(String sql, Object[] params){
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseUtil.getConnection();
			ps = con.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					setParam(i+1, params[i], ps);
				}
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("数据访问失败", e);
		} finally {
			DatabaseUtil.Close(null, ps, con);
		}
	}
	public Object query(String sql,Object[] params,ResultSetExtractor rse){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Object obj=null;
		try {
			con=DatabaseUtil.getConnection();
			ps=con.prepareStatement(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					setParam(i+1, params[i], ps);
				}
			}
			rs=ps.executeQuery();
			obj=rse.extractData(rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("数据访问失败",e);
		} finally {
			DatabaseUtil.Close(rs, ps, con);
		}
		return obj;
	}
	
	private void setParam(int index,Object param,PreparedStatement ps) throws SQLException{
		if(param==null){
			ps.setNull(index, Types.VARCHAR);
		}else if(param instanceof String){
			ps.setString(index, (String)param);
		}else if(param instanceof Integer||param.getClass().equals(int.class)){
			ps.setInt(index, (Integer)param);
		}else if(param instanceof Double||param.getClass().equals(double.class)){
			ps.setDouble(index, (Double)param);
		}else if(param instanceof java.sql.Date){
			ps.setDate(index, (java.sql.Date)param);
		}else if(param instanceof java.sql.Time){
			ps.setTime(index, (java.sql.Time)param);
		}else if(param instanceof java.sql.Timestamp){
			ps.setTimestamp(index, (java.sql.Timestamp)param);
		}else if(param instanceof Date){
			ps.setTimestamp(index, new Timestamp(((Date)param).getTime()));
		}else{
			ps.setObject(index, param);
		}
	}
}