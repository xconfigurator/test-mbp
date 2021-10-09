package liuyang.testmbp.service.impl;

import liuyang.testmbp.entity.Article;
import liuyang.testmbp.mapper.ArticleMapper;
import liuyang.testmbp.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuyang
 * @since 2021-09-24
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
