package liuyang.testmbp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import liuyang.testmbp.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author liuyang
 * @scine 2021/9/24
 */
@SpringBootTest
@Slf4j
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void testQuery() {
        List<Article> list = articleService.list();
        list.stream().forEach(System.out::println);
    }

    @Test
    void testQueryDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = sdf.parse("2021-06-18 08:34:11");
        Date end = sdf.parse("2021-06-18 15:34:11");

        begin = sdf.parse("2021-06-18 11:34:11");//
        end = sdf.parse("2021-06-18 12:34:11");

        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = Wrappers.<Article>lambdaQuery();
        //articleLambdaQueryWrapper.between(Article::getCreateTime, begin, end); // ok []
        //articleLambdaQueryWrapper.gt(Article::getCreateTime, begin).lt(Article::getCreateTime, end);// ok ()
        //articleLambdaQueryWrapper.ge(Article::getCreateTime, begin).le(Article::getCreateTime, end);// ok []
        //articleLambdaQueryWrapper.ge(Article::getCreateTime, begin).le(false, Article::getCreateTime, end);// ok [
        articleLambdaQueryWrapper.ge(Article::getCreateTime, begin).le(true, Article::getCreateTime, end);// ok

        List<Article> list = articleService.list(articleLambdaQueryWrapper);
        list.stream().forEach(System.out::println);
    }

}
