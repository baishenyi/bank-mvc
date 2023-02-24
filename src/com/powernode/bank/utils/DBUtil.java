package com.powernode.bank.utils;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * JDBC工具类
 * @author b
 * @version 1.0
 * @since 1.0
 */
public class DBUtil {

    private static ResourceBundle bundle = ResourceBundle.getBundle("resources/jdbc");
    private static String driver = bundle.getString("driver");
    private static String url = bundle.getString("url");
    private static String user = bundle.getString("user");
    private static String password = bundle.getString("password");

    // 不让创建对象,工具类中的方法都是静态的，不需要创建对象
    // 防止创建对象，将构造方法私有化
    private DBUtil(){}

    // DBUtil类加载时注册驱动
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 创建ThreadLocal对象存Connection对象
    private static ThreadLocal<Connection> local = new ThreadLocal<>();

    /**
     * 没有使用连接池，直接创建的连接对象
     * @return 返回连接对象
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        // 通过ThreadLocal的get()方法获取Connection对象
        Connection conn = local.get();
        if (conn == null) {
            // 第一次调用get()方法，conn一定为空
            conn = DriverManager.getConnection(url, user, password);
            // 将new的connection对象绑定到Map中
            local.set(conn);
        }
        return conn;
    }

    /**
     * 关闭资源
     * @param conn 连接对象
     * @param stmt 数据库操作对象
     * @param rs 结果集对象
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
                // Tomcat服务器内置了一个线程池，线程池中有很多线程对象
                // 这些线程对象t1,t2,t3都是提前创建好的，
                // 也就是说t1,t2,t3存在重复使用现象
                // 也就是说一个人用过了t1线程，t1线程还有可能别其他用户使用
                local.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
