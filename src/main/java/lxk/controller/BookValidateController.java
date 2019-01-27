package lxk.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lxk.model.Book;
import lxk.model.User;
import lxk.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author lxk
 * @use 校验入参
 */
@Controller("BookValidateController")
@RequestMapping(value = "book")
@Slf4j
public class BookValidateController {

    //@Resource
    private BookService bookService;


    /**
     * 校验 添加Book对象
     *
     * @param book
     */
    //@RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    //public Object addBook(@Valid Book book) {
    public Object addBook(@Validated @RequestBody Book book) {
    //public Object addBook(@Valid Book book) {
        log.info("book.toString() is [{}]", book.toString());
        return book;
    }

    @RequestMapping(value = "/addNoReturn", method = RequestMethod.POST)
    public void addBookNoreturn(@Valid Book book) {
        log.info("1book.toString() is [{}]", book.toString());
    }

    @RequestMapping(value = "/addNoReturn2", method = RequestMethod.POST)
    @ResponseBody
    public void addBookNoreturn2(@Valid Book book) {
        log.info("2book.toString() is [{}]", book.toString());
    }

    @RequestMapping(value = "/addReturn", method = RequestMethod.POST)
    @ResponseBody
    public String addBookreturn3(@Valid Book book) {
        log.info("3book.toString() is [{}]", book.toString());
        return "bookpostnoreturn3";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Object bookget() throws InterruptedException {
        val jsonObject = new JSONObject();
        jsonObject.put("testBookgetKey11", "testBookgetValue11");
        return jsonObject;
    }
    @RequestMapping(value = "/userGet", method = RequestMethod.GET)
    @ResponseBody
    public User userGet(User user) throws InterruptedException {
        return user;
    }

}
