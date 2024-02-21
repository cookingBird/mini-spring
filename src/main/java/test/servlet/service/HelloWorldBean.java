package test.servlet.service;

import test.servlet.User;
import web.bind.annotation.RequestMapping;
import web.bind.annotation.ResponseBody;
import web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class HelloWorldBean {

    public String doGet() {
        return "hello world!";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String doPost() {
        return "test requestMapping";
    }

    @RequestMapping("/hello-world")
    @ResponseBody
    public String helloWorld() {
        return "hello world requestMapping";
    }

    @RequestMapping("/test2")
    public void doTest2(HttpServletRequest request, HttpServletResponse response) {
        String str = "test 2, hello world!";
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping("/test5")
    public ModelAndView doTest5(User user) {
        ModelAndView mav = new ModelAndView("test", "msg", user.getName());
        return mav;
    }

    @RequestMapping("/test6")
    public String doTest6(User user) {
        return "error";
    }

    @RequestMapping("/test7")
    @ResponseBody
    public User doTest7(User user) {
        user.setName(user.getName() + "---");
        user.setBirthday(new Date());
        return user;
    }
}
