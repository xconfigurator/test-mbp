package liuyang.testmbp.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import liuyang.testmbp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liuyang
 * @scine 2021/3/29
 */
//@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 4-1 自定义SQL
     * 演示既使用Wrapper又使用自定义SQL的场景。
     * 其中：${ew.customSqlSegment}" 是固定写法。
     * customizeSelectAll               演示SQL写在注解上
     * customizeSelectAllDefineInXML    演示SQL写在XML文件里
     */
    @Select("select * from user ${ew.customSqlSegment}")
    List<User> customizeSelectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    List<User> customizeSelectAllDefineInXML(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    /**
     * 4-2 分页查询
     * 演示多表联查的分页（示例，还不是真正的多表。只展示这种实现方式。）
     * 参考customizeSelectAllDefineInXML，只需要把多表联结SQL写入XML
     */


}
