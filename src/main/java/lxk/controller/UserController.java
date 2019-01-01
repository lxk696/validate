package lxk.controller;


import com.alibaba.fastjson.JSONObject;
import lombok.val;
import lxk.model.CreateGroup;
import lxk.model.User;
import lxk.service.BtUserService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

/**
 * @desc 用户管理控制器
 * 
 * @author zhumaer
 * @since 6/20/2017 16:37 PM
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BtUserService btUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping("/add")
    public User addUser(@Validated(CreateGroup.class) @RequestBody User user
    ) {
        return btUserService.insertAndReturn(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping("/select")
    public User selectUser(@Validated(CreateGroup.class) @RequestBody User user,
                           @Length(min = 2) @RequestParam String id,
                           @Pattern(regexp = "^1[3-9]\\d{9}$", message = "{custom.phone.invalid}", groups = CreateGroup.class) @RequestParam String mobile) {
        return btUserService.insertAndReturn(user);
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Object userGet() throws InterruptedException {
        val jsonObject = new JSONObject();
        jsonObject.put("testUserGetKey11", "testUserkgetValue11");
        return jsonObject;
    }

    @RequestMapping(value = "/userGet", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public User userGet(User user) throws InterruptedException {
        return user;
    }
}
