package com.powernode.bank.pojo;

/**
 * 账户实体类:封装账户信息
 * 一般是一张表一个
 * 专门封装数据的java对象 bean对象（pojo对象）（domain对象）
 * @author b
 * @version 1.0
 * @since 1.0
 */
public class Account {
    // 一般属性不建议设计为基本数据类型，建议使用包装类，防止null带来的问题
    /**
     * 主键
     */
    private Long id;
    /**
     * 账号
     */
    private String actno;
    /**
     * 余额
     */
    private Double balance;

    public Account() {
    }

    public Account(Long id, String actno, Double balance) {
        this.id = id;
        this.actno = actno;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActno() {
        return actno;
    }

    public void setActno(String actno) {
        this.actno = actno;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", actno='" + actno + '\'' +
                ", balance=" + balance +
                '}';
    }
}
