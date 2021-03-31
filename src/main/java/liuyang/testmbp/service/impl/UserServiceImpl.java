package liuyang.testmbp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import liuyang.testmbp.entity.User;
import liuyang.testmbp.mapper.UserMapper;
import liuyang.testmbp.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author liuyang
 * @scine 2021/3/30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
