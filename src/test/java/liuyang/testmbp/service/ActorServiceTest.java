package liuyang.testmbp.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import liuyang.testmbp.entity.Actor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * MySQL 示例数据库Sakila中的actor表
 *
 * @author liuyang
 * @scine 2021/9/16
 */
@SpringBootTest
@Slf4j
public class ActorServiceTest {

    @Autowired
    private ActorService actorService;

    @Test
    void testQuery() {
        actorService.list().stream().forEach(System.out::println);
    }

    @Test
    void testQueryPage() {
        // Page
        LambdaQueryWrapper<Actor> actorLambdaQueryWrapper = Wrappers.<Actor>lambdaQuery();
        actorLambdaQueryWrapper.orderByDesc(Actor::getActorId);

        Page<Actor> page = new Page<>(1, 10);
        Page<Actor> pageData = actorService.page(page, actorLambdaQueryWrapper);
        List<Actor> records = pageData.getRecords();
        records.stream().forEach(System.out::println);

        //log.info(" pageData = {}", JSON.toJSONString(pageData));
        // resp.setIsEnd(pageData.getCurrent() == pageData.getPages() ? 1 : 0);
        System.out.println("pageData.getCurrent()=" + pageData.getCurrent());
        System.out.println("pageData.getPages()=" + pageData.getPages());
        System.out.println("isEnd = " + (pageData.getCurrent() >= pageData.getPages() ? 1 : 0));
    }

    @Test
    void testUpdate() {
        actorService.lambdaUpdate().eq(Actor::getActorId, 1).set(Actor::getLastName, "xxx").update();
    }


    @Test
    void testDelete() {
        System.out.println(actorService.list().size());

        LambdaQueryWrapper<Actor> wrapper = Wrappers.<Actor>lambdaQuery();
        wrapper.eq(Actor::getFirstName, "NICK");
        wrapper.eq(Actor::getLastName, "WAHLBERG");
        actorService.remove(wrapper);

        System.out.println(actorService.list().size());
    }
}
