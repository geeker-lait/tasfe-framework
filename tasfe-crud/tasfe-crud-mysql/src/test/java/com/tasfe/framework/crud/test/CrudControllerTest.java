package com.tasfe.framework.crud.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tasfe.framework.crud.api.params.CrudParam;
import com.tasfe.framework.crud.test.model.entity.User;
import com.tasfe.framework.crud.mysql.type.DataState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lait on 2017/4/15
 */
@Controller
public class CrudControllerTest {

  /*  @Autowired
    private CrudBusiness crudBusiness;

    //@Resource(name = "generalDao")
    @Autowired
    private RdbCrudDao dao;*/

    @RequestMapping(value = "/test/crud/selectByPrimaryKey.json")
    @ResponseBody
    public User test_select() throws Exception {

		/*Map<String, Object> result = new HashMap<String, Object>();

		result = service.selectByPrimaryKey(User.class, 1);*/
        //User user = crudBusiness.selectByPrimaryKey(User.class, 1L);

        return null;
    }

    @RequestMapping(value = "/test/crud/insertSelective.json")
    @ResponseBody
    public Map<String, Object> test_insert() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        User user = new User();
        user.setDeptId(1);
        user.setEmail("lait.zhang@gmail.com");
        user.setJob("java programer");
        //user.setLoginName("lait");
        user.setMobilePhone("15801818092");
        user.setOfficePhone("15121118910");
        user.setPassword("123456");
        user.setStatus(DataState.UNEFFECT);
        user.setUserName("lait");

        //int i = crudBusiness.insertSelective(user);

        //result.put("row", i);

        return result;
    }

    @RequestMapping(value = "/test/crud/insertBatch.json")
    @ResponseBody
    public Map<String, Object> test_insertBatch() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        List<User> list = new ArrayList<User>();

        for (int i = 1; i < 10; i++) {
            User user = new User();
            user.setDeptId(1 + i);
            user.setEmail("lait.zhang@gmail.com" + i);
            user.setJob("java programer" + i);
            //user.setLoginName("lait" + i);
            user.setMobilePhone("15801818092" + i);
            user.setOfficePhone("15121118910" + i);
            user.setPassword("123456" + i);
            user.setStatus(DataState.UNEFFECT);
            user.setUserName("lait" + i);
            list.add(user);
        }

        //int num = crudBusiness.insertBatch(list);

        //result.put("row", num);

        return result;
    }

    @RequestMapping(value = "/test/crud/updateByPrimaryKey.json")
    @ResponseBody
    public Map<String, Object> test_update() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        User user = new User();
        user.setUserId(13L);
        user.setDeptId(1);
        user.setEmail("lait.zhang@gmail.com");
        user.setJob("java programer");
        user.setStatus(DataState.UNEFFECT);
        user.setUserName("lait");

        /*int i = crudBusiness.updateByPrimaryKey(user);

        result.put("row", i);
*/
        return result;
    }

    @RequestMapping(value = "/test/crud/updateByPrimaryKeySelective.json")
    @ResponseBody
    public Map<String, Object> test_updateSelective() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        User user = new User();
        user.setUserId(12L);
        user.setDeptId(1);
        user.setEmail("lait.zhang@gmail.com");
        user.setJob("java programer");
        //user.setLoginName("lait");
        user.setMobilePhone("15801818092");
        user.setOfficePhone("15121118910");
        user.setPassword("123456");
        user.setStatus(DataState.UNEFFECT);
        user.setUserName("lait");

        /*int i = crudBusiness.updateByPrimaryKeySelective(user);

        result.put("row", i);*/

        return result;
    }

    @RequestMapping(value = "/test/crud/updateByCondition.json")
    @ResponseBody
    public Map<String, Object> test_updateByCondition() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> columnValueMapping = new HashMap<String, Object>();
        columnValueMapping.put("user_name", "update");
        columnValueMapping.put("dept_id", 99);
        columnValueMapping.put("state", DataState.EFFECT.ordinal());

        String conditionExp = "dept_id != #{conditionParam.deptId}";
        Map<String, Object> conditionParam = new HashMap<String, Object>();
        conditionParam.put("deptId", 1);

       /* int i = crudBusiness.updateByConditionSelective(User.class, columnValueMapping, conditionExp, conditionParam);

        result.put("row", i);
*/
        return result;
    }

    @RequestMapping(value = "/test/crud/deleteByPrimaryKey.json")
    @ResponseBody
    public Map<String, Object> test_delete() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

       /* int i = crudBusiness.deleteByPrimaryKey(User.class, "15");

        result.put("row", i);
*/
        return result;
    }

    @RequestMapping(value = "/test/crud/deleteByCondition.json")
    @ResponseBody
    public Map<String, Object> test_deleteByCondition() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        String condition = "dept_id != #{conditionParam.deptId}";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("deptId", 1);
        /*int i = crudBusiness.deleteByCondition(User.class, condition, param);

        result.put("row", i);*/

        return result;
    }

    @RequestMapping(value = "/test/crud/selectAdvanced.json")
    @ResponseBody
    public List<User> test_selectAdvanced() throws Exception {

        String conditionExp = "user_name = #{conditionParam.user_name}";

        Map<String, Object> conditionParam = new HashMap<String, Object>();
        conditionParam.put("user_name", "svili");

        String orderExp = "user_id";
        CrudParam crudParam = new CrudParam();

        crudParam.setConditionExp(conditionExp);
        crudParam.setConditionParam(conditionParam);
        crudParam.setOrderExp(orderExp);
        crudParam.setPageSize(3);
        crudParam.setPageNo(2);

        //List<User> result = crudBusiness.selectAdvanced(User.class, crudParam);

        return null;
    }

}
