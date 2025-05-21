package com.tears.usercenter.service;
import com.tears.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Taco
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-04-27 19:21:32
 */
public interface UserService extends IService<User> {



    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userResister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 脱敏信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 更新用户
     * @param id
     * @param username
     * @param newEmail
     * @param newPhone
     * @return
     */
    @Transactional
    User updateUser(Long id, String username, String newEmail, String newPhone);
}
