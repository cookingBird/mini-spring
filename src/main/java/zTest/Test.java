package zTest;

import aop.DynamicProxy;
import beans.BeansException;
import context.ApplicationContext;
import context.ClasspathXmlApplicationContext;
import zTest.aop.ISubject;
import zTest.aop.ProxySubject;
import zTest.aop.RealSubject;
import zTest.ioc.AService;
import zTest.ioc.BaseBaseService;
import zTest.ioc.BaseService;

public class Test {

    public static void main(String[] args) throws BeansException {
        ClasspathXmlApplicationContext context = new ClasspathXmlApplicationContext("applicationContext.xml");
//        Test.testIOC(context);
        Test.testAOP(context);
    }

    public static void testIOC(ApplicationContext context) throws BeansException {
        AService aService = null;
        BaseService baseService = null;
        BaseBaseService baseBaseService = null;
        aService = (AService) context.getBean("aservice");
        aService.sayHello();
        baseService = (BaseService) context.getBean("baseservice");
        baseService.sayHello();
        baseBaseService = (BaseBaseService) context.getBean("basebaseservice");
        baseBaseService.sayHello();
    }

    public static void testAOP(ApplicationContext context) {

        ISubject subject = new ProxySubject();
        subject.doAction("hello world");

        ISubject subject2 = (ISubject) new DynamicProxy(new RealSubject()).getObject();
        subject2.doAction("hello world");
    }
}
