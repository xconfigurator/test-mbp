package liuyang.testmbp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * MySQL 示例数据库Sakila中的actor表
 *
 * @author liuyang
 * @scine 2021/9/16
 */
@SpringBootTest
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

    }
}
