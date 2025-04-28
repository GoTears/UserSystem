package com.tears.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tears.usercenter.mapper.UserMapper;
import com.tears.usercenter.model.domain.User;
import com.tears.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 用户实现类
* @author Taco
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-04-27 19:21:31
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public long userResister(String userAccount, String userPassword, String checkPassword) {
        //1.check
        if (StringUtils.isAnyEmpty(userAccount, userPassword, checkPassword)){
            return -1;
        }
        if (userAccount.length()<4 ){
            return -1;
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }
        //账户重复查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count();
        if (count > 0){
            return -1;
        }
        // 账号不能包含特殊字符
        return 0;

    }
}




