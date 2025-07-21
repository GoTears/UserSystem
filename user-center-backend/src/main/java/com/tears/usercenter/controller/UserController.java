package com.tears.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tears.usercenter.common.BaseResponse;
import com.tears.usercenter.common.ErrorCode;
import com.tears.usercenter.common.ResultUtils;
import com.tears.usercenter.exception.BusinessException;
import com.tears.usercenter.model.domain.User;
import com.tears.usercenter.model.domain.request.UserLoginRequest;
import com.tears.usercenter.model.domain.request.UserRegisterRequest;
import com.tears.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
//            return ResultUtils.error(ErrorCode.NULL_ERROR);
            throw new BusinessException(ErrorCode.NULL_ERROR, "注册内容不能为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank()){
//            return null;
            throw new BusinessException(ErrorCode.NULL_ERROR, "注册内容不能为空");
        }
        long userResister = userService.userResister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(userResister);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
//            return null;
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank()){
//            return null;
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码为空");
        }
        User login = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(login);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        //管理猿可查(这里偷懒了，正常要在业务层写搜索逻辑)
        if(!isAdmin(request)){
//            return new ArrayList<>();
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
//      return userService.list(queryWrapper);
        //快速脱敏将密码返回为空（若测试要看信息可以将这段代码注释将上面代码释放 Java语法糖写法,后面会优化）
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
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
    public BaseResponse<User> updateUser(@RequestParam Long id,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String newEmail,
                           @RequestParam(required = false) String newPhone){
//        User updatingUser = userService.updateUser(id, username, newEmail, newPhone);
//        return userService.updateUser(id, username, newEmail, newPhone);
        User updateUser = userService.updateUser(id, username, newEmail, newPhone);
        return ResultUtils.success(updateUser);
    }

    /**
     * 用户删除，会触发逻辑删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        if (!isAdmin(request)){
//            return false;
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        if (id <= 0){
//            return false;
            throw new BusinessException(ErrorCode.NULL_ERROR, "删除数据不存在");
        }
//        return userService.removeById(id);
        boolean removeById = userService.removeById(id);
        return ResultUtils.success(removeById);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean userLogout = userService.userLogout(request);
        return ResultUtils.success(userLogout);
    }

    @GetMapping("/getuser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();

        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
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
