package com.tears.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tears.usercenter.model.domain.User;
import com.tears.usercenter.model.domain.request.UserLoginRequest;
import com.tears.usercenter.model.domain.request.UserRegisterRequest;
import com.tears.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tears.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.tears.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 *
 * 用户接口
 * @author  Tears
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank()){
            return null;
        }
        return userService.userResister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank()){
            return null;
        }
        return userService.userLogin(userAccount,userPassword,request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request){
        //管理猿可查(这里偷懒了，正常要在业务层写搜索逻辑)
        if(!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
//      return userService.list(queryWrapper);
        //快速脱敏将密码返回为空（若测试要看信息可以将这段代码注释将上面代码释放 Java语法糖写法,后面会优化）
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    /**
     * 更新用户信息
     * @param id
     * @param username
     * @param newEmail
     * @param newPhone
     * @return
     */
    @PostMapping("/update")
    public User updateUser(@RequestParam Long id,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String newEmail,
                           @RequestParam(required = false) String newPhone){
//        User updatingUser = userService.updateUser(id, username, newEmail, newPhone);
        return userService.updateUser(id, username, newEmail, newPhone);
    }

    /**
     * 用户删除，会触发逻辑删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request){
        if (!isAdmin(request)){
            return false;
        }
        if (id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 鉴权
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
