package test.servlet;

import web.annotation.RequestMapping;

public class HelloWorldBean {

    public String doGet() {
        return "hello world!";
    }

    @RequestMapping("/test")
    public String doPost() {
        return "hello world! requestMapping";
    }
}
