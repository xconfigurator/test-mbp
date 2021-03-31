package liuyang.testmbp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import liuyang.testmbp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyBatis-Plus 入门
 *
 * @author liuyang
 * @scine 2021/3/29
 */
@SpringBootTest
@Slf4j
public class UserMapperEssentialTest {

    @Autowired
    private UserMapper userMapper; // 如果是用@MapperScan就会标红，但运行没有问题。

    // 1-5 快速入门小例子
    @Test
    public void select() {
        List<User> users = userMapper.selectList(null);
        // Assertions.assertEquals(5, users.size());
        users.forEach(System.out::println);

        int count = userMapper.selectCount(null);
        log.info(" count = " + count);

        Assertions.assertEquals(count, users.size());
    }

    // 2-x 演示通用Mapper以及实体类上的注解
    @Test
    @RepeatedTest(5)
    public void insert() {
        User user = new User(); // ID 是MP
        user.setAge(38);
        user.setEmail("xconfigurator@126.com");
        user.setName("liuyang");
        user.setManagerId(1088248166370832385l); // 王天风的手下
        user.setCreateTime(LocalDateTime.now());
        user.setRemark("FOO BAR");
        int rows = userMapper.insert(user);
        log.info("影响记录数：" + rows);
    }

    // 3-1 普通查询
    @Test
    public void selectById() {
        Long id = 1094590409767661570l;
        User user = userMapper.selectById(id);
        log.info(user.toString());
        Assertions.assertEquals("张雨琪", user.getName());
    }

    // 3-1 普通查询
    @Test
    public void selectBatchIds() {
        List<Long> ids = Arrays.asList(1094592041087729666l, 1088248166370832385l, 1088250446457389058l);
        List<User> users = userMapper.selectBatchIds(ids);
        users.forEach(System.out::println);
    }

    // 3-1 普通查询
    @Test
    public void selectByMap() {

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "liuyang");
        columnMap.put("age", 38);
        columnMap.put("manager_id", 1088248166370832385l);
        // columnMap.put("managerId", 1088248166370832385l); // 报错！ 注意，这个key是指的数据库中的列, 而不是实体的属性名
        List<User> users = userMapper.selectByMap(columnMap); // 注意观察MBP生成的SQL
        users.forEach(System.out::println);
    }

    // 3-2 条件构造器查询1 Wrapper
    /*
    1、名字中包含雨并且年龄小于40
	name like '%雨%' and age<40
     */
    @Test
    public void selectByWrapper01() {
        // 创建方法1：
        // QueryWrapper<User> queyrWrapper = new QueryWrapper<>();
        // 创建方法2：
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "雨").lt("age", 40); // 注意这个也是列名而不是实体的属性名

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /*
    2、名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
   name like '%雨%' and age between 20 and 40 and email is not null
     */
    @Test
    public void selectByWrapper02() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "雨").between("age", 20, 40).isNotNull("email");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /*
    3、名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
   name like '王%' or age>=25 order by age desc,id asc
     */
    @Test
    public void selectByWrapper03() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.likeRight("name", "王").or().ge("age", 25).orderByDesc("age").orderByAsc("id");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 3-3 条件构造器查询2 Wrapper
    /*
    4、创建日期为2019年2月14日并且直属上级为名字为王姓
      date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
     */
    @Test
    public void selectByWrapper04() {// 练习带函数的如何编写
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", "2019-02-14").inSql("manager_id", "select id from user where name like '王%'");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 3-4 条件构造器查询3 Wrapper
    /*
    5、名字为王姓并且（年龄小于40或邮箱不为空）
    name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void selectByWrapper05() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.likeRight("name", "王").and(qw -> qw.lt("age", 40).or().isNotNull("email"));

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /*
    6、名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
    name like '王%' or (age<40 and age>20 and email is not null)
     */
    @Test
    public void selectByWrapper06() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.likeRight("name", "王").or(qw -> qw.lt("age", 40).gt("age", 20).isNotNull("email"));

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 3-5 条件构造器查询4 Wrapper
    /*
    7、（年龄小于40或邮箱不为空）并且名字为王姓
    (age<40 or email is not null) and name like '王%'
     */
    @Test
    public void selectByWrapper07() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email")).likeRight("name", "王");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /*
    8、年龄为30、31、34、35
    age in (30、31、34、35)
     */
    @Test
    public void selectByWrapper08() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.in("age", Arrays.asList(30, 31, 34, 35));

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /*
    9、只返回满足条件的其中一条语句即可
    limit 1
     */
    @Test
    public void selectByWrapper09() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        // queryWrapper.in("age", Arrays.asList(30, 31, 34, 35, 38));
        queryWrapper.in("age", Arrays.asList(30, 31, 34, 35, 38)).last("limit 1");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 3-6 select 不列出全部字段
    /*
    10、名字中包含雨并且年龄小于40(需求1加强版)
    第一种情况：select id,name
                   from user
                   where name like '%雨%' and age<40
    第二种情况：select id,name,age,email
                   from user
                   where name like '%雨%' and age<40
     */
    @Test
    public void selectByWrapper1001() {
        // 比较selectByWrapper01
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.select("id", "name").like("name", "雨").lt("age", 40); // 注意这个也是列名而不是实体的属性名

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 差集
    @Test
    public void selectByWrapper1002() {
        // 演示如果列非常多的情况下的处理方式
        // select也是可以写在后面的
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "雨").lt("age", 40)
                .select(User.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("create_time") && !tableFieldInfo.getColumn().equals("manager_id"));

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 3-7 condition 作用
    @Test
    public void selectByWrapper3070Condition() {
        // 模拟输入
        // String name = "雨";
        String name = null;
        // String email = null;
        // String email = "";
        String email = "xconfigurator@126.com";
        // int age = 40;

        // 查询器
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name).like(StringUtils.isNotBlank(email), "email", email);

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 3-8 实体作为条件构造器构造方法参数
    @Test
    public void selectByWrapper3080Entity() {
        // entity不为null的值将被作为查询条件，默认使用等值查询
        // 注意entity的属性和后面构造器方法共同作用的结果。
        // 若默认等值不满足要求，需要like，则需要在entity相关字段上使用@TableField(condition=SqlCondition.XXX)来修改
        // SqlCondiion里面没有提供需要的怎么办？答：照猫画虎。常量没有，就直接依照模式赋值啊。

        // User
        User user = new User();
        user.setName("liuyang");

        // 查询器
        QueryWrapper<User> queryWrapper = Wrappers.<User>query(user);
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE name=?
        queryWrapper.like("name", "雨");
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE name=? AND (name LIKE ?)

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 3-9 AllEq用法
    @Test
    public void selectByWrapper3090AllEq() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", 25);
        params.put("age", null);
        // queryWrapper.allEq(params);// is null
        // queryWrapper.allEq(params, false); // 忽略null
        queryWrapper.allEq((k, v) -> !k.equals("name"), params);// name不被加入where
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 3-10 其他使用条件构造器的方法
    @Test
    public void selectByWrapper3100SelectMaps() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "雨").lt("age", 40);

        // 对比
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);

        // selectMaps
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }

    /*
     统计查询
     11、按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。
        并且只取年龄总和小于500的组。
        select avg(age) avg_age,min(age) min_age,max(age) max_age
        from user
        group by manager_id
        having sum(age) <500
     */
    @Test
    public void selectByWrapper3100Statistics() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
                .groupBy("manager_id")
                .having("sum(age) < {0}", 500);
        // SELECT avg(age) avg_age,min(age) min_age,max(age) max_age FROM user GROUP BY manager_id HAVING sum(age) < ?

        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }

    /**
     * 只返回第一个字段的值（参考源码注释）
     */
    @Test
    public void selectByWrapper3100SelectObjs() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();

        // List<Object> objects = userMapper.selectObjs(queryWrapper);
        List<Object> objects = userMapper.selectObjs(null);
        for (Object object : objects) {
            System.out.println(object);
        }
    }

    @Test
    public void selectByWrapper3100SelectCount() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "雨");

        Integer integer = userMapper.selectCount(queryWrapper);
        log.info(" selectByWrapper3100SelectCount = " + integer);
        Assertions.assertEquals(2, integer);
    }

    @Test
    public void selectByWrapper3100SelectOne() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.like("name", "刘红雨").lt("age", 40);// 只有一条记录 OK
        // queryWrapper.like("name", "雨").lt("age", 40);// 有两条记录 报错！

        User user = userMapper.selectOne(queryWrapper);
        log.info(user.toString());
    }


    // 3-11 Lambda条件构造器
    @Test
    public void selectByWrapper3110Lambda01() {
        // 一共有四种方法
        // 方法1
        LambdaQueryWrapper<User> l1 = new QueryWrapper<User>().lambda();
        // 方法2
        LambdaQueryWrapper<User> l2 = new LambdaQueryWrapper<>();
        // 方法3
        LambdaQueryWrapper<User> l3 = Wrappers.<User>lambdaQuery();
        // 方法4 （略有不同）
        // 见 selectByWrapper3110LambdaType04()

        l3.like(User::getName, "雨").lt(User::getAge, 40);// 好处在于可以避免数据库字段误写（所有映射经过实体类）

        List<User> users = userMapper.selectList(l3);
        users.forEach(System.out::println);
    }

    // 使用Lambda防止误写字段（编译期即可检查出）
    @Test
    public void selectByWrapper3110Lambda02() {
        /*
        5、名字为王姓并且（年龄小于40或邮箱不为空）
    name like '王%' and (age<40 or email is not null)
         */
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        userLambdaQueryWrapper.likeRight(User::getName, "王").and(qw -> qw.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        List<User> users = userMapper.selectList(userLambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapper3110LambdaType04() {
        // 3.0.7 新增 (实质上是又包装了一层)
        LambdaQueryChainWrapper<User> userLambdaQueryChainwrapper = new LambdaQueryChainWrapper<User>(userMapper);

        List<User> users = userLambdaQueryChainwrapper.like(User::getName, "雨").ge(User::getAge, 20).list();
        users.forEach(System.out::println);
    }

    // 4-1 自定义SQL
    // 在UserMapper中定制
    @Test
    public void customizeSQL() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        userLambdaQueryWrapper.likeRight(User::getName, "王").and(qw -> qw.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        // 对比一下BaseMapper提供的方法
        List<User> users = userMapper.selectList(userLambdaQueryWrapper);
        users.forEach(System.out::println);

        // 自定义：sql写在注解上
        List<User> users1 = userMapper.customizeSelectAll(userLambdaQueryWrapper);
        users1.forEach(System.out::println);

        // 自定义：sql写在XML文件里
        /**
         * application.yml中添加*mapper.xml的路径
         */
        List<User> users2 = userMapper.customizeSelectAllDefineInXML(userLambdaQueryWrapper);
        users2.forEach(System.out::println);
    }

    // 4-2 分页查询
    /**
     * 需要配置分页插件。但3.1（PDT导入的包）和目前新版本3.4插件配置方式不一样。
     * 另外用法：连接不同的数据库，可以查看不同产品的分页语句。（需要配合日志输出）
     */
    @Test
    public void selectPage() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.ge("age", 26);

        Page<User> userPage = new Page<>(1, 5);
        IPage<User> iPage = userMapper.selectPage(userPage, queryWrapper);
        log.info("总页数： " + iPage.getPages());
        log.info("总记录数：" + iPage.getTotal());
        List<User> records = iPage.getRecords();
        records.forEach(System.out::println);
    }

    @Test
    public void selectMapsPage() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.ge("age", 26);

        Page<Map<String, Object>> userPage = new Page<>(1, 5);
        IPage<Map<String, Object>> iPage = userMapper.selectMapsPage(userPage, queryWrapper);
        log.info("总页数： " + iPage.getPages());
        log.info("总记录数：" + iPage.getTotal());
        List<Map<String, Object>> records = iPage.getRecords();
        records.forEach(System.out::println);
    }

    // 实现京东那种下滑式分页（不需要总记录数，用户只需要往下滑动，即服务端只要不断提供记录即可。）
    // 使用Page对象中带三个参数的构造（第三个参数isSearchCount=false则表示不需要查询总记录数）
    @Test
    public void selectWaterfall() {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.ge("age", 26);

        Page<User> userPage = new Page<>(1, 5, false);
        IPage<User> iPage = userMapper.selectPage(userPage, queryWrapper);
        log.info("总页数： " + iPage.getPages());// 0
        log.info("总记录数：" + iPage.getTotal());// 0
        List<User> records = iPage.getRecords();
        records.forEach(System.out::println);
    }

    // 多表连查的分页（4-2 分页视频10:46）
    // 在UserMapper中定制方法, 形式参考UserMapper.customizeSelectAllDefineInXML中将SQL写入XML的方式。


    // 5-1 更新方法
    // 5-1 1. 根据ID更新
    @Test
    public void updateById() {
        User user = new User();
        user.setId(1376385836864389122l);
        user.setAge(28);
        int rows = userMapper.updateById(user);
        log.info("影响记录数：" + rows);
    }

    // 5-2 2. 以条件构造器作为参数的更新方法
    @Test
    public void update01() {
        /**
         * 根据 whereEntity 条件，更新记录
         *
         * @param entity        实体对象 (set 条件值,可以为 null)
         * @param updateWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
         */
        User user = new User();
        user.setAge(30);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", 1376385836864389122l);

        int rows = userMapper.update(user, updateWrapper);
        log.info("影响记录数：" + rows);
    }

    @Test
    public void update02() {
        User user = new User();
        user.setAge(31);

        User whereUser = new User();
        whereUser.setId(1376385836864389122l);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>(whereUser);
        updateWrapper.gt("age", 17);

        int rows = userMapper.update(user, updateWrapper);
        log.info("影响记录数：" + rows);
    }

    @Test
    public void update03() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", 1376385836864389122l).set("age", 38);

        int rows = userMapper.update(null, updateWrapper);
        log.info("影响记录数：" + rows);
    }

    @Test
    public void update04() {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();
        userLambdaUpdateWrapper.eq(User::getName, "李艺伟").eq(User::getAge, 37).set(User::getAge, 31);

        int rows = userMapper.update(null, userLambdaUpdateWrapper);
        log.info("影响记录数：" + rows);
    }

    @Test
    public void update05() {
        boolean rows = new LambdaUpdateChainWrapper<User>(userMapper).eq(User::getName, "李艺伟").eq(User::getAge, 31).set(User::getAge, 22).update();
        log.info("影响记录数；" + rows);

    }

    // 5-2 删除方法
    @Test
    public void deleteById() {
        int i = userMapper.deleteById(1376771393282195457l);
        log.info("影响记录数：" + i);
    }

    // 使用方法与selectByMap类似
    @Test
    public void deleteByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        // columnMap.put("id", 1376771393231863810l);
        int i = userMapper.deleteByMap(columnMap);
        log.info("影响记录数：" + i);
    }

    @Test
    public void deleteBatchIds() {
        int i = userMapper.deleteBatchIds(Arrays.asList(1376771393185726466l, 1376771393152172034l, 1376771393101840385l));
        log.info("影响记录数：" + i);
    }

    @Test
    public void delete() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        // userLambdaQueryWrapper.eq(User::getAge, 37).or().gt(User::getAge, 41); // 呵，还有外键约束
        userLambdaQueryWrapper.eq(User::getName, "liuyang");
        int i = userMapper.delete(userLambdaQueryWrapper);
        log.info("影响记录数：" + i);

    }

    // 6-1 AR模式
    // User extends Model<User>
    // ActiveRecord 具体操作参见Model


    // 6-2 主键策略
    /**
     * 枚举类：IdType
     * 默认主键生成策略：雪花算法 (如果ID不设置则跟随全局设置, type=IdType.NONE)
     * 局部主键生成策略：通过@TableId设置 e.g. @TableId(type=IdType.AUTO) <-- 适配MySQL的auto_increment
     * 全局主键生成策略：mybatis-plus.global-config.db-config.id-type=
     * 局部策略优先级高于全局策略
     */

    // 6-3 基本配置
    /**
     * https://baomidou.com/config/
     * configLocation
     * mapperLocations
     * *typeAliasesPackages
     *
     * configuration
     *  mapUnderscoreToCamelCase
     *
     * DbConfig
     *  idType
     *  tablePrefix
     */


    // 7-1 通用Service
    /**
     * IService<User>
     */

}