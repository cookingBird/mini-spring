package test.servlet;

import web.annotation.RequestMapping;

public class HelloWorldBean {

    public String doGet() {
        return "hello world!";
    }

    @RequestMapping("/test")
    public String doPost() {
        return "test requestMapping";
    }

    @RequestMapping("/helloworld")
    public String helloWorld() {
        return "hello world requestMapping";
    }
}
