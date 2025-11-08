package db;

import java.sql.*;

public class DbCon {
    static String username="system";
    static String pass="root";
    static String url="jdbc:oracle:thin:@localhost:1521:XE";
    static
    {
        try
        {
            Class.forName("oracle.jdbc.OracleDriver");
        }
        catch(ClassNotFoundException e)
        {
            throw  new RuntimeException("Oracle JDBC driver not found",e);
        }
    }
    public static Connection getConnection()throws SQLException
    {
        return DriverManager.getConnection(url,username,pass);
    }
}
