package lxk.controller;


import com.alibaba.fastjson.JSONObject;
import lombok.val;
import lxk.model.CreateGroup;
import lxk.model.User;
import lxk.service.BtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @desc 用户管理控制器
 * 
 * @author zhumaer
 * @since 6/20/2017 16:37 PM
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private BtUserService btUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Validated(CreateGroup.class) @RequestBody User user) {
        return btUserService.insertAndReturn(user);
    }




}
