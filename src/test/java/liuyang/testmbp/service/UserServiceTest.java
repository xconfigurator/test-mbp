package liuyang.testmbp.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import liuyang.testmbp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author liuyang
 * @scine 2021/3/30
 */
@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getOne() {
        /*
        Assertions.assertThrows(TooManyResultsException.class, () -> {
            User one = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 20));
            log.info(one.toString());
        } );
         */
        Assertions.assertThrows(MyBatisSystemException.class, () -> {
            // 与BaseMapper的selectOne反应相同
            User one = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 20));
            log.info(one.toString());
        });
    }

    @Test
    public void getOneFalse() {
        // getOne(Wrapper, false); 这样调用，则多余一条记录返回查到的第一条记录
        // 这就与BaseMapper的selectOne效果不同了
        User one = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 20), false);
        log.info(one.toString());
    }

    // 批量操作
    /**
     * saveBatch
     * saveOrUpdateBatch
     * updateBatchById
     */

    // 使用Service层的Lambda条件构造器就比直接使用BaseMapper方便
    @Test
    public void chainSelect() {
        List<User> users = userService.lambdaQuery().gt(User::getAge, 25).like(User::getName, "雨").list();
        users.forEach(System.out::println);
    }

    @Test
    public void chainUpdate() {
        boolean updated = userService.lambdaUpdate().eq(User::getAge, 37).set(User::getAge, 18).update();
        log.info(" " + updated);
    }

    @Test
    public void chainRemove() {
        boolean removed = userService.lambdaUpdate().eq(User::getId, 1094592041087729666l).remove();
        log.info(" " + removed);
    }

}
