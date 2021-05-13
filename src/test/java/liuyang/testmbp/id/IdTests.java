package liuyang.testmbp.id;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.ImadcnIdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author liuyang
 * @scine 2021/5/13
 */
@Slf4j
public class IdTests {

    @Test
    public void test311() {
        log.info("test311");

        // ID_WORKER
        long id = IdWorker.getId();// long类型 19位 见IdWorker
        log.info(String.valueOf(id));// e.g. 1392713952255700993

        // UUID
        String uuid = IdWorker.get32UUID();
        log.info(uuid);// e.g. b6a328f850b3e33c51492c3182b1557d
    }

    @Test
    public void test342() {
        log.info("test342");

        // 3.4.2 变为可配置的IdentifierGenerator
        IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();// 另外一个实现是：ImadcnIdentifierGenerator

        // ASSIGN_ID
        // 这样转来转去是因为接口IdentifierGenerator的nextId方法定义的是Number类型。
        Long id = Long.valueOf(identifierGenerator.nextId(null).longValue());// Long类型 19位 见DefaultIdentifierGenerator
        log.info(String.valueOf(id));// e.g. 1392714106341875713

        // ASSIGN_UUID
        String uuid = identifierGenerator.nextUUID(null);
        log.info(uuid);// e.g. 833ad174273271711df550ce1ae17e95
    }
}
