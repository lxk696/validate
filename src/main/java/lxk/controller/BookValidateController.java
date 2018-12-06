package lxk.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lxk.model.Book;
import lxk.model.User;
import lxk.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author lxk
 * @use 校验入参
 */
@Controller("BookValidateController")
@RequestMapping(value = "api/validate")
@Slf4j
public class BookValidateController {

    @Resource
    private BookService bookService;


    /**
     * 校验 添加Book对象
     *
     * @param book
     */
    @RequestMapping(value = "/bookpost", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public Object addBook(@Valid Book book) {
        log.info("book.toString() is [{}]", book.toString());
        return book;
    }

    @RequestMapping(value = "/bookpostnoreturn", method = RequestMethod.POST)
    public void addBookNoreturn(@Valid Book book) {
        log.info("1book.toString() is [{}]", book.toString());
    }

    @RequestMapping(value = "/bookpostnoreturn2", method = RequestMethod.POST)
    @ResponseBody
    public void addBookNoreturn2(@Valid Book book) {
        log.info("2book.toString() is [{}]", book.toString());
    }

    @RequestMapping(value = "/bookpostnoreturn3", method = RequestMethod.POST)
    @ResponseBody
    public String addBookNoreturn3(@Valid Book book) {
        log.info("3book.toString() is [{}]", book.toString());
        return "bookpostnoreturn3";
    }

    @RequestMapping(value = "/bookget", method = RequestMethod.GET)
    @ResponseBody
    public Object bookget() throws InterruptedException {
        val jsonObject = new JSONObject();
        jsonObject.put("testkey11", "testvalue11");
        return jsonObject;
    }
    @RequestMapping(value = "/userGet", method = RequestMethod.GET)
    @ResponseBody
    public User userGet(User user) throws InterruptedException {
        return user;
    }

}
