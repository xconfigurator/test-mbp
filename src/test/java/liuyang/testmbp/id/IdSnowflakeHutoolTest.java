package liuyang.testmbp.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author liuyang
 * @scine 2021/7/30
 */
@Slf4j
public class IdSnowflakeHutoolTest {

    private Snowflake snowflake;
    private long workerId = 1;      // 5位：1 - 31
    private long datacenterId = 1;  // 5位：1 - 31

    // 注：在普通的Bean中想完成类似的功能，可以使用@PostConstruct注解。这个是javax包中的。
    @BeforeEach
    void init() {
        snowflake = IdUtil.createSnowflake(workerId, datacenterId);
    }

    @Test
    void testSnowflake() {
        long id = snowflake.nextId();
        log.info("id = " + id);

        String idStr = snowflake.nextIdStr();
        log.info("idStr = " + idStr);
    }

    @Test
    void testFastUUID() {
        log.info(IdUtil.fastUUID());//005fe9fd-0fbc-483a-99bf-3407f997647a
    }

    @Test
    void testSimple() {
        log.info(IdUtil.fastSimpleUUID());//c06484fb787c4caa9e572deb661ce1ac
    }

    @Test
    void testGenidInBatchUsingMultiThread() throws InterruptedException {
        int batchSize = 50;// 一次产生50个

        // TODO 问题：如何更好的接收并发线程工作的执行结果？
        ArrayList<Long> longs = new ArrayList<>();

        // 启动5个线程同时干活
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < batchSize; ++i) {
            executorService.submit(()-> {
                //snowflake.nextId();
                longs.add(snowflake.nextId());// ?
            });
        }
        executorService.shutdown();

        // TODO 这个肯定是有问题的！！！
        TimeUnit.SECONDS.sleep(2);// 休眠2秒等待工作线程完成任务

        // 输出
        log.info("size = " + String.valueOf(longs.size()));
        longs.stream().forEach(e -> {
            System.out.println(e);
        });
        log.info("size = " + String.valueOf(longs.size()));
    }
}
