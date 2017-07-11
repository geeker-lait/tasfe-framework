package com.tasfe.framework.crud.test.dao;

import com.tasfe.framework.crud.test.model.entity.User;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
public interface UserMapper {

    List<User> findUsers();

    User getUser(User user);
}
