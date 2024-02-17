package test;

import beans.factory.annotation.Autowired;

public class BaseService {
    @Autowired
    BaseBaseService basebaseservice;

    public BaseService() {
    }

    public BaseService(BaseBaseService bbs) {
        this.basebaseservice = bbs;
    }

    public void sayHello() {
        System.out.println("Base Service says Hello");
        basebaseservice.sayHello();
    }

    public BaseBaseService getBaseBaseService() {
        return basebaseservice;
    }

    public void setBaseBaseService(BaseBaseService basebaseservice) {
        this.basebaseservice = basebaseservice;
    }
}
