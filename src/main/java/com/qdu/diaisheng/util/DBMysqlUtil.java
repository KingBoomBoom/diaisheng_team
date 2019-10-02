package com.qdu.diaisheng.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBMysqlUtil {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String dbDriver = null;
    private String dbConnectionURL = null;
    private String dbUsername = null;
    private String dbPassword = null;


    public DBMysqlUtil(String dbDriver, String dbConnectionURL, String dbUsername,String dbPassword){
        this.dbDriver = dbDriver;
        this.dbConnectionURL = dbConnectionURL;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }
    /**
     * 功能：获取数据库连接
     */
    public Connection getConnection() {
        System.out.println("连接地址："+dbConnectionURL);
        System.out.println("用户名："+dbUsername);
        System.out.println("密码："+dbPassword);
        try {
            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbConnectionURL, dbUsername,
                    dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 功能：执行查询语句
     */
    public ResultSet select(String sql) {
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;

    }

    /**
     * 功能：执行查询语句，获取记录数
     */
    public int getRecordCount(String sql) {
        int counter = 0;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            while (rs.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        System.out.println("counter总数："+counter);
        return counter;
    }

    /**
     * 功能:针对单条记录执行更新操作(新增、修改、删除)
     */
    public int executeupdate(String sql) throws Exception {
        int num = 0;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            num = ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            close();
        }
        System.out.println("影响条数："+num);
        return num;
    }

    /**
     *
     * 功能:批量执行SQL(update或delete)
     *
     * @param sqlList
     *            sql语句集合
     */
    public int executeBatch(List<String> sqlList) {
        int result = 0;
        for (String sql : sqlList) {
            try {
                result += executeupdate(sql);
            } catch (Exception e) {
                System.out.println("查询异常："+e.getMessage());
            }
        }
        System.out.println("executeBatch Result:"+result);
        return result;
    }

    /**
     * 功能:关闭数据库的连接
     */
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}