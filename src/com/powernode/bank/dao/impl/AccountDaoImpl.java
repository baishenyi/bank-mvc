package com.powernode.bank.dao.impl;

import com.powernode.bank.dao.AccountDao;
import com.powernode.bank.pojo.Account;
import com.powernode.bank.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountDao负责Account数据的增删改查CRUD
 * 1、什么是DAO
 *      Data Access Object（数据库访问对象）
 * 2、DAO属于JavaEE的设计模式之一
 * 3、DAO只负责数据库表的CRUD，没有任何业务逻辑
 * 4、没有任何业务逻辑，只负责表中数据库增删改查的对象，称为DAO对象
 * 5、一般情况下：一张表对应一个DAO对象
 * 6、DAO中的方法起名一般为：
 *      insert
 *      deleteByXxx
 *      update
 *      selectByXxx
 *      selectAll
 * @author b
 * @version 1.0
 * @version 1.0
 */
public class AccountDaoImpl implements AccountDao {

    /**
     * 插入账户信息
     * @param act 账户信息
     * @return 1表示插入成功
     */
    public int insert(Account act){
        PreparedStatement ps = null;
        int count = 0;
        try {
            // 通过DBUtil工具栏获取Connection对象
            Connection conn = DBUtil.getConnection();
            String sql = "insert into t_act(actno, balance) values(?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, act.getActno());
            ps.setDouble(2, act.getBalance());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, null);
        }
        return count;
    }

    /**
     * 根据主键删除账户
     * @param id 主键
     * @return
     */
    public int deleteById(Long id){
        PreparedStatement ps = null;
        int count = 0;
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "delete from t_act where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, null);
        }
        return count;
    }

    /**
     * 更新账户
     * @param act
     * @return
     */
    public int update(Account act){
        PreparedStatement ps = null;
        int count = 0;
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "update t_act set balance = ? , actno = ? where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, act.getBalance());
            ps.setString(2, act.getActno());
            ps.setLong(3, act.getId());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, null);
        }
        return count;
    }

    /**
     * 根据账号查询账户
     * @param actno 账号
     * @return
     */
    public Account selectByActno(String actno){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Account act = null;
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "select id,balance from t_act where actno = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, actno);
            rs = ps.executeQuery();
            if (rs.next()){
                Long id = rs.getLong("id");
                Double balance = rs.getDouble("balance");
                // 将结果集封装成Account对象
                act = new Account();
                act.setId(id);
                act.setActno(actno);
                act.setBalance(balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, rs);
        }
        return act;
    }

    /**
     * 获取所有账户信息
     * @return
     */
    public List<Account> selectAll(){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Account> list = new ArrayList<>();
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "select id,actno,balance from t_act";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                // 取出数据
                Long id = rs.getLong("id");
                String actno = rs.getString("actno");
                Double balance = rs.getDouble("balance");
                //封装对象
                Account account = new Account();
                account.setId(id);
                account.setActno(actno);
                account.setBalance(balance);
                // 加到List集合
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, rs);
        }
        return list;
    }
}
