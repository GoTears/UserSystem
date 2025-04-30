package com.tears.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tears.usercenter.mapper.UserMapper;
import com.tears.usercenter.model.domain.User;
import com.tears.usercenter.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户实现类
* @author Taco
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-04-27 19:21:31
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private static final String SALT = "tears";
    @Resource
    private UserMapper userMapper;

    @Override
    public long userResister(String userAccount, String userPassword, String checkPassword) {
        //1.check

        //查看账户密码等是否为空
        if (StringUtils.isAnyEmpty(userAccount, userPassword, checkPassword)){
            return -1;

        }
        //查看账号长度是否小于4
        if (userAccount.length() < 4){
            return -1;
        }
        //查看密码是否小于8
        if (userPassword.length() < 8){
            return -1;

        }
        if (userAccount.length()<4 ){
            return -1;
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }

        //账号不能重复
        QueryWrapper<User> queryWarpper = new QueryWrapper<>(); // MyBatis-Plus 的标准类名
        queryWarpper.eq("user_account", userAccount); // 字段名应与数据库表字段名一致
        long count = userMapper.selectAccount(queryWarpper);
        if (count > 0){
            return -1;
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }

        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //开始插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        this.save(user);
        return user.getId();
    }
}


