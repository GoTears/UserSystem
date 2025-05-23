package com.tears.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tears.usercenter.common.ErrorCode;
import com.tears.usercenter.exception.BusinessException;
import com.tears.usercenter.mapper.UserMapper;
import com.tears.usercenter.model.domain.User;
import com.tears.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tears.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户实现类
* @author Taco
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-04-27 19:21:31
*/
@Service
//@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);


    /**
     * 混淆密码
     */
    public static final String SALT = "tears";


    @Resource
    private UserMapper userMapper;

    @Override
    public long userResister(String userAccount, String userPassword, String checkPassword) {
        //1.check

        //查看账户密码等是否为空
        if (StringUtils.isAnyEmpty(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        //查看账号长度是否小于4
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        //查看密码是否小于8
//        if (userPassword.length() < 8){
//            throw new BusinessException(ErrorCode.NULL_ERROR, "用户密码过短");
//        }

        //检查密码栏的密码长度不能小于8且密码长度不能小于8
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        //账号不能重复
        QueryWrapper<User> queryWarpper = new QueryWrapper<>(); // MyBatis-Plus 的标准类名
        queryWarpper.eq("userAccount", userAccount); // 字段名应与数据库表字段名一致
        long count = userMapper.selectCount(queryWarpper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能重复");
        }

        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
//            return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }

        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
//            return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码和校验密码相同");
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

    //Login major
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
//            return null;
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }

        //用户账户信息脱敏
       User safetyUser = getSafetyUser(user);

        //记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return safetyUser;
    }


    @Override
    @Transactional
    public User updateUser(Long id, String username, String newEmail, String newPhone) {
        // 1. 校验用户是否存在
        User existingUser = this.getById(id);
        if (existingUser == null) {
//            return null;
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }

        //校验用户名是否重复
        QueryWrapper<User> queryWarpper = new QueryWrapper<>(); // MyBatis-Plus 的标准类名
        queryWarpper.eq("username", username); // 字段名应与数据库表字段名一致
        queryWarpper.ne("id", id);
        long count = userMapper.selectCount(queryWarpper);
        if (count > 0) {
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名重复");
        }

        // 3. 合并字段（避免 null 覆盖）
        if (newEmail != null) {
            existingUser.setEmail(newEmail);
        }
        if (newPhone != null) {
            existingUser.setPhone(newPhone);
        }
        if (username != null) {
            existingUser.setUsername(username);
        }

        this.updateById(existingUser);
        return existingUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request){
        /*
        try {
            request.getSession().removeAttribute(USER_LOGIN_STATE);
            return true;
        }catch (Exception e){
            e.printStackTrace();
          throw new BusinessException(ErrorCode.PARAMS_ERROR,"删除用户不存在");
        }*/
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        session.removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 用户脱敏（单独写一个方法）
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;

    }

}


