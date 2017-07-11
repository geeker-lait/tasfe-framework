package com.tasfe.framework.crud.test.mysql.impl;

import com.tasfe.framework.crud.test.business.UserBusiness;
import com.tasfe.framework.crud.test.mysql.dao.UserMapper;
import com.tasfe.framework.crud.test.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
@Service
public class UserBusinessImpl  implements UserBusiness {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> findUsers() {
        userMapper.findUsers();
        return null;
    }
}
