package Databaseutil;
/**
*@author
*@version：2020年8月16日下午8:38:26
*类说明
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {
        public static final  String URL="jdbc:mysql://localhost:3306/data011?serverTimezone=UTC";     
        public static final String NAME = "root";
        public static final String PASSWORD = "lingxi251421.";
         public static final String DREIVER = "com.mysql.cj.jdbc.Driver"; 
         static {
            try {
                //加载驱动器 
                Class.forName(DREIVER);                 
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
         }
        public static Connection getConnection() {
            try {
                return  DriverManager.getConnection(URL, NAME, PASSWORD);//创建与数据库的链接
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        public static ResultSet query(String querySql) throws SQLException {
    		Statement stateMent = (Statement) getConnection().createStatement();
    		return stateMent.executeQuery(querySql);
    	}
//设置一个公共的关闭链接、释放资源的方法    .   因为每次只要进行了增,删,查,改 之后 都必须要 事件,  那么就设置一个公共的方法
        //而关闭资源要从 ResultSet先关闭-->,再到 PreparedStatement-->,最后到 Connection关闭
        public static void Close(ResultSet rs, PreparedStatement ps, Connection conn) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
}