package liuyang.testmbp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author liuyang
 * @scine 2021/3/29
 */
@Data
@ToString
// @TableName e.g. @TableName("t_user")
public class User {
    // ////////////////////////////////////////////////////////
    // 主键 @TableId 如果属性名不叫id则需要加上这个注解，以告诉MP这个是主键。并且可以指定主键生成策略。
    // IdType.AUTO  == @GeneratedValue(strategy = GenerationType.IDENTITY)
    // IdType.INPUT == @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // ////////////////////////////////////////////////////////
    // 姓名 @TableField 指定表字段名 e.g.@TableField("real_name")
    private String name;
    private Integer age;// 年龄
    private String email;// 邮箱
    private Long managerId;// 直属上级
    private LocalDateTime createTime;// 创建时间

    // ////////////////////////////////////////////////////////
    // 演示不参与到持久化的三种方法
    // private String remark; // 报错
    // 方法1 transient
    // 但注意，加上transient修饰后，该字段不参与序列化。如果该VO有序列化的需求，则需要考虑其他方案。
    // private transient String remark;

    // 方法2 使用静态变量
    /*
    private static String remark;

    public static String getRemark() {
        return remark;
    }

    public static void setRemark(String remark) {
        User.remark = remark;
    }
    */

    // 方法3 @TableField(exist = false)
    @TableField(exist = false)
    private String remark;
}
