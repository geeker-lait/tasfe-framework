package com.tasfe.framework.crud.test.business;

import com.tasfe.framework.crud.business.CrudBusiness;
import com.tasfe.framework.crud.test.model.entity.User;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
public interface UserBusiness {
    List<User> findUsers();
}
