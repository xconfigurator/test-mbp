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
public class User {// @TableName
    private Long id;// 主键 @TableId
    private String name;// 姓名 @TableField
    private Integer age;// 年龄
    private String email;// 邮箱
    private Long managerId;// 直属上级
    private LocalDateTime createTime;// 创建时间

    // private String remark; // 报错
    /**
     * 演示不参与到持久化的三种方法
     */
    // 方法1 transient （但注意，加上transient修饰后，该字段不参与序列化）
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
