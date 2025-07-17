package com.tears.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tears.usercenter.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Taco
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-04-27 19:21:32
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {


}




