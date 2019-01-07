package lxk.controller;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lxk.model.CreateGroup;
import lxk.model.SequenceGroup;
import lxk.model.User;
import lxk.service.BtUserService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * @desc 用户管理控制器
 * 
 * @author zhumaer
 * @since 6/20/2017 16:37 PM
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    /**
     * @Description: 测试spring 直接注入OnlineChannelBusiness 接口的所有实现类到 map->channels中。key为实现类id
    */
    @Autowired
    protected Map<String,OnlineChannelBusiness> channels;

    @Autowired
    private BtUserService btUserService;



    /**@Validated({CreateGroup.class,SequenceGroup.class}
     *   校验顺序和  SequenceGroup  与 CreateGroup的顺序无关。
     *   此时仍然是SequenceGroup 里的first优先
     *   此时不会校验user里没有groups分组的字段(除非CreateGroup extends Default)，没加分组的字段默认是Default分组
     *
    */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping("/add")
    public User addUser(@Validated({CreateGroup.class, SequenceGroup.class}) @RequestBody User user) throws ParseException {
    //public User addUser(@Validated({CreateGroup.class}) @RequestBody User user) throws ParseException {

        log.info("-=---="+channels);

        btUserService.changePassword(user.getPwd(), "pwdpwd");

        String testEmail = "69671710@qq.com";
        Date testDate = new Date();
        Integer testRange = 10;

         //String testEmail = "69671710";
         //Date testDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2029-02-02 23:20:20");
         //Integer testRange = 100;
        return btUserService.insertAndReturn(user, testEmail, testDate, testRange);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping("/select")
    public User selectUser(@Validated(CreateGroup.class) @RequestBody User user,
                           @Length(min = 2) @RequestParam String id,
                           @Pattern(regexp = "^1[3-9]\\d{9}$", message = "{custom.phone.invalid}", groups = CreateGroup.class) @RequestParam String mobile) {
        String testEmail = "69671710@qq.com";
        Date testDate = new Date();
        Integer testRange = 100;
        return btUserService.insertAndReturn(user, testEmail, testDate, testRange);
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
