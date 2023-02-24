package com.powernode.bank.service.impl;

import com.powernode.bank.dao.AccountDao;
import com.powernode.bank.dao.impl.AccountDaoImpl;
import com.powernode.bank.exceptions.AppException;
import com.powernode.bank.exceptions.MoneyNotEnoughException;
import com.powernode.bank.pojo.Account;
import com.powernode.bank.service.AccountService;
import com.powernode.bank.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * service翻译为：业务
 * AccountService:专门处理Account业务的一个类
 * 在该类中编写纯业务代码（只专注业务，不写别的，不和其他代码混合）
 *
 * 业务类一般起名：XxxService、XxxBiz。。。
 *  方法起名要体现处理的是什么业务（一个业务对应一个方法）
 * @author b
 * @version 1.0
 * @since 1.0
 */
public class AccountServiceImpl implements AccountService {

    // 每一个方法中都需要连接数据库
    private AccountDao accountDao = new AccountDaoImpl();
    // 实现转账的业务

    /**
     * 完成转账的业务逻辑
     * @param fromActno 转出账号
     * @param toActno 转入账号
     * @param money 转账金额
     */
    public void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, AppException {
        // 在service层控制事务
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            // 开启事务（需要使用Connection对象）
            connection.setAutoCommit(false);
            // 查询转出账户余额是否充足
            Account fromAct = accountDao.selectByActno(fromActno);
            if (fromAct.getBalance() < money) {
                throw new MoneyNotEnoughException("余额不足");
            }

            // 余额充足，查询转入账户
            Account toAct = accountDao.selectByActno(toActno);
            // 修改余额(只是修改了内存中java对象的余额)
            fromAct.setBalance(fromAct.getBalance() - money);


            toAct.setBalance(toAct.getBalance() + money);
            // 更新数据库中的余额
            int count = accountDao.update(fromAct);

            // 模拟异常
            /*String s = null;
            s.toString();*/

            count += accountDao.update(toAct);
            if (count != 2) {
                throw new AppException("账户转账异常");
            }
            // 提交事务
            connection.commit();
        } catch (SQLException e) {
            throw new AppException("账户转账异常");
        } finally {
            DBUtil.close(connection, null, null);
        }

    }
}
