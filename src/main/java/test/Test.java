package test;

import context.ClasspathXmlApplicationContext;
import test.ioc.AService;
import test.ioc.BaseBaseService;
import test.ioc.BaseService;

public class Test {
    public static void main(String[] args) {
        ClasspathXmlApplicationContext context = new ClasspathXmlApplicationContext("beans.xml");
        AService aService = null;
        BaseService baseService = null;
        BaseBaseService baseBaseService = null;
//        try {
//            aService = (AService) context.getBean("aservice");
//            aService.sayHello();
//            baseService = (BaseService) context.getBean("baseservice");
//            baseService.sayHello();
//            baseBaseService = (BaseBaseService) context.getBean("basebaseservice");
//            baseBaseService.sayHello();
//        } catch (BeansException e) {
//            e.printStackTrace();
//        }
    }
}
