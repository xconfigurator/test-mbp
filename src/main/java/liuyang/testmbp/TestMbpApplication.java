package liuyang.testmbp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("liuyang.testmbp.mapper")
public class TestMbpApplication {

    public static void main(String[] args) {

        SpringApplication.run(TestMbpApplication.class, args);

    }

}
