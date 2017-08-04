# tasfe-framework
极客精神，工匠品质，10年磨一剑，只为追求理想和自由！tasfe-framework(The application solutions for enterprise)，万千瞩目，集思想与艺术与技术的完美结合，专为提高开发效率而生。你，值得拥有！——lait

V1.0.0
1.实现基于RDB的通用CRUD

===

通过在实体上的@Storager注解标注可直接将数据路由到指定的存储/索引库中
###@Storager(storage={StoragerType.*})
    StoragerType.MYSQL
    StoragerType.HBASE 
    StoragerType.HIVE
    StoragerType.ES
```
@Alias("User")
@Table(name = "t_user")
@Storager(storage={StoragerType.MYSQL,StoragerType.HBASE})
public class User{
    @Id
    private Long id;

    private Long userId;

    private Integer deptId;

    private String userName;

    private String password;

    private String email;

    ...

    @Override
    public int hashCode() {
        ...
    }

    @Override
    public boolean equals(Object obj) {
        ...
    }

    @Override
    public String toString() {
        ...
    }
}

```
###测试/Test(支持混合使用mapper)
```
/**
 * Created by Lait on 2017/4/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springs/spring-mybatis.xml"})
public class CrudTest {
    @Autowired
    UserMapper userMapper;

    @Resource(name="mysql")
    CrudTemplate crudTemplate;

    private User getUser(){
        User u = new User();
        AtomicLong al = new AtomicLong();
        Random random = new Random();
        u.setUserId(Long.valueOf(random.nextInt()));
        u.setDeptId(1);
        u.setOrderId(random.nextInt());
        u.setEmail("lait.zhang@gmail.com");
        u.setMobilePhone("15801818092");
        return u;
    }

    private List<User> getUsers(int count){
        List<User> userList = new ArrayList<User>();
        for(int i=0;i<count;i++){
            userList.add(getUser());
        }
        return userList;
    }

    @Test
    public void testInsert() throws Exception {

        crudTemplate.insert(getUser());

        crudTemplate.insertBatch(getUsers(10));

        Member member = new Member();
        member.setEmail("lait");
        member.setUserId(11L);
        member.setDeptId(1);
        member.setOrderId(111);
        crudTemplate.insert(member);

        // 自定义填充方式
        //User user1 = mysqlTemplate.forParam(user).exec("doXX").fill(User.class);
        //System.out.println("=========" + user);
    }



    @Test
    public void testGet() throws Exception {
        User user = new User();
        user.setId(25L);
        user = userMapper.getUser(user);
        user = crudTemplate.get(User.class,25L);
        List<User> users = crudTemplate.gets(User.class,1L,2L,3L);
        System.out.println(user +"====="+ users);
        users = crudTemplate.gets(user);
        System.out.println(user +"====="+ users.size());

        User user11 = mysqlTemplate.get(User.class,1L);
        User user1 = mysqlTemplate.forParam(user).exec("getUser").get();

        // 填充
        //User user1 = mysqlTemplate.forParam(user).exec("getUser").fill(User.class);
        //List<User> user1 = mysqlTemplate.get(user);
        //System.out.println(user11+ "--------------------------------" );

        //System.out.println(user +"====="+ users);
    }


    @Test
    public void testTemplateInsert() throws Exception {
        User u = getUser();
        crudTemplate.insert(u);
    }

```

###数据路由
```
    /**
     * 路由数据
     * @param crudParam
     * @return
     * @throws Exception
     * CrudMethod cm,Class<T> clazz,T entity,List<T> tlist,QueryParam param
     */
    @Route
    public Object route(QueryParam crudParam) throws Exception {
        Class<?> clazz = crudParam.getQueryClazz();
        if(clazz !=null ){
            Storager storager = clazz.getAnnotation(Storager.class);
            Crudable crudable = null;
            StoragerType[] storagerTypes = null;
            if (storager != null) {
                storagerTypes = storager.storage();
            } else {
                storagerTypes = new StoragerType[]{StoragerType.MYSQL};
            } 

            for (StoragerType storagerType : storagerTypes) {
                crudable = (Crudable)applicationContext.getBean(storagerType.toString().toLowerCase());
                CrudMethod cm = CrudMethod.get(crudParam.getCrudMethod());
                switch (cm) {
                    case IN:
                        crudable._in(crudParam.getEntity());
                        break;
                    case INS:
                        crudable._ins(crudParam.getEntityList());
                        break;
                    case GET:
                        return crudable._get(clazz,crudParam.getPk());
                    case GETS:
                        return crudable._gets(clazz,crudParam);
                    case COUNT:
                        return crudable._count(clazz,crudParam);
                    case LIST:
                        // 待完成
                        break;
                    case FIND:
                        // 待完成
                        break;
                    case UPD:
                        return crudable._upd(crudParam.getEntity());
                    case UPDS:
                        crudable.upds(tlist);
                        break;
                    case DEL:
                        crudable._del(crudParam);
                        break;
                    case DELS:
                        // 待完成
                        break;
                }
            }
        } else {
            // 做点提示
        }
        return null;
    }

```
