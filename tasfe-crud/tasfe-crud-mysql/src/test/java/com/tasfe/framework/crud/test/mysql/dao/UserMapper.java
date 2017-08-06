package com.tasfe.framework.crud.test.mysql.dao;

import com.tasfe.framework.crud.api.Crudable;
import com.tasfe.framework.crud.test.model.entity.User;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
public interface UserMapper extends Crudable {

    List<User> findUsers();

    User getUser(User user);
}
