package com.docker.mapper;

import com.docker.entity.User;

/**
 * Created by CHEN on 2019/12/10.
 */
public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User selUser(String username);

}
