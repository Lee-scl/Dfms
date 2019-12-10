package com.docker.entity;

/**
 *  账户实体
 * Created by CHEN on 2019/11/26.
 */
public class User {
    //账户
    private String username;

    //密码
    private String password;

    //权力等级
    private Integer rank;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Integer rank) {
        this.username = username;
        this.password = password;
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rank=" + rank +
                '}';
    }
}
